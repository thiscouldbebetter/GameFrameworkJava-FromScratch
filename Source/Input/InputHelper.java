package Input;

import Geometry.*;
import Helpers.*;
import Model.*;
import Utility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class InputHelper
	extends JComponent
	implements KeyListener, MouseListener, MouseMotionListener, Platformable
{
	public java.util.List<Input> _inputsActive;
	public Map<String,Input> _inputsActiveByName;
	public java.util.List<Input> inputsPressed;
	public Map<String,Input> inputsPressedByName;
	public java.util.List<Integer> keyCodesPressed;
	public Coords mouseClickPos;
	public Coords mouseMovePos;
	public Coords mouseMovePosNext;
	public Coords mouseMovePosPrev;

	public void initialize(Universe universe)
	{
		var platformHelper = universe.platformHelper;
		platformHelper.platformableAdd(this);

		this._inputsActive = new ArrayList<Input>();
		this._inputsActiveByName = new HashMap<String,Input>();
		this.inputsPressed = new ArrayList<Input>();
		this.inputsPressedByName = new HashMap<String,Input>();
		this.keyCodesPressed = new ArrayList<Integer>();
		this.mouseClickPos = Coords.zeroes();
		this.mouseMovePos = Coords.zeroes();
		this.mouseMovePosNext = Coords.zeroes();
		this.mouseMovePosPrev = Coords.zeroes();
	}

	public void updateForTimerTick(Universe universe)
	{
		// todo
	}

	public void inputActivate(Input inputToActivate)
	{
		var inputToActivateName = inputToActivate.name;

		if (this._inputsActiveByName.get(inputToActivateName) == null)
		{
			this._inputsActiveByName.put(inputToActivateName, inputToActivate);
			this._inputsActive.add(inputToActivate);
		}
	}

	public void inputAdd(String inputPressedName)
	{
		if (this.inputsPressedByName.get(inputPressedName) == null)
		{
			var inputPressed = new Input(inputPressedName);
			this.inputsPressedByName.put(inputPressedName, inputPressed);
			this.inputsPressed.add(inputPressed);
			this.inputActivate(inputPressed);
		}
	}

	public void inputInactivate(Input inputToInactivate)
	{
		var inputToInactivateName = inputToInactivate.name;

		var inputToInactivateExisting =
			this._inputsActiveByName.get(inputToInactivateName);

		if (inputToInactivateExisting != null)
		{
			this._inputsActiveByName.remove(inputToInactivateName);
			ArrayHelper.remove(this._inputsActive, inputToInactivateExisting);
		}
	}

	public void inputRemove(String inputReleasedName)
	{
		var inputReleased = this.inputsPressedByName.get(inputReleasedName);

		if (inputReleased != null)
		{
			this.inputInactivate(inputReleased);
			this.inputsPressedByName.remove(inputReleasedName);
			ArrayHelper.remove(this.inputsPressed, inputReleased);
		}
	}

	public java.util.List<Input> inputsActive()
	{
		return this._inputsActive;
	}

	public void inputsRemoveAll()
	{
		var inputsPressed = this.inputsPressed;
		for (var i = 0; i < inputsPressed.size(); i++)
		{
			var input = inputsPressed.get(i);
			this.inputRemove(input.name);
		}

		var inputsActive = this._inputsActive;
		for (var i = 0; i < inputsActive.size(); i++)
		{
			var input = inputsActive.get(i);
			this.inputRemove(input.name);
		}
	}

	// KeyListener.

	public void keyPressed(KeyEvent e)
	{
		var inputPressed = e.getKeyText(e.getKeyCode() );

		/*
		if (this.keysToPreventDefaultsFor.indexOf(inputPressed) >= 0)
		{
			event.preventDefault();
		}
		*/

		if (inputPressed == " ")
		{
			inputPressed = "_";
		}
		else if (inputPressed == "_")
		{
			inputPressed = "__";
		}
		/*
		else if (parseFloat(inputPressed) == null)
		{
			inputPressed = "_" + inputPressed;
		}
		*/

		this.inputAdd(inputPressed);

	}

	public void keyReleased(KeyEvent e)
	{
		var inputReleased = e.getKeyText(e.getKeyCode() );

		if (inputReleased == " ")
		{
			inputReleased = "_";
		}
		else if (inputReleased == "_")
		{
			inputReleased = "__";
		}
		/*
		else if (parseFloat(inputReleased) == null)
		{
			inputReleased = "_" + inputReleased;
		}
		*/

		this.inputRemove(inputReleased);
	}


	public void keyTyped(KeyEvent e)
	{
		// Do nothing.
	}

	// MouseListener.

	public void mouseClicked(MouseEvent event)
	{
		// Do nothing.
	}

	public void mousePressed(MouseEvent event)
	{
		this.mouseClickPos.overwriteWithDimensions
		(
			event.getX(),
			event.getY(),
			0
		);

		this.inputAdd(Input.Names().MouseClick);
	}

	public void mouseReleased(MouseEvent event)
	{
		this.inputRemove(Input.Names().MouseClick);
	}

	// MouseMoveListener.

	public void mouseDragged(MouseEvent event)
	{
		// Do nothing.
	}

	public void mouseEntered(MouseEvent event)
	{
		// Do nothing.
	}

	public void mouseExited(MouseEvent event)
	{
		// Do nothing.
	}

	public void mouseMoved(MouseEvent event)
	{
		this.mouseMovePosNext.overwriteWithDimensions
		(
			event.getX(),
			event.getY(),
			0
		);

		if (this.mouseMovePosNext.equals(this.mouseMovePos) == false)
		{
			this.mouseMovePosPrev.overwriteWith(this.mouseMovePos);
			this.mouseMovePos.overwriteWith(this.mouseMovePosNext);
			this.inputAdd(Input.Names().MouseMove);
		}
	}


	// Platformable.

	public boolean isKeyListener()
	{
		return true;
	}

	public boolean isMouseListener()
	{
		return true;
	}

	public JComponent toJComponent()
	{
		return this;
	}

}
