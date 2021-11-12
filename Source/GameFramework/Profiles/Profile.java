
package GameFramework.Profiles;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Display.Visuals.*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*;
import GameFramework.Media.*;
import GameFramework.Model.*;
import GameFramework.Storage.*;
import GameFramework.Utility.*;

public class Profile
{
	public String name;
	public List<SaveState> saveStates;
	public String saveStateNameSelected;

	public Profile(String name, List<SaveState> saveStates)
	{
		this.name = name;
		this.saveStates = (saveStates != null ? saveStates: new ArrayList<SaveState>());
		this.saveStateNameSelected = null;
	}

	public static Profile anonymous()
	{
		var now = DateTime.now();
		var nowAsString = now.toStringMMDD_HHMM_SS();
		var profileName = "Anon-" + nowAsString;
		var profile = new Profile(profileName, new ArrayList<SaveState>());
		return profile;
	}

	public SaveState saveStateSelected()
	{
		return this.saveStates.stream().filter
		(
			x -> x.name == this.saveStateNameSelected
		).collect(Collectors.toList()).get(0);
	}

	// controls

	public static ControlBase toControlSaveStateLoad
	(
		Universe universe, Coords size, Venue venuePrev
	)
	{
		var isLoadNotSave = true;
		return Profile.toControlSaveStateLoadOrSave
		(
			universe, size, venuePrev, isLoadNotSave
		);
	}

	public static ControlBase toControlSaveStateSave
	(
		Universe universe, Coords size, Venue venuePrev
	)
	{
		var isLoadNotSave = false;
		return Profile.toControlSaveStateLoadOrSave
		(
			universe, size, venuePrev, isLoadNotSave
		);
	}

	public static ControlBase toControlSaveStateLoadOrSave
	(
		Universe universe, Coords size, Venue venuePrev, boolean isLoadNotSave
	)
	{
		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var controlBuilder = universe.controlBuilder;
		var sizeBase = controlBuilder.sizeBase;
		var scaleMultiplier = size.clone().divide(sizeBase);
		var fontHeight = controlBuilder.fontHeightInPixelsBase;
		var buttonHeightBase = controlBuilder.buttonHeightBase;

		var visualThumbnailSize = Coords.fromXY(60, 45);

		var venueToReturnTo = universe.venueCurrent;

		Runnable loadNewWorld = () ->
		{
			var world = universe.worldCreate();
			universe.world = world;
			Venue venueNext = controlBuilder.worldDetail
			(
				universe, size, universe.venueCurrent
			).toVenue();
			venueNext = VenueFader.fromVenuesToAndFrom
			(
				venueNext, universe.venueCurrent
			);
			universe.venueNext = venueNext;
		};

		Runnable loadSelectedSlotFromLocalStorage = () ->
		{
			var saveStateNameSelected = universe.profile.saveStateNameSelected;
			if (saveStateNameSelected != null)
			{
				VenueTask messageAsDataBindingContext = null;
				var messageAsDataBinding = DataBinding.fromContextAndGet
				(
					messageAsDataBindingContext, // Will be set below.
					(VenueTask c) -> "Loading game..."
				);

				var messageAsDataBinding2 = DataBinding.fromContextAndGet
				(
					null, (Object c) -> "Loading game..."
				);

				var venueMessage = VenueMessage.fromMessage
				(
					messageAsDataBinding2
				);

				var venueTask = new VenueTask<SaveState>
				(
					venueMessage,
					(Universe universe2) -> // perform
						universe2.storageHelper.load<SaveState>
						(
							saveStateNameSelected
						),
					(Universe universe2, SaveState saveStateSelected) -> // done
					{
						var worldSelected = saveStateSelected.world;
						universe.world = worldSelected;
						Venue venueNext = worldSelected.toVenue();
						venueNext = VenueFader.fromVenuesToAndFrom
						(
							venueNext, universe2.venueCurrent
						);
						universe2.venueNext = venueNext;
					}
				);

				messageAsDataBinding.contextSet(venueTask);

				universe.venueNext = VenueFader.fromVenuesToAndFrom
				(
					venueTask, universe.venueCurrent
				);
			}
		};

		Function<SaveState,Boolean> saveToLocalStorage = (SaveState saveState) ->
		{
			var profile = universe.profile;
			var world = universe.world;
			var now = DateTime.now();
			world.dateSaved = now;

			var nowAsString = now.toStringYYYYMMDD_HHMM_SS();
			var place = world.placeCurrent;
			var placeName = place.name();
			var timePlayingAsString = world.timePlayingAsStringShort(universe);

			var displaySize = universe.display.sizeInPixels();
			var displayFull = Display2D.fromSizeAndIsInvisible(displaySize, true);
			displayFull.initialize(universe);
			place.draw(universe, world, displayFull);
			var imageSnapshotFull = displayFull.toImage();

			var imageSizeThumbnail = visualThumbnailSize.clone();
			var displayThumbnail = Display2D.fromSizeAndIsInvisible
			(
				imageSizeThumbnail, true
			);
			displayThumbnail.initialize(universe);
			displayThumbnail.drawImageScaled
			(
				imageSnapshotFull, Coords.Instances().Zeroes, imageSizeThumbnail
			);
			var imageThumbnailFromDisplay = displayThumbnail.toImage();
			/*
			var imageThumbnailAsDataUrl =
				imageThumbnailFromDisplay.systemImage.toDataURL();
			var imageThumbnail = new Image2("Snapshot", imageThumbnailAsDataUrl).unload();
			*/
			Image2 imageThumbnail = null; // todo

			var saveStateName = "Save-" + nowAsString;
			var saveState2 = new SaveState
			(
				saveStateName,
				placeName,
				timePlayingAsString,
				now,
				imageThumbnail,
				world
			);

			var storageHelper = universe.storageHelper;

			var wasSaveSuccessful = false;
			try
			{
				storageHelper.save(saveStateName, saveState2);
				if (profile.saveStates.stream().anyMatch(x -> x.name == saveStateName) == false)
				{
					saveState.unload();
					profile.saveStates.add(saveState2);
					storageHelper.save(profile.name, profile);
				}
				var profileNames = (List<String>)storageHelper.load("ProfileNames");
				if (Arrays.asList(profileNames).indexOf(profile.name) == -1)
				{
					profileNames.add(profile.name);
					storageHelper.save("ProfileNames", profileNames);
				}

				wasSaveSuccessful = true;
			}
			catch (Exception ex)
			{
				wasSaveSuccessful = false;
			}

			return wasSaveSuccessful;
		};

		Consumer<Boolean> saveToLocalStorageDone = (Boolean wasSaveSuccessful) ->
		{
			var message =
			(
				wasSaveSuccessful
				? "Game saved successfully."
				: "Save failed due to errors."
			);

			var controlMessage = universe.controlBuilder.message
			(
				universe,
				size,
				DataBinding.fromContext(message),
				(UniverseWorldPlaceEntities uwpe) -> // acknowledge
				{
					Venue venueNext = universe.controlBuilder.game
					(
						universe, null, universe.venueCurrent
					).toVenue();
					venueNext = VenueFader.fromVenuesToAndFrom
					(
						venueNext, universe.venueCurrent
					);
					universe.venueNext = venueNext;
				},
				false
			);

			Venue venueNext = controlMessage.toVenue();
			venueNext = VenueFader.fromVenuesToAndFrom
			(
				venueNext, universe.venueCurrent
			);
			universe.venueNext = venueNext;
		};

		Runnable saveToLocalStorageAsNewSlot = () ->
		{
			var messageAsDataBinding = DataBinding.fromContextAndGet
			(
				null, // context - Set below.
				(Object c) -> "Saving game..."
			);

			var venueMessage = VenueMessage.fromMessage
			(
				messageAsDataBinding
			);

			var venueTask = new VenueTask<Boolean>
			(
				venueMessage,
				(UniverseWorldPlaceEntities uwpe2) ->
					saveToLocalStorage.apply((SaveState)null), // perform
				(UniverseWorldPlaceEntities uwpe2, Boolean result) -> // done
				{
					saveToLocalStorageDone.accept(result);
				}
			);
			messageAsDataBinding.contextSet(venueTask);

			universe.venueNext = VenueFader.fromVenuesToAndFrom(venueTask, universe.venueCurrent);
		};

		Runnable saveToFilesystem = () ->
		{
			var venueMessage = VenueMessage.fromText("Saving game...");

			var venueTask = new VenueTask<Integer[]>
			(
				venueMessage,
				() -> // perform
				{
					var world = universe.world;

					world.dateSaved = DateTime.now();
					var worldSerialized = universe.serializer.serialize(world, false);

					var compressor = universe.storageHelper.compressor;
					var worldCompressedAsBytes =
						compressor.compressStringToBytes(worldSerialized);

					return worldCompressedAsBytes;
				},
				(Universe universe2, int[] worldCompressedAsBytes) -> // done
				{
					var wasSaveSuccessful = (worldCompressedAsBytes != null);
					var message =
					(
						wasSaveSuccessful ? "Save choose ready location on dialog." : "Save failed due to errors."
					);

					new FileHelper().saveBytesToFileWithName
					(
						worldCompressedAsBytes, universe2.world.name + ".json.lzw"
					);

					var controlMessage = universe2.controlBuilder.message
					(
						universe2,
						size,
						DataBinding.fromContext(message),
						(UniverseWorldPlaceEntities uwpe) -> // acknowledge
						{
							Venue venueNext = universe2.controlBuilder.game
							(
								universe, null, universe2.venueCurrent
							).toVenue();
							venueNext = VenueFader.fromVenuesToAndFrom
							(
								venueNext, universe2.venueCurrent
							);
							universe2.venueNext = venueNext;
						},
						false // ?
					);

					var venueMessage2 = controlMessage.toVenue();
					universe2.venueNext = VenueFader.fromVenuesToAndFrom
					(
						venueMessage2, universe2.venueCurrent
					);
				}
			);

			universe.venueNext = VenueFader.fromVenuesToAndFrom
			(
				venueTask, universe.venueCurrent
			);
		};

		Runnable loadFromFile = () -> // click
		{
			var venueFileUpload = new VenueFileUpload(null, null);

			var controlMessageReadyToLoad = controlBuilder.message
			(
				universe,
				size,
				DataBinding.fromContext("Ready to load from file..."),
				(UniverseWorldPlaceEntities uwpe) -> // acknowledge
				{
					Consumer<String> callback = (String fileContentsAsString) ->
					{
						var worldAsStringCompressed = fileContentsAsString;
						var compressor = universe.storageHelper.compressor;
						var worldSerialized = compressor.decompressString
						(
							worldAsStringCompressed
						);
						var worldDeserialized = (World)
						(
							universe.serializer.deserialize
							(
								worldSerialized
							)
						);
						universe.world = worldDeserialized;

						Venue venueNext = universe.controlBuilder.game
						(
							universe, size, universe.venueCurrent
						).toVenue();
						venueNext = VenueFader.fromVenuesToAndFrom
						(
							venueNext, universe.venueCurrent
						);
						universe.venueNext = venueNext;
					};

					throw new Exception("Not yet implemented!");
					/*
					var inputFile = venueFileUpload.toDomElement().getElementsByTagName("input")[0];
					var fileToLoad = inputFile.files[0];
					new FileHelper().loadFileAsBinaryString
					(
						fileToLoad,
						callback,
						null // contextForCallback
					);
					*/
				},
				false // ?
			);

			var venueMessageReadyToLoad = controlMessageReadyToLoad.toVenue();

			var controlMessageCancelled = controlBuilder.message
			(
				universe,
				size,
				DataBinding.fromContext("No file specified."),
				(UniverseWorldPlaceEntities uwpe) -> // acknowlege
				{
					Venue venueNext = controlBuilder.game
					(
						universe, size, universe.venueCurrent
					).toVenue();
					venueNext = VenueFader.fromVenuesToAndFrom
					(
						venueNext, universe.venueCurrent
					);
					universe.venueNext = venueNext;
				},
				false //?
			);

			var venueMessageCancelled = controlMessageCancelled.toVenue();

			venueFileUpload.venueNextIfFileSpecified = venueMessageReadyToLoad;
			venueFileUpload.venueNextIfCancelled = venueMessageCancelled;

			universe.venueNext = venueFileUpload;
		};

		Runnable back = () ->
		{
			var venueNext = venueToReturnTo;
			venueNext = VenueFader.fromVenuesToAndFrom(venueNext, universe.venueCurrent);
			universe.venueNext = venueNext;
		};

		Runnable deleteSaveSelectedConfirm = () ->
		{
			var saveStateSelected = universe.profile.saveStateSelected();

			var storageHelper = universe.storageHelper;
			storageHelper.delete(saveStateSelected.name);
			var profile = universe.profile;
			ArrayHelper.remove(profile.saveStates, saveStateSelected);
			storageHelper.save(profile.name, profile);
		};

		Runnable deleteSaveSelected = () ->
		{
			var saveStateSelected = universe.profile.saveStateSelected();

			if (saveStateSelected == null)
			{
				return;
			}

			var controlConfirm = universe.controlBuilder.confirmAndReturnToVenue
			(
				universe,
				size,
				"Delete save state \""
					+ saveStateSelected.timeSaved.toStringYYYY_MM_DD_HH_MM_SS()
					+ "\"?",
				universe.venueCurrent,
				(UniverseWorldPlaceEntities uwpe) -> deleteSaveSelectedConfirm.run(),
				null // cancel
			);

			Venue venueNext = controlConfirm.toVenue();
			venueNext = VenueFader.fromVenuesToAndFrom
			(
				venueNext, universe.venueCurrent
			);
			universe.venueNext = venueNext;
		};

		Runnable saveToLocalStorageOverwritingSlotSelected = () ->
		{
			deleteSaveSelectedConfirm.run();
			saveToLocalStorageAsNewSlot.run();
		};

		var returnValue = new ControlContainer
		(
			"containerSaveStates",
			Coords.create(), // pos
			sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelProfileName",
					Coords.fromXY(100, 10), // pos
					Coords.fromXY(120, fontHeight), // size
					true, // isTextCentered
					DataBinding.fromContext("Profile: " + universe.profile.name),
					fontHeight
				),

				new ControlLabel
				(
					"labelChooseASave",
					Coords.fromXY(100, 20), // pos
					Coords.fromXY(150, 25), // size
					true, // isTextCentered
					DataBinding.fromContext
					(
						"Choose a State to "
						+ (isLoadNotSave ? "Restore" : "Overwrite")
						+ ":"
					),
					fontHeight
				),

				ControlList.from10
				(
					"listSaveStates",
					Coords.fromXY(10, 35), // pos
					Coords.fromXY(110, 75), // size
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) ->
							c.saveStates.toArray(new SaveState[] {})
					), // items
					DataBinding.fromGet
					(
						(SaveState c) ->
						{
							var timeSaved = c.timeSaved;
							return
							(
								timeSaved == null
								? "-"
								: timeSaved.toStringYYYY_MM_DD_HH_MM_SS()
							);
						}
					), // bindingForOptionText
					fontHeight,
					new DataBinding<Profile,SaveState>
					(
						universe.profile,
						(Profile c) -> c.saveStateSelected(),
						(Profile c, SaveState v) -> c.saveStateNameSelected = v.name
					), // bindingForOptionSelected
					DataBinding.fromGet( (String c) -> c ), // value
					null,
					(
						isLoadNotSave
						? (Universe u) -> saveToLocalStorageOverwritingSlotSelected.run()
						: (Universe u) -> loadSelectedSlotFromLocalStorage.run()
					) // confirm
				),

				ControlButton.from8
				(
					"buttonNew",
					Coords.fromXY(10, 120), // pos
					Coords.fromXY(25, buttonHeightBase), // size
					"New",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isTextCentered
					(isLoadNotSave ? loadNewWorld : saveToLocalStorageAsNewSlot) // click
				),

				new ControlButton
				(
					"buttonSelect",
					Coords.fromXY(40, 120), // pos
					Coords.fromXY(25, buttonHeightBase), // size
					(isLoadNotSave ? "Load" : "Save"),
					fontHeight,
					true, // hasBorder
					// isEnabled
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) -> (new Boolean(c.saveStateNameSelected != null))
					),
					(isLoadNotSave ? loadSelectedSlotFromLocalStorage : saveToLocalStorageOverwritingSlotSelected), // click
					null, false // ?
				),

				ControlButton.from8
				(
					"buttonFile",
					Coords.fromXY(70, 120), // pos
					Coords.fromXY(25, buttonHeightBase), // size
					"File",
					fontHeight,
					true, // hasBorder
					// isEnabled
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) -> (c.saveStateNameSelected != null)
					),
					(isLoadNotSave ? loadFromFile : saveToFilesystem) // click
				),

				ControlButton.from8
				(
					"buttonDelete",
					Coords.fromXY(100, 120), // pos
					Coords.fromXY(20, buttonHeightBase), // size
					"X",
					fontHeight,
					true, // hasBorder
					// isEnabled
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) -> (c.saveStateNameSelected != null)
					),
					deleteSaveSelected // click
				),

				ControlVisual.from5
				(
					"visualSnapshot",
					Coords.fromXY(130, 35),
					visualThumbnailSize,
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) ->
						{
							var saveState = c.saveStateSelected();
							var saveStateImageSnapshot =
							(
								saveState == null
								? null
								: saveState.imageSnapshot.load()
							);
							var returnValue2 =
							(
								(saveStateImageSnapshot == null) || saveStateImageSnapshot.isLoaded() == false
								? new VisualNone()
								: new VisualImageImmediate(saveStateImageSnapshot, true) // isScaled
							);
							return returnValue2;
						}
					),
					Color.byName("White")
				),

				new ControlLabel
				(
					"labelPlaceName",
					Coords.fromXY(130, 80), // pos
					Coords.fromXY(120, buttonHeightBase), // size
					false, // isTextCentered
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) ->
						{
							var saveState = c.saveStateSelected();
							return (saveState == null ? "" : saveState.placeName);
						}
					),
					fontHeight
				),

				new ControlLabel
				(
					"labelTimePlaying",
					Coords.fromXY(130, 90), // pos
					Coords.fromXY(120, buttonHeightBase), // size
					false, // isTextCentered
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) ->
						{
							var saveState = c.saveStateSelected();
							return (saveState == null ? "" : saveState.timePlayingAsString);
						}
					),
					fontHeight
				),

				new ControlLabel
				(
					"labelDateSaved",
					Coords.fromXY(130, 100), // pos
					Coords.fromXY(120, buttonHeightBase), // size
					false, // isTextCentered
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) ->
						{
							var saveState = c.saveStateSelected();
							var returnValue2 =
							(
								saveState == null
								? ""
								:
								(
									saveState.timeSaved == null
									? ""
									: saveState.timeSaved.toStringYYYY_MM_DD()
								)
							);
							return returnValue2;
						}
					),
					fontHeight
				),

				new ControlLabel
				(
					"labelTimeSaved",
					Coords.fromXY(130, 110), // pos
					Coords.fromXY(120, buttonHeightBase), // size
					false, // isTextCentered
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) ->
						{
							var saveState = c.saveStateSelected();
							return (saveState == null ? "" : saveState.timeSaved.toStringHH_MM_SS());
						}
					),
					fontHeight
				),

				ControlButton.from8
				(
					"buttonBack",
					Coords.fromXY
					(
						sizeBase.x - 10 - 25, sizeBase.y - 10 - 15
					), // pos
					Coords.fromXY(25, 15), // size
					"Back",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					back // click
				),
			},
			null, null
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public static ControlBase toControlProfileNew
	(
		Universe universe, Coords size
	)
	{
		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var controlBuilder = universe.controlBuilder;
		var sizeBase = controlBuilder.sizeBase;
		var scaleMultiplier = size.clone().divide(sizeBase);
		var fontHeight = controlBuilder.fontHeightInPixelsBase;
		var buttonHeightBase = controlBuilder.buttonHeightBase;

		var returnValue = ControlContainer.from4
		(
			"containerProfileNew",
			Coords.create(), // pos
			sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelName",
					Coords.fromXY(100, 40), // pos
					Coords.fromXY(100, 20), // size
					true, // isTextCentered
					DataBinding.fromContext("Profile Name:"),
					fontHeight
				),

				new ControlTextBox
				(
					"textBoxName",
					Coords.fromXY(50, 50), // pos
					Coords.fromXY(100, 20), // size
					new DataBinding<Profile,String>
					(
						universe.profile,
						(Profile c) -> c.name,
						(Profile c, String v) -> c.name = v
					), // text
					fontHeight,
					null, // charCountMax
					DataBinding.fromTrue() // isEnabled
				),

				ControlButton.from8
				(
					"buttonCreate",
					Coords.fromXY(50, 80), // pos
					Coords.fromXY(45, buttonHeightBase), // size
					"Create",
					fontHeight,
					true, // hasBorder
					// isEnabled
					DataBinding.fromContextAndGet
					(
						universe.profile,
						(Profile c) -> { return c.name.length() > 0; }
					),
					() -> // click
					{
						var venueControls = ((VenueControls)(universe.venueCurrent));
						var controlRootAsContainer =
							((ControlContainer)(venueControls.controlRoot));
						var textBoxName =
							(ControlTextBox)(controlRootAsContainer.childrenByName.get("textBoxName"));
						var profileName = textBoxName.text();
						if (profileName == "")
						{
							return;
						}

						var storageHelper = universe.storageHelper;

						var profile = new Profile(profileName, null);
						var profileNames =
							(List<String>)storageHelper.load("ProfileNames");
						if (profileNames == null)
						{
							profileNames = new ArrayList<String>();
						}
						profileNames.add(profileName);
						storageHelper.save("ProfileNames", profileNames);
						storageHelper.save(profileName, profile);

						universe.profile = profile;
						Venue venueNext = Profile.toControlSaveStateLoad
						(
							universe, null, universe.venueCurrent
						).toVenue();
						venueNext = VenueFader.fromVenuesToAndFrom
						(
							venueNext, universe.venueCurrent
						);
						universe.venueNext = venueNext;
					}
				),

				ControlButton.from8
				(
					"buttonCancel",
					Coords.fromXY(105, 80), // pos
					Coords.fromXY(45, buttonHeightBase), // size
					"Cancel",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					() -> // click
					{
						Venue venueNext = Profile.toControlProfileSelect
						(
							universe, null, universe.venueCurrent
						).toVenue();
						venueNext = VenueFader.fromVenuesToAndFrom(venueNext, universe.venueCurrent);
						universe.venueNext = venueNext;
					}
				),
			}
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public static ControlBase toControlProfileSelect
	(
		Universe universe, Coords size, Venue venuePrev
	)
	{
		if (size == null)
		{
			size = universe.display.sizeDefault();
		}

		var controlBuilder = universe.controlBuilder;
		var sizeBase = controlBuilder.sizeBase;
		var scaleMultiplier = size.clone().divide(sizeBase);
		var fontHeight = controlBuilder.fontHeightInPixelsBase;
		var buttonHeightBase = controlBuilder.buttonHeightBase;

		var storageHelper = universe.storageHelper;
		var profileNames = (List<String>)storageHelper.load("ProfileNames");
		if (profileNames == null)
		{
			profileNames = new ArrayList<String>();
			storageHelper.save("ProfileNames", profileNames);
		}
		var profiles = profileNames.stream().map(x -> storageHelper.load(x));

		Runnable create = () ->
		{
			universe.profile = new Profile("", null);
			Venue venueNext = Profile.toControlProfileNew(universe, null).toVenue();
			venueNext = VenueFader.fromVenuesToAndFrom(venueNext, universe.venueCurrent);
			universe.venueNext = venueNext;
		};

		Runnable select = () ->
		{
			var venueControls = (VenueControls)universe.venueCurrent;
			var controlRootAsContainer =
				(ControlContainer)(venueControls.controlRoot);
			var listProfiles =
				(ControlList)(controlRootAsContainer.childrenByName.get("listProfiles"));
			var profileSelected = (Profile)(listProfiles.itemSelected());
			universe.profile = profileSelected;
			if (profileSelected != null)
			{
				Venue venueNext = Profile.toControlSaveStateLoad
				(
					universe, null, universe.venueCurrent
				).toVenue();
				venueNext = VenueFader.fromVenuesToAndFrom
				(
					venueNext, universe.venueCurrent
				);
				universe.venueNext = venueNext;
			}
		};

		Runnable deleteProfileConfirm = () ->
		{
			var profileSelected = universe.profile;

			var storageHelper2 = universe.storageHelper;
			storageHelper2.delete(profileSelected.name);
			var profileNames2 = (List<String>)storageHelper2.load("ProfileNames");
			ArrayHelper.remove(profileNames2, profileSelected.name);
			storageHelper2.save("ProfileNames", profileNames2);
		};

		Runnable deleteProfile = () ->
		{
			var profileSelected = universe.profile;
			if (profileSelected != null)
			{
				var controlConfirm = universe.controlBuilder.confirmAndReturnToVenue
				(
					universe,
					size,
					"Delete profile \""
						+ profileSelected.name
						+ "\"?",
					universe.venueCurrent,
					(UniverseWorldPlaceEntities uwpe) ->
						deleteProfileConfirm.run(),
					null // cancel
				);

				Venue venueNext = controlConfirm.toVenue();
				venueNext = VenueFader.fromVenuesToAndFrom
				(
					venueNext, universe.venueCurrent
				);
				universe.venueNext = venueNext;
			}
		};

		Consumer<Universe> listProfilesConfirm =
			(Universe u2) -> select.run();

		var returnValue = ControlContainer.from4
		(
			"containerProfileSelect",
			Coords.create(), // pos
			sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelSelectAProfile",
					Coords.fromXY(100, 40), // pos
					Coords.fromXY(100, 25), // size
					true, // isTextCentered
					DataBinding.fromContext("Select a Profile:"),
					fontHeight
				),

				new ControlList
				(
					"listProfiles",
					Coords.fromXY(30, 50), // pos
					Coords.fromXY(140, 40), // size
					DataBinding.fromContext(profiles), // items
					DataBinding.fromGet( (Profile c) -> c.name ), // bindingForItemText
					fontHeight,
					new DataBinding<Universe,Profile>
					(
						universe,
						(Universe c) -> c.profile,
						(Universe c, Profile v) -> c.profile = v
					), // bindingForOptionSelected
					DataBinding.fromGet( (Profile c) -> c ), // value
					null, // bindingForIsEnabled
					listProfilesConfirm, // confirm
					null // widthInItems
				),

				ControlButton.from8
				(
					"buttonNew",
					Coords.fromXY(30, 95), // pos
					Coords.fromXY(35, buttonHeightBase), // size
					"New",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					create // click
				),

				ControlButton.from8
				(
					"buttonSelect",
					Coords.fromXY(70, 95), // pos
					Coords.fromXY(35, buttonHeightBase), // size
					"Select",
					fontHeight,
					true, // hasBorder
					// isEnabled
					DataBinding.fromContextAndGet
					(
						universe,
						(Universe c) -> { return (c.profile != null); }
					),
					select // click
				),

				ControlButton.from8
				(
					"buttonSkip",
					Coords.fromXY(110, 95), // pos
					Coords.fromXY(35, buttonHeightBase), // size
					"Skip",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					// click
					() ->
					{
						universe.venueNext = Profile.venueWorldGenerate(universe);
					}
				),

				ControlButton.from8
				(
					"buttonDelete",
					Coords.fromXY(150, 95), // pos
					Coords.fromXY(20, buttonHeightBase), // size
					"X",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					deleteProfile // click
				),

				ControlButton.from8
				(
					"buttonBack",
					Coords.fromXY(sizeBase.x - 10 - 25, sizeBase.y - 10 - 20), // pos
					Coords.fromXY(25, 20), // size
					"Back",
					fontHeight,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					() -> // click
					{
						var venueNext = venuePrev;
						venueNext = VenueFader.fromVenuesToAndFrom(venueNext, universe.venueCurrent);
						universe.venueNext = venueNext;
					}
				),
			}
		);

		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}

	public static Venue venueWorldGenerate(Universe universe)
	{
		var messageAsDataBinding = DataBinding.fromGet
		(
			(VenueTask c) -> "Generating world..."
		);

		var venueMessage =
			VenueMessage.fromMessage(messageAsDataBinding);

		var venueTask = new VenueTask
		(
			venueMessage,
			(Universe universe2) -> universe2.worldCreate(), // perform
			(Universe universe3, World world) -> // done
			{
				universe3.world = world;

				var profile = Profile.anonymous();
				universe.profile = profile;

				Venue venueNext = world.toVenue();
				venueNext = VenueFader.fromVenuesToAndFrom
				(
					venueNext, universe.venueCurrent
				);
				universe.venueNext = venueNext;
			}
		);

		messageAsDataBinding.contextSet(venueTask);

		var returnValue =
			VenueFader.fromVenuesToAndFrom(venueTask, universe.venueCurrent);

		return returnValue;
	}

}
