
package GameFramework.Input;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import GameFramework.*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;
import GameFramework.Model.Actors.*;

public class InputHelper
	extends JComponent
	implements KeyListener, MouseListener, MouseMotionListener, Platformable
{
	public Coords mouseClickPos;
	public Coords mouseMovePos;
	public Coords mouseClickPosPrev;
	public Coords mouseMovePosNext;
	public Coords mouseMovePosPrev;

	//public List<Object> gamepadsConnected;
	public Map<String,String> inputNamesLookup;
	public List<Input> inputsPressed;
	public HashMap<String, Input> inputsPressedByName;

	boolean isEnabled;
	boolean isMouseMovementTracked;

	public InputHelper()
	{
		// Helper variables.

		this.mouseClickPos = Coords.create();
		this.mouseMovePos = Coords.create();
		this.mouseMovePosPrev = Coords.create();
		this.mouseMovePosNext = Coords.create();

		var inputNames = Input.Names();
		this.inputNamesLookup = inputNames._AllByName;

		this.inputsPressed = new ArrayList<Input>();
		this.inputsPressedByName = new HashMap<String, Input>();

		this.isEnabled = true;
	}

	public List<ActorAction> actionsFromInput
	(
		Map<String,ActorAction> actionsByName,
		Map<String,ActionToInputsMapping> actionToInputsMappingsByInputName
	)
	{
		var returnValues = new ArrayList<ActorAction>();

		if (this.isEnabled == false)
		{
			return returnValues;
		}

		var inputsPressed = this.inputsPressed;
		for (var i = 0; i < inputsPressed.size(); i++)
		{
			var inputPressed = inputsPressed.get(i);
			if (inputPressed.isActive)
			{
				var mapping = actionToInputsMappingsByInputName.get(inputPressed.name);
				if (mapping != null)
				{
					var actionName = mapping.actionName;
					var action = actionsByName.get(actionName);
					returnValues.add(action);
					if (mapping.inactivateInputWhenActionPerformed)
					{
						inputPressed.isActive = false;
					}
				}
			}
		}

		return returnValues;
	}

	public void initialize(Universe universe)
	{
		this.inputsPressed = new ArrayList<Input>();
		//this.gamepadsConnected = new ArrayList<Object>();

		this.isMouseMovementTracked = true; // hack

		if (universe == null)
		{
			// hack - Allows use of this class
			// without including PlatformHelper or Universe.
			this.toJComponent();
		}
		else
		{
			universe.platformHelper.platformableAdd(this);
		}

		//this.gamepadsCheck();
	}

	public void inputAdd(String inputPressedName)
	{
		if (this.inputsPressedByName.containsKey(inputPressedName) == false)
		{
			var inputPressed = new Input(inputPressedName);
			this.inputsPressedByName.put(inputPressedName, inputPressed);
			this.inputsPressed.add(inputPressed);
		}
	}

	public void inputRemove(String inputReleasedName)
	{
		if (this.inputsPressedByName.containsKey(inputReleasedName))
		{
			var inputReleased = this.inputsPressedByName.get(inputReleasedName);
			this.inputsPressedByName.remove(inputReleasedName);
			ArrayHelper.remove(this.inputsPressed, inputReleased);
		}
	}

	public List<Input> inputsActive()
	{
		return this.inputsPressed.stream().filter
		(
			(x) -> x.isActive
		).collect(Collectors.toList());
	}

	public void inputsRemoveAll()
	{
		for (var i = 0; i < this.inputsPressed.size(); i++)
		{
			var input = this.inputsPressed.get(i);
			this.inputRemove(input.name);
		}
	}

	public boolean isMouseClicked()
	{
		var inputNameMouseClick = Input.Names().MouseClick;
		var inputPressed = this.inputsPressedByName.get(inputNameMouseClick);
		var returnValue = (inputPressed != null && inputPressed.isActive);
		return returnValue;
	}

	public boolean isMouseClicked(boolean value)
	{
		var returnValue = false;

		var inputNameMouseClick = Input.Names().MouseClick;

		if (value == true)
		{
			this.inputAdd(inputNameMouseClick);
		}
		else
		{
			this.inputRemove(inputNameMouseClick);
		}
	}

	public void updateForTimerTick(Universe universe)
	{
		//this.updateForTimerTick_Gamepads(universe);
	}

	/*
	public void updateForTimerTick_Gamepads(Universe universe)
	{
		var systemGamepads = this.systemGamepads();
		var inputNames = Input.Names();

		for (var i = 0; i < this.gamepadsConnected.length; i++)
		{
			var gamepad = this.gamepadsConnected[i];
			var systemGamepad = systemGamepads[gamepad.index];
			gamepad.updateFromSystemGamepad(systemGamepad);
			var gamepadID = "Gamepad" + i;

			var axisDisplacements = gamepad.axisDisplacements;
			for (var a = 0; a < axisDisplacements.length; a++)
			{
				var axisDisplacement = axisDisplacements[a];
				if (axisDisplacement == 0)
				{
					if (a == 0)
					{
						this.inputRemove(inputNames.GamepadMoveLeft + i);
						this.inputRemove(inputNames.GamepadMoveRight + i);
					}
					else
					{
						this.inputRemove(inputNames.GamepadMoveUp + i);
						this.inputRemove(inputNames.GamepadMoveDown + i);
					}
				}
				else
				{
					var gamepadIDMove = gamepadID + "Move"; // todo
					var directionName;
					if (a == 0)
					{
						directionName = (axisDisplacement < 0 ? "Left" : "Right");
					}
					else
					{
						directionName = (axisDisplacement < 0 ? "Up" : "Down");
					}

					this.inputAdd(gamepadIDMove + directionName);
				}
			} // end for

			var gamepadIDButton = gamepadID + "Button";
			var areButtonsPressed = gamepad.buttonsPressed;
			for (var b = 0; b < areButtonsPressed.length; b++)
			{
				var isButtonPressed = areButtonsPressed[b];

				if (isButtonPressed)
				{
					this.inputAdd(gamepadIDButton + b);
				}
				else
				{
					this.inputRemove(gamepadIDButton + b);
				}
			}
		}
	}
	*/

	// events

	// events - keyboard

	public void keyPressed(KeyEvent event)
	{
		var inputPressed = "" + event.getKeyChar();

		if (inputPressed == " ")
		{
			inputPressed = "_";
		}
		else if (inputPressed == "_")
		{
			inputPressed = "__";
		}
		else if (inputPressed.compareTo("0") >= 0 && inputPressed.compareTo("9") <= 0)
		{
			inputPressed = "_" + inputPressed;
		}

		this.inputAdd(inputPressed);
	}

	public void keyReleased(KeyEvent event)
	{
		var inputReleased = "" + event.getKeyChar();
		if (inputReleased == " ")
		{
			inputReleased = "_";
		}
		else if (inputReleased == "_")
		{
			inputReleased = "__";
		}
		else if
		(
			inputReleased.compareTo("0") >= 0
			&& inputReleased.compareTo("9") <= 0
		)
		{
			inputReleased = "_" + inputReleased;
		}

		this.inputRemove(inputReleased);
	}

	public void keyTyped(KeyEvent event)
	{
		// Do nothing.
	}

	// events - mouse

	public void mousePressed(MouseEvent event)
	{
		/*
		var canvas = event.target;
		var canvasBox = canvas.getBoundingClientRect();
		this.mouseClickPos.overwriteWithDimensions
		(
			event.clientX - canvasBox.left,
			event.clientY - canvasBox.top,
			0
		);
		this.inputAdd(Input.Names().MouseClick);
		*/
	}

	public void mouseMoved(MouseEvent event)
	{
		/*
		var canvas = event.target;
		var canvasBox = canvas.getBoundingClientRect();
		this.mouseMovePosNext.overwriteWithDimensions
		(
			event.clientX - canvasBox.left,
			event.clientY - canvasBox.top,
			0
		);

		if (this.mouseMovePosNext.equals(this.mouseMovePos) == false)
		{
			this.mouseMovePosPrev.overwriteWith(this.mouseMovePos);
			this.mouseMovePos.overwriteWith(this.mouseMovePosNext);
			this.inputAdd(Input.Names().MouseMove);
		}
		*/
	}

	public void mouseReleased(MouseEvent event)
	{
		this.inputRemove(Input.Names().MouseClick);
	}

	// gamepads

	/*
	public void gamepadsCheck()
	{
		var systemGamepads = this.systemGamepads();
		for (var i = 0; i < systemGamepads.length; i++)
		{
			var systemGamepad = systemGamepads[i];
			if (systemGamepad != null)
			{
				var gamepad = new Gamepad(); // todo
				this.gamepadsConnected.push(gamepad);
			}
		}
	}

	systemGamepads(): any
	{
		return navigator.getGamepads();
	}

	*/

	// Platformable.

	public JComponent toJComponent()
	{
		return this;
	}
}
