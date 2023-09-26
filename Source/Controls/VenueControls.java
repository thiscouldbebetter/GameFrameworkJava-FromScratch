
package Controls;

import Geometry.*;
import Helpers.*;
import Input.*;
import Model.*;
import Model.Actors.*;

import java.util.*;
import java.util.function.*;

public class VenueControls implements Venue
{
	public ControlBase controlRoot;
	public ActionToInputsMapping[] actionToInputsMappings;
	public Map<String, ActionToInputsMapping> actionToInputsMappingsByInputName;

	private Disposition _drawLoc;
	private Coords _mouseClickPos;
	private Coords _mouseMovePos;
	private Coords _mouseMovePosPrev;

	public VenueControls
	(
		ControlBase controlRoot, boolean ignoreKeyboardAndGamepadInputs
	)
	{
		this.controlRoot = controlRoot;

		Function<String,List<String>> buildGamepadInputs = (String inputName) ->
		{
			var numberOfGamepads = 1; // todo

			var returnValues = new ArrayList<String>();

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
		var actionToInputsMappingsArray = new ActionToInputsMapping[]
		{
			new ActionToInputsMapping
			(
				controlActionNames.ControlIncrement,
				ArrayHelper.addMany(
					new String[] { inputNames.ArrowDown },
					buildGamepadInputs.apply(inputNames.GamepadMoveDown)
				).toArray(String[]::new),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlPrev,
				ArrayHelper.addMany
				(
					new String[] { inputNames.ArrowLeft },
					buildGamepadInputs.apply(inputNames.GamepadMoveLeft)
				).toArray(String[]::new),
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
						buildGamepadInputs.apply(inputNames.GamepadMoveRight)
					).toArray(String[]::new)
				).toArray(String[]::new),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlDecrement,
				ArrayHelper.addMany
				(
					new String[] { inputNames.ArrowUp },
					buildGamepadInputs.apply(inputNames.GamepadMoveUp)
				).toArray(String[]::new),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlConfirm,
				ArrayHelper.addMany
				(
					new String[] { inputNames.Enter },
					buildGamepadInputs.apply(inputNames.GamepadButton1)
				).toArray(String[]::new),
				inactivate
			),
			new ActionToInputsMapping
			(
				controlActionNames.ControlCancel,
				ArrayHelper.addMany
				(
					new String[] { inputNames.Escape },
					buildGamepadInputs.apply(inputNames.GamepadButton0)
				).toArray(String[]::new),
				inactivate
			)
		};

		if (ignoreKeyboardAndGamepadInputs)
		{
			// ArrayHelper.clear(this.actionToInputsMappings);
		}

		var control = (ControlContainer)this.controlRoot;
		var mappings = control.actionToInputsMappings();
		var actionToInputsMappings = ArrayHelper.addMany
		(
			actionToInputsMappingsArray, Arrays.asList(mappings)
		);

		this.actionToInputsMappings =
			actionToInputsMappings.toArray(new ActionToInputsMapping[] {});

		this.actionToInputsMappingsByInputName =
			ActionToInputsMapping.mappingsToMappingsByInputName
			(
				this.actionToInputsMappings
			);

		// Helper variables.

		this._drawLoc = Disposition.create();
		this._mouseClickPos = Coords.create();
		this._mouseMovePos = Coords.create();
		this._mouseMovePosPrev = Coords.create();
	}

	public static VenueControls fromControl(ControlBase controlRoot)
	{
		return new VenueControls(controlRoot, false);
	}

	public void draw(Universe universe)
	{
		var display = universe.display;
		var drawLoc = this._drawLoc;
		drawLoc.pos.clear();
		ControlStyle styleOverrideNone = null;
		this.controlRoot.draw(universe, display, drawLoc, styleOverrideNone);
	}

	public void finalize(Universe universe) {}

	public void initialize(Universe universe) {}

	public void updateForTimerTick(Universe universe)
	{
		this.draw(universe);

		var inputHelper = universe.inputHelper;
		var inputsActive = inputHelper.inputsActive();
		var inputNames = Input.Names();

		for (var i = 0; i < inputsActive.size(); i++)
		{
			var inputActive = inputsActive.get(i);
			var inputActiveName = inputActive.name;

			var mapping = this.actionToInputsMappingsByInputName.get
			(
				inputActiveName
			);

			if (inputActiveName.startsWith("Mouse") == false)
			{
				if (mapping == null)
				{
					// Pass the raw input, to allow for text entry.
					var wasActionHandled = this.controlRoot.actionHandle
					(
						inputActiveName, universe
					);
					if (wasActionHandled)
					{
						inputActive.isActive = false;
					}
				}
				else
				{
					var actionName = mapping.actionName;
					this.controlRoot.actionHandle(actionName, universe);
					if (mapping.inactivateInputWhenActionPerformed)
					{
						inputActive.isActive = false;
					}
				}
			}
			else if (inputActiveName == inputNames.MouseClick)
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
					inputActive.isActive = false;
				}
			}
			else if (inputActiveName == inputNames.MouseMove)
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

		} // end for

	}

}
