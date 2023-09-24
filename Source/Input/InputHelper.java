package Input;

import Geometry.*;
import Helpers.*;
import Model.*;
import Utility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class InputHelper extends JComponent implements KeyListener, Platformable
{
	public java.util.List<Input> inputsPressed;
	public Map<String,Input> inputsPressedByName;
	public java.util.List<Integer> keyCodesPressed;
	public Coords mouseClickPos;
	public Coords mouseMovePos;
	public Coords mouseMovePosPrev;

	public void initialize(Universe universe)
	{
		var platformHelper = universe.platformHelper;
		platformHelper.platformableAdd(this);

		this.inputsPressed = new ArrayList<Input>();
		this.inputsPressedByName = new HashMap<String,Input>();
		this.keyCodesPressed = new ArrayList<Integer>();
		this.mouseClickPos = Coords.zeroes();
		this.mouseMovePos = Coords.zeroes();
		this.mouseMovePosPrev = Coords.zeroes();
	}

	public void updateForTimerTick(Universe universe)
	{
		// todo
	}

	// Ported methods.

	public void inputAdd(String inputPressedName)
	{
		if (this.inputsPressedByName.get(inputPressedName) == null)
		{
			var inputPressed = new Input(inputPressedName);
			this.inputsPressedByName.put(inputPressedName, inputPressed);
			this.inputsPressed.add(inputPressed);
		}
	}

	public void inputRemove(String inputReleasedName)
	{
		if (this.inputsPressedByName.get(inputReleasedName) != null)
		{
			var inputReleased = this.inputsPressedByName.get(inputReleasedName);
			this.inputsPressedByName.remove(inputReleasedName);
			ArrayHelper.remove(this.inputsPressed, inputReleased);
		}
	}

	public java.util.List<Input> inputsActive()
	{
		var inputsActive = new ArrayList<Input>();
		for (var i = 0; i < this.inputsPressed.size(); i++)
		{
			var input = this.inputsPressed.get(i);
			if (input.isActive)
			{
				inputsActive.add(input);
			}
		}
		return inputsActive;
	}

	public void inputsRemoveAll()
	{
		for (var i = 0; i < this.inputsPressed.size(); i++)
		{
			var input = this.inputsPressed.get(i);
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

	// Platformable.

	public boolean isKeyListener()
	{
		return true;
	}

	public JComponent toJComponent()
	{
		return this;
	}

}
