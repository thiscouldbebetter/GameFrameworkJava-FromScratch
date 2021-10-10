
package GameFramework.Controls;

import java.util.*;
import java.util.function.*;

import GameFramework.*;
import GameFramework.Geometry.*;
import GameFramework.Inputs.*;
import GameFramework.Model.*;

public class ControlBuilder
{
	public ControlStyle[] styles;
	public Map<String,ControlStyle> stylesByName;
	public BiFunction<Venue,Venue,Venue> venuevenueTransitionalFromTo;

	public double buttonHeightBase;
	public double buttonHeightSmallBase;
	public double fontHeightInPixelsBase;
	public Coords sizeBase;

	private _Coords zeroes;
	private _Coords scaleMultiplier;

	public ControlBuilder
	(
		List<ControlStyle> styles,
		BiFunction<Venue,Venue,Venue> venueTransitionalFromTo
	)
	{
		this.styles = (styles != null ? styles : this.Instances()._All);
		this.venueTransitionalFromTo =
		(
			venueTransitionalFromTo != null
			? venueTransitional
			: this.venueFaderFromTo
		);

		this.stylesByName = ArrayHelper.addLookupsByName(this.styles);

		this.fontHeightInPixelsBase = 10;
		this.buttonHeightBase = this.fontHeightInPixelsBase * 2;
		this.buttonHeightSmallBase = this.fontHeightInPixelsBase * 1.5;
		this.sizeBase = new Coords(200, 150, 1);

		// Helper variables.

		this._zeroes = Coords.create();
		this._scaleMultiplier = Coords.create();
	}

	public static ControlBuilder create()
	{
		return new ControlBuilder(null, null);
	}

	public ControlStyle styleByName(String styleName)
	{
		return this.stylesByName.get(styleName);
	}

	public ControlStyle styleDefault()
	{
		return this.styles[0];
	}

	public VenueFader venueFaderFromTo(Venue vFrom, Venue vTo)
	{
		if (vTo.constructor.name == VenueFader.name)
		{
			vTo = ((VenueFader)vTo).venueToFadeTo();
		}
		var returnValue = VenueFader.fromVenuesToAndFrom(vTo, vFrom);
		return returnValue;
	}

	// Controls.

	public ControlBase choice
	(
		Universe universe,
		Coords size,
		DataBinding<Object, String> message,
		List<String> optionNames,
		List<Object> optionFunctions,
		boolean showMessageOnly
	)
	{
		size = (size != null ? size : universe.display.sizeDefault());
		showMessageOnly = (showMessageOnly != null ? showMessageOnly : false);

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);
		var fontHeight = this.fontHeightInPixelsBase;

		var doubleOfLinesInMessageMinusOne = message.get().split("\n").length - 1;
		var labelSize = Coords.fromXY
		(
			200, fontHeight * doubleOfLinesInMessageMinusOne
		);

		var doubleOfOptions = optionNames.length;

		if (showMessageOnly && doubleOfOptions == 1)
		{
			doubleOfOptions = 0; // Is a single option really an option?
		}

		var labelPosYBase = (doubleOfOptions > 0 ? 65 : 75); // hack

		var labelPos = Coords.fromXY
		(
			100, labelPosYBase - fontHeight * (doubleOfLinesInMessageMinusOne / 4)
		);

		var labelMessage = new ControlLabel
		(
			"labelMessage",
			labelPos,
			labelSize,
			true, // isTextCentered
			message,
			fontHeight
		);

		var childControls = new Object[] { labelMessage };

		if (showMessageOnly == false)
		{
			var buttonWidth = 55;
			var buttonSize = Coords.fromXY(buttonWidth, fontHeight * 2);
			var spaceBetweenButtons = 5;
			var buttonMarginLeftRight =
				(
					this.sizeBase.x
					- (buttonWidth * doubleOfOptions)
					- (spaceBetweenButtons * (doubleOfOptions - 1))
				) / 2;

			for (var i = 0; i < doubleOfOptions; i++)
			{
				var button = ControlButton.from9
				(
					"buttonOption" + i,
					Coords.fromXY
					(
						buttonMarginLeftRight + i
							* (buttonWidth + spaceBetweenButtons),
						100
					), // pos
					buttonSize.clone(),
					optionNames[i],
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					optionFunctions[i],
					universe
				);

				childControls.add(button);
			}
		}

		var containerSizeScaled =
			size.clone().clearZ().divide(scaleMultiplier);
		var display = universe.display;
		var displaySize =
			display.sizeDefault().clone().clearZ().divide(scaleMultiplier);
		var containerPosScaled =
			displaySize.clone().subtract(containerSizeScaled).half();
		var actions = null;
		if (doubleOfOptions <= 1)
		{
			var acknowledge = optionFunctions[0];
			var controlActionNames = ControlActionNames.Instances();
			actions = new Action[]
			{
				new Action( controlActionNames.ControlCancel, acknowledge ),
				new Action( controlActionNames.ControlConfirm, acknowledge ),
			};
		}

		var returnValue = new ControlContainer
		(
			"containerChoice",
			containerPosScaled,
			containerSizeScaled,
			childControls,
			actions,
			null //?
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		if (showMessageOnly)
		{
			returnValue = new ControlContainerTransparent(returnValue);
		}

		return returnValue;
	}

	public ControlBase choiceList
	(
		Universe universe,
		Coords size,
		String message,
		DataBinding<Object,Object[]> options,
		DataBinding<Object,Object> bindingForOptionText,
		String buttonSelectText,
		BiConsumer<Universe,Object> select
	)
	{
		// todo - Variable sizes.

		size = (size != null ? size : universe.display.sizeDefault());

		var marginWidth = 10;
		var marginSize = Coords.fromXY(1, 1).multiplyScalar(marginWidth);
		var fontHeight = 20;
		var labelSize = Coords.fromXY(size.x - marginSize.x * 2, fontHeight);
		var buttonSize = Coords.fromXY(labelSize.x, fontHeight * 2);
		var listSize = Coords.fromXY
		(
			labelSize.x,
			size.y - labelSize.y - buttonSize.y - marginSize.y * 4
		);

		var listOptions = ControlList.from8
		(
			"listOptions",
			Coords.fromXY(marginSize.x, labelSize.y + marginSize.y * 2),
			listSize,
			options,
			bindingForOptionText,
			fontHeight,
			null, // bindingForItemSelected
			null // bindingForItemValue
		);

		var returnValue = ControlContainer.from4
		(
			"containerChoice",
			Coords.create(),
			size,
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelMessage",
					Coords.fromXY(size.x / 2, marginSize.y + fontHeight / 2),
					labelSize,
					true, // isTextCentered
					message,
					fontHeight
				),

				listOptions,

				new ControlButton
				(
					"buttonSelect",
					Coords.fromXY(marginSize.x, size.y - marginSize.y - buttonSize.y),
					buttonSize,
					buttonSelectText,
					fontHeight,
					true, // hasBorder
					true, // isEnabled,
					() -> // click
					{
						var itemSelected = listOptions.itemSelected(null);
						if (itemSelected != null)
						{
							select(universe, itemSelected);
						}
					},
					universe, // context
					false // canBeHeldDown
				),
			}
		);

		return returnValue;
	}

	public ControlBase confirm
	(
		Universe universe,
		Coords size,
		String message,
		Runnable confirm,
		Runnable cancel
	)
	{
		return this.choice
		(
			universe, size, DataBinding.fromContext(message),
			new String[] { "Confirm", "Cancel" },
			new Runnable[] { confirm, cancel },
			null
		);
	}

	public ControlBase confirmAndReturnToVenue
	(
		Universe universe,
		Coords size,
		String message,
		Venue venuePrev,
		Runnable confirm,
		Runnable cancel
	)
	{
		var controlBuilder = this;

		var confirmThenReturnToVenuePrev = () ->
		{
			confirm();
			var venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venuePrev
			);
			universe.venueNext = venueNext;
		};

		var cancelThenReturnToVenuePrev = () ->
		{
			if (cancel != null)
			{
				cancel();
			}
			var venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venuePrev
			);
			universe.venueNext = venueNext;
		};

		return this.choice
		(
			universe, size, DataBinding.fromContext(message),
			new String[] { "Confirm", "Cancel" },
			new Runnable[]
			{
				confirmThenReturnToVenuePrev, cancelThenReturnToVenuePrev
			},
			null
		);
	}

	public ControlBase game(Universe universe, Coords size, Venue venuePrev)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault().clone();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var buttonHeight = this.buttonHeightBase;
		var padding = 5;
		var rowHeight = buttonHeight + padding;
		var rowCount = 5;
		var buttonsAllHeight = rowCount * buttonHeight + (rowCount - 1) * padding;
		var margin = (this.sizeBase.y - buttonsAllHeight) / 2;

		var buttonSize = Coords.fromXY(60, buttonHeight);
		var posX = (this.sizeBase.x - buttonSize.x) / 2;

		var row0PosY = margin;
		var row1PosY = row0PosY + rowHeight;
		var row2PosY = row1PosY + rowHeight;
		var row3PosY = row2PosY + rowHeight;
		var row4PosY = row3PosY + rowHeight;

		var back = () ->
		{
			var venueNext = venuePrev;
			venueNext = controlBuilder.venueTransitionalFromTo
			(
				 universe.venueCurrent, venueNext
			);
			universe.venueNext = venueNext;
		};

		var returnValue = new ControlContainer
		(
			"containerStorage",
			this._zeroes, // pos
			this.sizeBase.clone(),
			// children
			new ControlBase[]
			{
				ControlButton.from8
				(
					"buttonSave",
					Coords.fromXY(posX, row0PosY), // pos
					buttonSize.clone(),
					"Save",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						Venue venueNext = Profile.toControlSaveStateSave
						(
							universe, size, universe.venueCurrent
						).toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonLoad",
					Coords.fromXY(posX, row1PosY), // pos
					buttonSize.clone(),
					"Load",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						Venue venueNext = Profile.toControlSaveStateLoad
						(
							universe, null, universe.venueCurrent
						).toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonAbout",
					Coords.fromXY(posX, row2PosY), // pos
					buttonSize.clone(),
					"About",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var venueCurrent = universe.venueCurrent;
						Venue venueNext = new VenueMessage
						(
							DataBinding.fromContext(universe.name + "\nv" + universe.version),
							() -> // acknowledge
							{
								var venueNext = controlBuilder.venueTransitionalFromTo
								(
									null, venueCurrent
								);
								universe.venueNext = venueNext;
							},
							universe.venueCurrent, // venuePrev
							size,
							false
						);
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonQuit",
					Coords.fromXY(posX, row3PosY), // pos
					buttonSize.clone(),
					"Quit",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var controlConfirm = universe.controlBuilder.confirm
						(
							universe,
							size,
							"Are you sure you want to quit?",
							() -> // confirm
							{
								universe.reset();
								Venue venueNext =
									controlBuilder.title(universe, null).toVenue();
								venueNext = controlBuilder.venueTransitionalFromTo
								(
									universe.venueCurrent, venueNext
								);
								universe.venueNext = venueNext;
							},
							() -> // cancel
							{
								var venueNext = venuePrev;
								venueNext = controlBuilder.venueTransitionalFromTo
								(
									universe.venueCurrent, venueNext
								);
								universe.venueNext = venueNext;
							}
						);

						Venue venueNext = controlConfirm.toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonBack",
					Coords.fromXY(posX, row4PosY), // pos
					buttonSize.clone(),
					"Back",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					back // click
				),
			},

			new Action[] { new Action("Back", back) },

			new ActionToInputsMapping[]
			{
				new ActionToInputsMapping
				(
					"Back", new String[] { "Escape" }, true
				)
			}

		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase gameAndSettings1(Universe universe)
	{
		return this.gameAndSettings(universe, null, universe.venueCurrent, true);
	}

	public ControlBase gameAndSettings
	(
		Universe universe, Coords size, Venue venuePrev, boolean includeResumeButton
	)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault().clone();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var buttonWidth = 40;
		var buttonHeight = this.buttonHeightBase;
		var padding = 5;
		var rowCount = (includeResumeButton ? 3 : 2);
		var rowHeight = buttonHeight + padding;
		var buttonsAllHeight =
			rowCount * buttonHeight + (rowCount - 1) * padding;
		var margin = Coords.fromXY
		(
			(this.sizeBase.x - buttonWidth) / 2,
			(this.sizeBase.y - buttonsAllHeight) / 2
		);

		var row0PosY = margin.y;
		var row1PosY = row0PosY + rowHeight;
		var row2PosY = row1PosY + rowHeight;

		var returnValue = new ControlContainer
		(
			"Game",
			this._zeroes.clone(), // pos
			this.sizeBase.clone(),
			// children
			new ControlBase[]
			{
				ControlButton.from8
				(
					"buttonGame",
					Coords.fromXY(margin.x, row0PosY), // pos
					Coords.fromXY(buttonWidth, buttonHeight), // size
					"Game",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						Venue venueNext = controlBuilder.game
						(
							universe, null, universe.venueCurrent
						).toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonSettings",
					Coords.fromXY(margin.x, row1PosY), // pos
					Coords.fromXY(buttonWidth, buttonHeight), // size
					"Settings",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						Venue venueNext = controlBuilder.settings
						(
							universe, null, universe.venueCurrent
						).toVenue(),
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),
			},
			null, // actions
			null // mappings
		);

		if (includeResumeButton)
		{
			var back = () ->
			{
				Venue venueNext = venuePrev;
				venueNext = controlBuilder.venueTransitionalFromTo
				(
					universe.venueCurrent, venueNext
				);
				universe.venueNext = venueNext;
			};

			var buttonResume = ControlButton.from8
			(
				"buttonResume",
				Coords.fromXY(margin.x, row2PosY), // pos
				Coords.fromXY(buttonWidth, buttonHeight), // size
				"Resume",
				fontHeight,
				true, // hasBorder
				true, // isEnabled
				back
			);

			returnValue.children.add(buttonResume);

			returnValue.actions.add(new Action("Back", back));

			returnValue._actionToInputsMappings.add
			(
				new ActionToInputsMapping
				(
					"Back", new String[] { "Escape" }, true
				)
			);
		}

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase inputs(Universe universe, Coords size, Venue venuePrev)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var world = universe.world;

		// hack - Should do ALL placeDefns, not just the current one.
		var placeCurrent = world.placeCurrent;
		var placeDefn = placeCurrent.defn(world);

		var returnValue = ControlContainer.from4
		(
			"containerGameControls",
			this._zeroes, // pos
			this.sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelActions",
					Coords.fromXY(100, 15), // pos
					Coords.fromXY(100, 20), // size
					true, // isTextCentered
					"Actions:",
					fontHeight
				),

				ControlList.from8
				(
					"listActions",
					Coords.fromXY(50, 25), // pos
					Coords.fromXY(100, 40), // size
					DataBinding.fromContext(placeDefn.actionToInputsMappingsEdited), // items
					DataBinding.fromGet
					(
						(ActionToInputsMapping c) -> { return c.actionName; }
					), // bindingForItemText
					fontHeight,
					new DataBinding
					(
						placeDefn,
						(PlaceDefn c) -> c.actionToInputsMappingSelected,
						(PlaceDefn c, ActionToInputsMapping v) -> { c.actionToInputsMappingSelected = v; }
					), // bindingForItemSelected
					DataBinding.fromGet( (ActionToInputsMapping c) -> c ) // bindingForItemValue
				),

				new ControlLabel
				(
					"labelInput",
					Coords.fromXY(100, 70), // pos
					Coords.fromXY(100, 15), // size
					true, // isTextCentered
					"Inputs:",
					fontHeight
				),

				new ControlLabel
				(
					"infoInput",
					Coords.fromXY(100, 80), // pos
					Coords.fromXY(200, 15), // size
					true, // isTextCentered
					DataBinding.fromContextAndGet
					(
						placeDefn,
						(PlaceDefn c) ->
						{
							var i = c.actionToInputsMappingSelected;
							return (i == null ? "-" : i.inputNames.join(", "));
						}
					), // text
					fontHeight
				),

				ControlButton.from8
				(
					"buttonClear",
					Coords.fromXY(25, 90), // pos
					Coords.fromXY(45, 15), // size
					"Clear",
					fontHeight,
					true, // hasBorder
					DataBinding.fromContextAndGet
					(
						placeDefn,
						(PlaceDefn c) -> c.actionToInputsMappingSelected
					), // isEnabled
					() -> // click
					{
						var mappingSelected = placeDefn.actionToInputsMappingSelected;
						if (mappingSelected != null)
						{
							mappingSelected.inputNames.length = 0;
						}
					}
				),

				ControlButton.from8
				(
					"buttonAdd",
					Coords.fromXY(80, 90), // pos
					Coords.fromXY(45, 15), // size
					"Add",
					fontHeight,
					true, // hasBorder
					DataBinding.fromContextAndGet
					(
						placeDefn,
						(PlaceDefn c) -> c.actionToInputsMappingSelected
					), // isEnabled
					() -> // click
					{
						var mappingSelected = placeDefn.actionToInputsMappingSelected;
						if (mappingSelected != null)
						{
							var venueInputCapture = new VenueInputCapture
							(
								universe.venueCurrent,
								(Input inputCaptured) ->
								{
									var inputName = inputCaptured.name;
									mappingSelected.inputNames.add(inputName);
								}
							);
							universe.venueNext = venueInputCapture;
						}
					}
				),

				ControlButton.from8
				(
					"buttonRestoreDefault",
					Coords.fromXY(135, 90), // pos
					Coords.fromXY(45, 15), // size
					"Default",
					fontHeight,
					true, // hasBorder
					DataBinding.fromContextAndGet
					(
						placeDefn,
						(PlaceDefn c) -> (c.actionToInputsMappingSelected != null)
					), // isEnabled
					() -> // click
					{
						var mappingSelected = placeDefn.actionToInputsMappingSelected;
						if (mappingSelected != null)
						{
							var mappingDefault = placeDefn.actionToInputsMappingsDefault.filter
							(
								(ActionToInputsMapping x) -> (x.actionName == mappingSelected.actionName)
							)[0];
							mappingSelected.inputNames = mappingDefault.inputNames.slice();
						}
					}
				),

				ControlButton.from8
				(
					"buttonRestoreDefaultsAll",
					Coords.fromXY(50, 110), // pos
					Coords.fromXY(100, 15), // size
					"Default All",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() ->
					{
						var venueInputs = universe.venueCurrent;
						var controlConfirm = universe.controlBuilder.confirmAndReturnToVenue
						(
							universe,
							size,
							"Are you sure you want to restore defaults?",
							venueInputs,
							() -> // confirm
							{
								placeDefn.actionToInputsMappingsRestoreDefaults();
							},
							null // cancel
						);
						Venue venueNext = controlConfirm.toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonCancel",
					Coords.fromXY(50, 130), // pos
					Coords.fromXY(45, 15), // size
					"Cancel",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						Venue venueNext = venuePrev;
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonSave",
					Coords.fromXY(105, 130), // pos
					Coords.fromXY(45, 15), // size
					"Save",
					fontHeight,
					true, // hasBorder
					// isEnabled
					DataBinding.fromContextAndGet
					(
						placeDefn,
						(PlaceDefn c) ->
						{
							var mappings = c.actionToInputsMappingsEdited;
							var doAnyActionsLackInputs = mappings.some
							(
								(ActionToInputsMapping x) -> (x.inputNames.length == 0)
							);
							return (doAnyActionsLackInputs == false);
						}
					),
					() -> // click
					{
						placeDefn.actionToInputsMappingsSave();
						Venue venueNext = venuePrev;
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				)
			}
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase message
	(
		Universe universe,
		Coords size,
		DataBinding<Object, String> message,
		Runnable acknowledge,
		boolean showMessageOnly
	)
	{
		var optionNames = new String[] {};
		var optionFunctions = new Runnable[] {};

		if (acknowledge != null)
		{
			optionNames.add("Acknowledge");
			optionFunctions.add(acknowledge);
		}

		return this.choice
		(
			universe, size, message, optionNames, optionFunctions, showMessageOnly
		);
	}

	public ControlBase opening(Universe universe, Coords size)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var goToVenueNext = () ->
		{
			universe.soundHelper.soundsAllStop(universe);

			var venueNext = this.producer(universe, size).toVenue();

			universe.venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueNext
			);
		};

		var visual = new VisualGroup
		(
			new Visual[]
			{
				new VisualImageScaled
				(
					new VisualImageFromLibrary("Opening"), size
				)
				// todo - Sound?
			}
		);

		var controlActionNames = ControlActionNames.Instances();

		var returnValue = new ControlContainer
		(
			"containerOpening",
			this._zeroes, // pos
			this.sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlVisual
				(
					"imageOpening",
					this._zeroes.clone(),
					this.sizeBase.clone(), // size
					DataBinding.fromContext<Visual>(visual),
					null, null // colors
				),

				ControlButton.from8
				(
					"buttonNext",
					Coords.fromXY(75, 120), // pos
					Coords.fromXY(50, fontHeight * 2), // size
					"Next",
					fontHeight * 2,
					false, // hasBorder
					true, // isEnabled
					goToVenueNext // click
				)
			}, // end children

			new Action[]
			{
				new Action( controlActionNames.ControlCancel, goToVenueNext ),
				new Action( controlActionNames.ControlConfirm, goToVenueNext )
			},

			null
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase producer(Universe universe, Coords size)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var goToVenueNext = () ->
		{
			universe.soundHelper.soundsAllStop(universe);

			var venueTitle = this.title(universe, size).toVenue();

			universe.venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueTitle
			);
		};

		var visual = new VisualGroup
		(
			new Visual[]
			{
				new VisualImageScaled
				(
					new VisualImageFromLibrary("Producer"), size
				),
				new VisualSound("Music_Producer", false) // repeat
			}
		);

		var controlActionNames = ControlActionNames.Instances();

		var returnValue = new ControlContainer
		(
			"containerProducer",
			this._zeroes, // pos
			this.sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlVisual
				(
					"imageProducer",
					this._zeroes.clone(),
					this.sizeBase.clone(), // size
					DataBinding.fromContext<Visual>(visual),
					null, null // colors
				),

				ControlButton.from8
				(
					"buttonNext",
					Coords.fromXY(75, 120), // pos
					Coords.fromXY(50, fontHeight * 2), // size
					"Next",
					fontHeight * 2,
					false, // hasBorder
					true, // isEnabled
					goToVenueNext // click
				)
			}, // end children

			new Action[]
			{
				new Action( controlActionNames.ControlCancel, goToVenueNext ),
				new Action( controlActionNames.ControlConfirm, goToVenueNext )
			},

			null
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase settings
	(
		Universe universe, Coords size, Venue venuePrev
	)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var buttonHeight = this.buttonHeightBase;
		var margin = 15;
		var padding = 5;
		var labelPadding = 3;

		var rowHeight = buttonHeight + padding;
		var row0PosY = margin;
		var row1PosY = row0PosY + rowHeight / 2;
		var row2PosY = row1PosY + rowHeight;
		var row3PosY = row2PosY + rowHeight;
		var row4PosY = row3PosY + rowHeight;

		var back = () ->
		{
			var venueNext = venuePrev;
			venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueNext
			);
			universe.venueNext = venueNext;
		};

		var returnValue = new ControlContainer
		(
			"containerSettings",
			this._zeroes, // pos
			this.sizeBase.clone(),
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelMusicVolume",
					Coords.fromXY(30, row1PosY + labelPadding), // pos
					Coords.fromXY(75, buttonHeight), // size
					false, // isTextCentered
					"Music:",
					fontHeight
				),

				new ControlSelect
				(
					"selectMusicVolume",
					Coords.fromXY(70, row1PosY), // pos
					Coords.fromXY(30, buttonHeight), // size
					new DataBinding
					(
						universe.soundHelper,
						(SoundHelper c) -> c.musicVolume,
						(SoundHelper c, double v) -> c.musicVolume = v
					), // valueSelected
					SoundHelper.controlSelectOptionsVolume(), // options
					DataBinding.fromGet((Object c) -> c.value), // bindingForOptionValues,
					DataBinding.fromGet((Object c) -> { return c.text; }), // bindingForOptionText
					fontHeight
				),

				new ControlLabel
				(
					"labelSoundVolume",
					Coords.fromXY(105, row1PosY + labelPadding), // pos
					Coords.fromXY(75, buttonHeight), // size
					false, // isTextCentered
					"Sound:",
					fontHeight
				),

				new ControlSelect
				(
					"selectSoundVolume",
					Coords.fromXY(140, row1PosY), // pos
					Coords.fromXY(30, buttonHeight), // size
					new DataBinding
					(
						universe.soundHelper,
						(SoundHelper c) -> c.soundVolume,
						(SoundHelper c, double v) -> { c.soundVolume = v; }
					), // valueSelected
					SoundHelper.controlSelectOptionsVolume(), // options
					DataBinding.fromGet( (ControlSelectOption c) -> c.value ), // bindingForOptionValues,
					DataBinding.fromGet( (ControlSelectOption c) -> c.text ), // bindingForOptionText
					fontHeight
				),

				new ControlLabel
				(
					"labelDisplaySize",
					Coords.fromXY(30, row2PosY + labelPadding), // pos
					Coords.fromXY(75, buttonHeight), // size
					false, // isTextCentered
					"Display:",
					fontHeight
				),

				new ControlSelect
				(
					"selectDisplaySize",
					Coords.fromXY(70, row2PosY), // pos
					Coords.fromXY(65, buttonHeight), // size
					universe.display.sizeInPixels, // valueSelected
					// options
					universe.display.sizesAvailable,
					DataBinding.fromGet( (Coords c) -> c ), // bindingForOptionValues,
					DataBinding.fromGet( (Coords c) -> c.toStringXY() ), // bindingForOptionText
					fontHeight
				),

				ControlButton.from8
				(
					"buttonDisplaySizeChange",
					Coords.fromXY(140, row2PosY), // pos
					Coords.fromXY(30, buttonHeight), // size
					"Change",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var venueControls =
							(VenueControls)(universe.venueCurrent);
						var controlRootAsContainer =
							(ControlContainer)(venueControls.controlRoot);
						var selectDisplaySize =
							(ControlSelect)(controlRootAsContainer.childrenByName.get("selectDisplaySize"));
						var displaySizeSpecified =
							selectDisplaySize.optionSelected();

						var displayAsDisplay = universe.display;
						var display = (Display2D)displayAsDisplay;
						var platformHelper = universe.platformHelper;
						platformHelper.platformableRemove(display);
						display.sizeInPixels = displaySizeSpecified;
						display.canvas = null; // hack
						display.initialize(universe);
						platformHelper.initialize(universe);

						var venueNext = universe.controlBuilder.settings
						(
							universe, null, universe.venueCurrent
						).toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonInputs",
					Coords.fromXY(70, row3PosY), // pos
					Coords.fromXY(65, buttonHeight), // size
					"Inputs",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var venueCurrent = universe.venueCurrent;
						var controlGameControls =
							universe.controlBuilder.inputs(universe, size, venueCurrent);
						Venue venueNext = controlGameControls.toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo(venueCurrent, venueNext);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonDone",
					Coords.fromXY(70, row4PosY), // pos
					Coords.fromXY(65, buttonHeight), // size
					"Done",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					back // click
				),
			},

			new Action[] { new Action("Back", back) },

			new ActionToInputsMapping[]
			{
				new ActionToInputsMapping
				(
					"Back", new String[] { "Escape" }, true
				)
			}

		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase slideshow
	(
		Universe universe,
		Coords size,
		String imageNamesAndMessagesForSlides[][],
		Venue venueAfterSlideshow
	)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var controlsForSlides = new ArrayList<Object>();

		var nextDefn = (double slideIndexNext) -> // click
		{
			var venueNext;
			if (slideIndexNext < controlsForSlides.length)
			{
				var controlForSlideNext = controlsForSlides[slideIndexNext];
				venueNext = controlForSlideNext.toVenue();
			}
			else
			{
				venueNext = venueAfterSlideshow;
			}
			venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueNext
			);
			universe.venueNext = venueNext;
		};

		var skip = () ->
		{
			universe.venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueAfterSlideshow
			);
		};

		for (var i = 0; i < imageNamesAndMessagesForSlides.length; i++)
		{
			var imageNameAndMessage = imageNamesAndMessagesForSlides[i];
			var imageName = imageNameAndMessage[0];
			var message = imageNameAndMessage[1];

			var next = nextDefn.bind(this, i + 1);

			var containerSlide = new ControlContainer
			(
				"containerSlide_" + i,
				this._zeroes, // pos
				this.sizeBase.clone(), // size
				// children
				new ControlBase[]
				{
					new ControlVisual
					(
						"imageSlide",
						this._zeroes,
						this.sizeBase.clone(), // size
						DataBinding.fromContext<Visual>
						(
							new VisualImageScaled
							(
								new VisualImageFromLibrary(imageName),
								this.sizeBase.clone().multiply(scaleMultiplier) // sizeToDrawScaled
							)
						),
						null, null // colorBackground, colorBorder
					),

					new ControlLabel
					(
						"labelSlideText",
						Coords.fromXY(100, this.fontHeightInPixelsBase * 2), // pos
						this.sizeBase.clone(), // size
						true, // isTextCentered,
						message,
						this.fontHeightInPixelsBase
					),

					ControlButton.from8
					(
						"buttonNext",
						Coords.fromXY(75, 120), // pos
						Coords.fromXY(50, 40), // size
						"Next",
						this.fontHeightInPixelsBase,
						false, // hasBorder
						true, // isEnabled
						next
					)
				},

				new Action[]
				{
					new Action( ControlActionNames.Instances().ControlCancel, skip ),
					new Action( ControlActionNames.Instances().ControlConfirm, next )
				},

				null // ?

			);

			containerSlide.scalePosAndSize(scaleMultiplier);

			controlsForSlides.add(containerSlide);
		}

		return controlsForSlides[0];
	}

	ControlBase title(Universe universe, Coords size)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var start = () ->
		{
			var venueMessage = VenueMessage.fromText("Loading profiles...");

			var venueTask = new VenueTask
			(
				venueMessage,
				() ->
					Profile.toControlProfileSelect(universe, null, universe.venueCurrent),
				(Universe universe, Object result) -> // done
				{
					var venueProfileSelect = result.toVenue();

					universe.venueNext = controlBuilder.venueTransitionalFromTo
					(
						universe.venueCurrent, venueProfileSelect
					);
				}
			);

			universe.venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueTask
			);
		};

		var visual = new VisualGroup
		(
			new Visual[]
			{
				new VisualImageScaled
				(
					new VisualImageFromLibrary("Title"), size
				),
				new VisualSound("Music_Title", true) // isMusic
			}
		);

		var returnValue = new ControlContainer
		(
			"containerTitle",
			this._zeroes, // pos
			this.sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				ControlVisual.from4
				(
					"imageTitle",
					this._zeroes.clone(),
					this.sizeBase.clone(), // size
					DataBinding.fromContext<Visual>(visual)
				),

				ControlButton.from8
				(
					"buttonStart",
					Coords.fromXY(75, 120), // pos
					Coords.fromXY(50, fontHeight * 2), // size
					"Start",
					fontHeight * 2,
					false, // hasBorder
					true, // isEnabled
					start // click
				)
			}, // end children

			new Action[]
			{
				new Action( ControlActionNames.Instances().ControlCancel, start ),
				new Action( ControlActionNames.Instances().ControlConfirm, start )
			},

			null // mappings
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase worldDetail
	(
		Universe universe, Coords size, Venue venuePrev
	)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var world = universe.world;

		var dateCreated = world.dateCreated;
		var dateSaved = world.dateSaved;

		var returnValue = ControlContainer.from4
		(
			"containerWorldDetail",
			this._zeroes, // pos
			this.sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelProfileName",
					Coords.fromXY(100, 40), // pos
					Coords.fromXY(100, 20), // size
					true, // isTextCentered
					"Profile: " + universe.profile.name,
					fontHeight
				),
				new ControlLabel
				(
					"labelWorldName",
					Coords.fromXY(100, 55), // pos
					Coords.fromXY(150, 25), // size
					true, // isTextCentered
					"World: " + world.name,
					fontHeight
				),
				new ControlLabel
				(
					"labelStartDate",
					Coords.fromXY(100, 70), // pos
					Coords.fromXY(150, 25), // size
					true, // isTextCentered
					"Started:" + dateCreated.toStringTimestamp(),
					fontHeight
				),
				new ControlLabel
				(
					"labelSavedDate",
					Coords.fromXY(100, 85), // pos
					Coords.fromXY(150, 25), // size
					true, // isTextCentered
					"Saved:" +
						(
							dateSaved == null
							? "[never]"
							: dateSaved.toStringTimestamp()
						),
					fontHeight
				),

				ControlButton.from8
				(
					"buttonStart",
					Coords.fromXY(50, 100), // pos
					Coords.fromXY(100, this.buttonHeightBase), // size
					"Start",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var world = universe.world;
						var venueWorld = world.toVenue();
						var venueNext;
						if (world.dateSaved != null)
						{
							venueNext = venueWorld;
						}
						else
						{
							var textInstructions =
								universe.mediaLibrary.textStringGetByName("Instructions");
							var instructions = textInstructions.value;
							var controlInstructions = controlBuilder.message
							(
								universe,
								size,
								DataBinding.fromContext(instructions),
								() -> // acknowledge
								{
									universe.venueNext = controlBuilder.venueTransitionalFromTo
									(
										universe.venueCurrent, venueWorld
									);
								},
								false
							);

							var venueInstructions =
								controlInstructions.toVenue();

							var venueMovie = new VenueVideo
							(
								"Movie", // videoName
								venueInstructions // fader implicit
							);

							venueNext = venueMovie;
						}

						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonBack",
					Coords.fromXY(10, 10), // pos
					Coords.fromXY(15, 15), // size
					"<",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var venueNext = venuePrev;
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonDelete",
					Coords.fromXY(180, 10), // pos
					Coords.fromXY(15, 15), // size
					"x",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var saveState = universe.profile.saveStateSelected();
						var saveStateName = saveState.name;

						var controlConfirm = universe.controlBuilder.confirmAndReturnToVenue
						(
							universe,
							size,
							"Delete save \""
								+ saveStateName
								+ "\"?",
							universe.venueCurrent,
							() -> // confirm
							{
								var storageHelper = universe.storageHelper;

								var profile = universe.profile;

								var saveStates = profile.saveStates;
								ArrayHelper.remove(saveStates, saveState);
								storageHelper.save(profile.name, profile);

								universe.world = null;
								storageHelper.delete(saveStateName);
							},
							null // cancel
						);

						Venue venueNext = controlConfirm.toVenue();
						venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueNext
						);

						universe.venueNext = venueNext;
					}
				),
			} // end children
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public ControlBase worldLoad(Universe universe, Coords size)
	{
		var controlBuilder = this;

		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var scaleMultiplier =
			this._scaleMultiplier.overwriteWith(size).divide(this.sizeBase);

		var fontHeight = this.fontHeightInPixelsBase;

		var confirm = () ->
		{
			var storageHelper = universe.storageHelper;

			var messageAsDataBinding = DataBinding.fromContextAndGet
			(
				null, // Will be set below.
				(VenueTask c) -> "Loading game..."
			);

			var venueMessage = VenueMessage.fromMessage
			(
				messageAsDataBinding
			);

			var venueTask = new VenueTask
			(
				venueMessage,
				() -> // perform
				{
					var profile = universe.profile;
					var saveStateSelected = profile.saveStateSelected;
					return storageHelper.load(saveStateSelected.name);
				},
				(Universe universe, SaveState saveStateReloaded) -> // done
				{
					universe.world = saveStateReloaded.world;
					Venue venueNext = universe.controlBuilder.worldLoad
					(
						universe, null
					).toVenue();
					venueNext = controlBuilder.venueTransitionalFromTo
					(
						universe.venueCurrent, venueNext
					);
					universe.venueNext = venueNext;
				}
			);

			messageAsDataBinding.contextSet(venueTask);

			universe.venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueTask
			);
		};

		var cancel = () ->
		{
			Venue venueNext = controlBuilder.worldLoad(universe, null).toVenue();
			venueNext = controlBuilder.venueTransitionalFromTo
			(
				universe.venueCurrent, venueNext
			);
			universe.venueNext = venueNext;
		};

		var returnValue = ControlContainer.from4
		(
			"containerWorldLoad",
			this._zeroes, // pos
			this.sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelProfileName",
					Coords.fromXY(100, 25), // pos
					Coords.fromXY(120, 25), // size
					true, // isTextCentered
					"Profile: " + universe.profile.name,
					fontHeight
				),

				new ControlLabel
				(
					"labelSelectASave",
					Coords.fromXY(100, 40), // pos
					Coords.fromXY(100, 25), // size
					true, // isTextCentered
					"Select a Save:",
					fontHeight
				),

				ControlList.from8
				(
					"listSaveStates",
					Coords.fromXY(30, 50), // pos
					Coords.fromXY(140, 50), // size
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) -> c.saveStates
					), // items
					DataBinding.fromGet( (SaveState c) -> c.name ), // bindingForOptionText
					fontHeight,
					new DataBinding
					(
						universe.profile,
						(Profile c) -> c.saveStateSelected(),
						(Profile c, SaveState v) -> { c.saveStateNameSelected = v.name; }
					), // bindingForOptionSelected
					DataBinding.fromGet( (String c) -> c ) // value
				),

				ControlButton.from8
				(
					"buttonLoadFromServer",
					Coords.fromXY(30, 105), // pos
					Coords.fromXY(40, this.buttonHeightBase), // size
					"Load",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					() -> // click
					{
						var controlConfirm = universe.controlBuilder.confirm
						(
							universe, size, "Abandon the current game?",
							confirm, cancel
						);
						var venueConfirm = controlConfirm.toVenue();
						universe.venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueConfirm
						);
					}
				),

				ControlButton.from8
				(
					"buttonLoadFromFile",
					Coords.fromXY(80, 105), // pos
					Coords.fromXY(40, this.buttonHeightBase), // size
					"Load File",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var venueFileUpload = new VenueFileUpload(null, null);

						var controlMessageReadyToLoad =
							universe.controlBuilder.message
							(
								universe,
								size,
								DataBinding.fromContext("Ready to load from file..."),
								() -> // acknowledge
								{
									(String fileContentsAsString) -> // callback
									{
										var worldAsStringCompressed = fileContentsAsString;
										var compressor = universe.storageHelper.compressor;
										var worldSerialized = compressor.decompressString(worldAsStringCompressed);
										var worldDeserialized = universe.serializer.deserialize(worldSerialized);
										universe.world = worldDeserialized;

										Venue venueNext = controlBuilder.game
										(
											universe, size, universe.venueCurrent
										).toVenue();
										venueNext = controlBuilder.venueTransitionalFromTo
										(
											universe.venueCurrent, venueNext
										);
										universe.venueNext = venueNext;
									}

									/*
									var inputFile =
										venueFileUpload.toDomElement().getElementsByTagName("input")[0];
									var fileToLoad = inputFile.files[0];
									new FileHelper().loadFileAsBinaryString
									(
										fileToLoad,
										callback,
										null // contextForCallback
									);
									*/
								},
								null
							);

						var venueMessageReadyToLoad =
							controlMessageReadyToLoad.toVenue();

						var controlMessageCancelled = universe.controlBuilder.message
						(
							universe,
							size,
							DataBinding.fromContext("No file specified."),
							() -> // acknowlege
							{
								Venue venueNext = controlBuilder.game
								(
									universe, size, universe.venueCurrent
								).toVenue();
								venueNext = controlBuilder.venueTransitionalFromTo
								(
									universe.venueCurrent, venueNext
								);
								universe.venueNext = venueNext;
							},
							false //?
						);

						var venueMessageCancelled = controlMessageCancelled.toVenue();

						venueFileUpload.venueNextIfFileSpecified = venueMessageReadyToLoad;
						venueFileUpload.venueNextIfCancelled = venueMessageCancelled;

						universe.venueNext = venueFileUpload;
					}
				),

				ControlButton.from8
				(
					"buttonReturn",
					Coords.fromXY(130, 105), // pos
					Coords.fromXY(40, this.buttonHeightBase), // size
					"Return",
					fontHeight,
					true, // hasBorder
					true, // isEnabled
					() -> // click
					{
						var venueGame = controlBuilder.game
						(
							universe, size, universe.venueCurrent
						).toVenue();
						universe.venueNext = controlBuilder.venueTransitionalFromTo
						(
							universe.venueCurrent, venueGame
						);
					}
				),
			}
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}
}