
package GameFramework.Controls;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Model.*;

public class VenueControls implements Venue
{
	public ControlBase controlRoot;
	public ActionToInputsMapping[] actionToInputsMappings;
	public Map<String, ActionToInputsMapping> actionToInputsMappingsByInputName;

	private _Disposition drawLoc;
	private _Coords mouseClickPos;
	private _Coords mouseMovePos;
	private _Coords mouseMovePosPrev;

	public VenueControls
	(
		ControlBase controlRoot, boolean ignoreKeyboardAndGamepadInputs
	)
	{
		this.controlRoot = controlRoot;
		ignoreKeyboardAndGamepadInputs =
		(
			ignoreKeyboardAndGamepadInputs != null
			? ignoreKeyboardAndGamepadInputs
			: false
		);

		var buildGamepadInputs = (String inputName) ->
		{
			var numberOfGamepads = 1; // todo

			var returnValues = new ArrayList<Input>();

			for (var i = 0; i < numberOfGamepads; i++)
			{
				var inputNameForGamepad = inputName + i;
				returnValues.add(inputNameForGamepad);
			}

			return returnValues;
		};

		var controlActionNames = ControlActionNames.Instances();
		var inputNames = Input.Names();

		var inactivate = true;
		this.actionToInputsMappings = new Array<ActionToInputsMapping>
		(
			new ActionToInputsMapping
			(
				controlActionNames.ControlIncrement,
				ArrayHelper.addMany(
					new String[] { inputNames.ArrowDown },
					buildGamepadInputs(inputNames.GamepadMoveDown)
				),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlPrev,
				ArrayHelper.addMany
				(
					new String[] { inputNames.ArrowLeft },
					buildGamepadInputs(inputNames.GamepadMoveLeft)
				),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlNext,
				ArrayHelper.addMany
				(
					new String[] { inputNames.ArrowRight },
					ArrayHelper.addMany
					(
						new String[] { inputNames.ArrowRight, inputNames.Tab },
						buildGamepadInputs(inputNames.GamepadMoveRight)
					)
				),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlDecrement,
				ArrayHelper.addMany
				(
					new String[] { inputNames.ArrowUp },
					buildGamepadInputs(inputNames.GamepadMoveUp)
				),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlConfirm,
				ArrayHelper.addMany
				(
					new String[] { inputNames.Enter },
					buildGamepadInputs(inputNames.GamepadButton1)
				),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlCancel,
				ArrayHelper.addMany
				(
					new String[] { inputNames.Escape },
					buildGamepadInputs(inputNames.GamepadButton0)
				),
				inactivate
			)
		);

		if (ignoreKeyboardAndGamepadInputs)
		{
			this.actionToInputsMappings.length = 0;
		}

		var mappingsGet = this.controlRoot.actionToInputsMappings;
		if (mappingsGet != null)
		{
			var mappings = mappingsGet.call(this.controlRoot);
			this.actionToInputsMappings.addAll(mappings);
		}

		this.actionToInputsMappingsByInputName = ArrayHelper.addLookupsMultiple
		(
			this.actionToInputsMappings,
			(ActionToInputsMapping x) -> x.inputNames
		);

		// Helper variables.

		this._drawLoc = Disposition.create();
		this._mouseClickPos = Coords.create();
		this._mouseMovePos = Coords.create();
		this._mouseMovePosPrev = Coords.create();
	}

	static fromControl(ControlBase controlRoot): VenueControls
	{
		return new VenueControls(controlRoot, false);
	}

	draw(Universe universe)
	{
		var display = universe.display;
		var drawLoc = this._drawLoc;
		drawLoc.pos.clear();
		var styleOverrideNone = null;
		this.controlRoot.draw(universe, display, drawLoc, styleOverrideNone);
	}

	public void finalize(Universe universe) {}

	public void initialize(Universe universe) {}

	public void updateForTimerTick(Universe universe)
	{
		this.draw(universe);

		var inputHelper = universe.inputHelper;
		var inputsPressed = inputHelper.inputsPressed;
		var inputNames = Input.Names();

		for (var i = 0; i < inputsPressed.length; i++)
		{
			var inputPressed = inputsPressed[i];
			if (inputPressed.isActive)
			{
				var inputPressedName = inputPressed.name;

				var mapping = this.actionToInputsMappingsByInputName.get
				(
					inputPressedName
				);

				if (inputPressedName.startsWith("Mouse") == false)
				{
					if (mapping == null)
					{
						// Pass the raw input, to allow for text entry.
						var wasActionHandled = this.controlRoot.actionHandle
						(
							inputPressedName, universe
						);
						if (wasActionHandled)
						{
							inputPressed.isActive = false;
						}
					}
					else
					{
						var actionName = mapping.actionName;
						this.controlRoot.actionHandle(actionName, universe);
						if (mapping.inactivateInputWhenActionPerformed)
						{
							inputPressed.isActive = false;
						}
					}
				}
				else if (inputPressedName == inputNames.MouseClick)
				{
					this._mouseClickPos.overwriteWith
					(
						inputHelper.mouseClickPos
					).divide
					(
						universe.display.scaleFactor()
					);
					var wasClickHandled =
						this.controlRoot.mouseClick(this._mouseClickPos);
					if (wasClickHandled)
					{
						//inputHelper.inputRemove(inputPressed);
						inputPressed.isActive = false;
					}
				}
				else if (inputPressedName == inputNames.MouseMove)
				{
					this._mouseMovePos.overwriteWith
					(
						inputHelper.mouseMovePos
					).divide
					(
						universe.display.scaleFactor()
					);
					this._mouseMovePosPrev.overwriteWith
					(
						inputHelper.mouseMovePosPrev
					).divide
					(
						universe.display.scaleFactor()
					);

					this.controlRoot.mouseMove
					(
						this._mouseMovePos //, this._mouseMovePosPrev
					);
				}

			} // end if isActive

		} // end for

	}

}
