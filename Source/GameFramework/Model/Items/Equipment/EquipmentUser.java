
package GameFramework.Model.Items.Equipment;

import GameFramework.Controls.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;
import GameFramework.Model.Items.*;

public class EquipmentUser implements EntityProperty
{
	public EquipmentSocketGroup socketGroup;
	public EquipmentSocketDefnGroup socketDefnGroup;

	public Entity itemEntitySelected;
	public EquipmentSocket socketSelected;
	public String statusMessage;

	public EquipmentUser(EquipmentSocketDefnGroup socketDefnGroup)
	{
		this.socketGroup = new EquipmentSocketGroup(socketDefnGroup);
	}

	public void equipAll(UniverseWorldPlaceEntities uwpe)
	{
		var world = uwpe.world;
		var entityEquipmentUser = uwpe.entity;

		var itemHolder = entityEquipmentUser.itemHolder();
		var itemsNotYetEquipped = itemHolder.items;
		var sockets = this.socketGroup.sockets;
		for (var s = 0; s < sockets.length; s++)
		{
			var socket = sockets[s];
			if (socket.itemEntityEquipped == null)
			{
				var socketDefn = socket.defn(this.socketGroup.defnGroup);
				var categoriesEquippableNames = socketDefn.categoriesAllowedNames;
				var itemsEquippable = itemsNotYetEquipped.filter
				(
					(Item x) ->
						ArrayHelper.intersectArrays
						(
							x.defn(world).categoryNames, categoriesEquippableNames
						).length > 0
				);

				if (itemsEquippable.length > 0)
				{
					var itemToEquip = itemsEquippable[0];
					var itemToEquipAsEntity = itemToEquip.toEntity
					(
						uwpe
					);
					uwpe.entity2 = itemToEquipAsEntity;
					this.equipItemEntityInSocketWithName
					(
						uwpe, socket.defnName, true // ?
					);
				}
			}
		}
	}

	public Object equipEntityWithItem(UniverseWorldPlaceEntities uwpe)
	{
		var world = uwpe.world;
		var itemEntityToEquip = uwpe.entity2;

		if (itemEntityToEquip == null)
		{
			return null;
		}
		var sockets = this.socketGroup.sockets;
		var socketDefnGroup = this.socketGroup.defnGroup;
		var itemToEquip = itemEntityToEquip.item();
		var itemDefn = itemToEquip.defn(world);

		var socketFound = sockets.filter
		(
			(EquipmentSocket socket) ->
			{
				var socketDefn = socket.defn(socketDefnGroup);
				var isItemAllowedInSocket = socketDefn.categoriesAllowedNames.some
				(
					(String y) -> itemDefn.categoryNames.indexOf(y) >= 0
				);
				return isItemAllowedInSocket;
			}
		)[0];

		var message = "";
		if (socketFound == null)
		{
			message = "Can't equip " + itemDefn.name + ".";
		}
		else
		{
			var socketFoundName = socketFound.defnName;

			message = this.equipItemEntityInSocketWithName
			(
				uwpe, socketFoundName, false
			);
		}

		return message;
	}

	public void equipItemEntityInFirstOpenQuickSlot
	(
		UniverseWorldPlaceEntities uwpe, boolean includeSocketNameInMessage
	)
	{
		var itemEntityToEquip = uwpe.entity2;
		var itemToEquipDefnName = itemEntityToEquip.item().defnName;
		var socketFound = null;

		var itemQuickSlotCount = 10;
		for (var i = 0; i < itemQuickSlotCount; i++)
		{
			var socketName = "Item" + i;
			var socket = this.socketByName(socketName);
			if (socketFound == null && socket.itemEntityEquipped == null)
			{
				socketFound = socket;
			}
			else if (socket.itemEntityEquipped != null)
			{
				var itemInSocketDefnName =
					socket.itemEntityEquipped.item().defnName;
				if (itemInSocketDefnName == itemToEquipDefnName)
				{
					socketFound = socket;
					break;
				}
			}
		}
		if (socketFound != null)
		{
			this.equipItemEntityInSocketWithName
			(
				uwpe, socketFound.defnName, includeSocketNameInMessage
			);
		}
	}

	public Object equipItemEntityInSocketWithName
	(
		UniverseWorldPlaceEntities uwpe,
		String socketName,
		boolean includeSocketNameInMessage
	)
	{
		var world = uwpe.world;
		var itemEntityToEquip = uwpe.entity2;

		if (itemEntityToEquip == null)
		{
			return "Nothing to equip!";
		}

		var itemToEquip = itemEntityToEquip.item();
		var itemDefn = itemToEquip.defn(world);
		var equippable = itemEntityToEquip.equippable();

		var message = itemDefn.appearance;

		var socket = this.socketByName(socketName);

		if (socket == null)
		{
			message += " cannot be equipped.";
		}
		else if (socket.itemEntityEquipped == itemEntityToEquip)
		{
			if (equippable != null)
			{
				equippable.unequip(uwpe);
			}
			socket.itemEntityEquipped = null;
			message += " unequipped.";
		}
		else
		{
			if (equippable != null)
			{
				equippable.equip(uwpe);
			}
			socket.itemEntityEquipped = itemEntityToEquip;
			message += " equipped";
			if (includeSocketNameInMessage)
			{
				message += " as " + socket.defnName;
			}
			message += ".";
		}

		return message;
	}

	public Entity itemEntityInSocketWithName(String socketName)
	{
		var socket = this.socketByName(socketName);
		return socket.itemEntityEquipped;
	}

	public EquipmentSocket socketByName(String socketName)
	{
		return this.socketGroup.socketsByDefnName.get(socketName);
	}

	public Object unequipItemFromSocketWithName(World world, String socketName)
	{
		var message;
		var socketToUnequipFrom = this.socketGroup.socketsByDefnName.get(socketName);
		if (socketToUnequipFrom == null)
		{
			message = "Nothing to unequip!";
		}
		else
		{
			var itemEntityToUnequip = socketToUnequipFrom.itemEntityEquipped;
			if (itemEntityToUnequip == null)
			{
				message = "Nothing to unequip!";
			}
			else
			{
				socketToUnequipFrom.itemEntityEquipped = null;
				var itemToUnequip = itemEntityToUnequip.item();
				var itemDefn = itemToUnequip.defn(world);
				message = itemDefn.appearance + " unequipped.";
			}
		}
		return message;
	}

	public void unequipItemsNoLongerHeld(UniverseWorldPlaceEntities uwpe)
	{
		var entityEquipmentUser = uwpe.entity;
		var itemHolder = entityEquipmentUser.itemHolder();
		var itemsHeld = itemHolder.items;
		var sockets = this.socketGroup.sockets;
		for (var i = 0; i < sockets.length; i++)
		{
			var socket = sockets[i];
			var socketItemEntity = socket.itemEntityEquipped;
			if (socketItemEntity != null)
			{
				var socketItem = socketItemEntity.item();
				var socketItemDefnName = socketItem.defnName;
				if (itemsHeld.indexOf(socketItem) == -1)
				{
					var itemOfSameTypeStillHeld = itemsHeld.filter
					(
						x -> x.defnName == socketItemDefnName
					)[0];
					if (itemOfSameTypeStillHeld == null)
					{
						socket.itemEntityEquipped = null;
					}
					else
					{
						socket.itemEntityEquipped = itemOfSameTypeStillHeld.toEntity
						(
							uwpe
						);
					}
				}
			}
		}
	}

	public void unequipItem(Item itemToUnequip)
	{
		var socket = this.socketGroup.sockets.filter
		(
			x -> x.itemEntityEquipped.item() == itemToUnequip
		)[0];
		if (socket != null)
		{
			socket.itemEntityEquipped = null;
		}
	}

	public void useItemInSocketNumbered
	(
		UniverseWorldPlaceEntities uwpe, int socketNumber
	)
	{
		var actor = uwpe.entity;
		var equipmentUser = actor.equipmentUser();
		var socketName = "Item" + socketNumber;
		var entityItemEquipped = equipmentUser.itemEntityInSocketWithName(socketName);
		if (entityItemEquipped != null)
		{
			var itemEquipped = entityItemEquipped.item();
			uwpe.entity2 = entityItemEquipped;
			itemEquipped.use(uwpe);
		}
		this.unequipItemsNoLongerHeld(uwpe);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

	// control

	public ControlBase toControl
	(
		Universe universe, Coords size, Entity entityEquipmentUser,
		Venue venuePrev, boolean includeTitleAndDoneButton
	)
	{
		this.statusMessage = "Equip items in available slots.";

		if (size == null)
		{
			size = universe.display.sizeDefault().clone();
		}

		var sizeBase = new Coords(200, 135, 1);

		var fontHeight = 10;
		var fontHeightSmall = fontHeight * .6;
		var fontHeightLarge = fontHeight * 1.5;

		var itemHolder = entityEquipmentUser.itemHolder();
		var equipmentUser = this;
		var sockets = this.socketGroup.sockets;
		var socketDefnGroup = this.socketGroup.defnGroup;

		var itemCategoriesForAllSockets = new ItemCategory[sockets.length];
		for (var i = 0; i < sockets.length; i++)
		{
			var socket = sockets[i];
			var socketDefn = socket.defn(socketDefnGroup);
			var socketCategoryNames = socketDefn.categoriesAllowedNames;
			for (var j = 0; j < socketCategoryNames.length; j++)
			{
				var categoryName = socketCategoryNames[j];
				if (itemCategoriesForAllSockets.indexOf(categoryName) == -1)
				{
					itemCategoriesForAllSockets.push(categoryName);
				}
			}
		}

		var world = universe.world;
		var place = world.placeCurrent;

		var uwpe = new UniverseWorldPlaceEntities
		(
			universe, world, place, entityEquipmentUser, null
		);

		var itemEntities = itemHolder.itemEntities
		(
			uwpe
		);
		var itemEntitiesEquippable = itemEntities.filter
		(
			x -> x.equippable() != null
		);

		var world = universe.world;
		var place = world.placeCurrent;

		var listHeight = 100;

		Runnable equipItemSelectedToSocketDefault = () ->
		{
			var itemEntityToEquip = equipmentUser.itemEntitySelected;
			uwpe.entity2 = itemEntityToEquip;
			var message = equipmentUser.equipEntityWithItem(uwpe);
			equipmentUser.statusMessage = message;
		};

		var listEquippables = new ControlList
		(
			"listEquippables",
			Coords.fromXY(10, 15), // pos
			Coords.fromXY(70, listHeight), // size
			DataBinding.fromContext(itemEntitiesEquippable), // items
			DataBinding.fromGet
			(
				(Entity c) -> c.item().toString(world)
			), // bindingForItemText
			fontHeightSmall,
			new DataBinding
			(
				this,
				(EquipmentUser c) -> c.itemEntitySelected,
				(EquipmentUser c, Entity v) -> c.itemEntitySelected = v
			), // bindingForItemSelected
			DataBinding.fromGet( (Entity c) -> c ), // bindingForItemValue
			null, // bindingForIsEnabled
			equipItemSelectedToSocketDefault,
			null
		);

		Runnable equipItemSelectedToSocketSelected = () ->
		{
			var itemEntityToEquip = equipmentUser.itemEntitySelected;
			uwpe.entity2 = itemEntityToEquip;

			var message;
			var socketSelected = equipmentUser.socketSelected;
			if (socketSelected == null)
			{
				message = equipmentUser.equipEntityWithItem
				(
					uwpe
				);
			}
			else
			{
				message = equipmentUser.equipItemEntityInSocketWithName
				(
					uwpe,
					socketSelected.defnName, true // includeSocketNameInMessage
				);
			}
			equipmentUser.statusMessage = message;
		};

		Consumer<Integer> equipItemSelectedInQuickSlot = (int quickSlotNumber) ->
		{
			uwpe.entity2 = equipmentUser.itemEntitySelected;
			equipmentUser.equipItemEntityInSocketWithName
			(
				uwpe,
				"Item" + quickSlotNumber, // socketName
				true // includeSocketNameInMessage
			);
		};

		var buttonEquip = ControlButton.from8
		(
			"buttonEquip",
			Coords.fromXY(85, 50), // pos
			Coords.fromXY(10, 10), // size
			">", // text
			fontHeight * 0.8,
			true, // hasBorder
			true, // isEnabled - todo
			equipItemSelectedToSocketSelected
		);

		Runnable unequipFromSocketSelected = () ->
		{
			var socketToUnequipFrom = equipmentUser.socketSelected;
			var message = equipmentUser.unequipItemFromSocketWithName
			(
				world, socketToUnequipFrom.defnName
			);
			equipmentUser.statusMessage = message;
		};

		var buttonUnequip = ControlButton.from8
		(
			"buttonEquip",
			Coords.fromXY(85, 65), // pos
			Coords.fromXY(10, 10), // size
			"<", // text
			fontHeight * 0.8,
			true, // hasBorder
			true, // isEnabled - todo
			unequipFromSocketSelected
		);

		var listEquipped = new ControlList
		(
			"listEquipped",
			Coords.fromXY(100, 15), // pos
			Coords.fromXY(90, listHeight), // size
			DataBinding.fromContext(sockets), // items
			DataBinding.fromGet
			(
				(EquipmentSocket c) -> c.toString(world)
			), // bindingForItemText
			fontHeightSmall,
			new DataBinding
			(
				this,
				(EquipmentUser c) -> c.socketSelected,
				(EquipmentUser c, EquipmentSocket v) -> c.socketSelected = v
			), // bindingForItemSelected
			DataBinding.fromGet( (Entity c) -> c ), // bindingForItemValue
			null, // bindingForIsEnabled
			unequipFromSocketSelected, // confirm
			null
		);

		Runnable back = () ->
		{
			var venueNext = venuePrev;
			venueNext = VenueFader.fromVenuesToAndFrom
			(
				venueNext, universe.venueCurrent
			);
			universe.venueNext = venueNext;
		};

		var returnValue = new ControlContainer
		(
			"Equip",
			Coords.create(), // pos
			sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelEquippable",
					Coords.fromXY(10, 5), // pos
					Coords.fromXY(70, 25), // size
					false, // isTextCentered
					"Equippable:",
					fontHeightSmall
				),

				listEquippables,

				buttonEquip,

				buttonUnequip,

				new ControlLabel
				(
					"labelEquipped",
					Coords.fromXY(100, 5), // pos
					Coords.fromXY(100, 25), // size
					false, // isTextCentered
					"Equipped:",
					fontHeightSmall
				),

				listEquipped,

				new ControlLabel
				(
					"infoStatus",
					Coords.fromXY(sizeBase.x / 2, 125), // pos
					Coords.fromXY(sizeBase.x, 15), // size
					true, // isTextCentered
					DataBinding.fromContextAndGet
					(
						this,
						(EquipmentUser c) -> c.statusMessage
					), // text
					fontHeightSmall
				)
			},

			new ActorAction[]
			{
				new ActorAction("Back", back),
				new ActorAction("EquipItemSelectedInQuickSlot0", () -> equipItemSelectedInQuickSlot(0)),
				new ActorAction("EquipItemSelectedInQuickSlot1", () -> equipItemSelectedInQuickSlot(1)),
				new ActorAction("EquipItemSelectedInQuickSlot2", () -> equipItemSelectedInQuickSlot(2)),
				new ActorAction("EquipItemSelectedInQuickSlot3", () -> equipItemSelectedInQuickSlot(3)),
				new ActorAction("EquipItemSelectedInQuickSlot4", () -> equipItemSelectedInQuickSlot(4)),
				new ActorAction("EquipItemSelectedInQuickSlot5", () -> equipItemSelectedInQuickSlot(5)),
				new ActorAction("EquipItemSelectedInQuickSlot6", () -> equipItemSelectedInQuickSlot(6)),
				new ActorAction("EquipItemSelectedInQuickSlot7", () -> equipItemSelectedInQuickSlot(7)),
				new ActorAction("EquipItemSelectedInQuickSlot8", () -> equipItemSelectedInQuickSlot(8)),
				new ActorAction("EquipItemSelectedInQuickSlot9", () -> equipItemSelectedInQuickSlot(9))
			},

			new ActionToInputsMapping[]
			{
				new ActionToInputsMapping( "Back", new String[] { Input.Names().Escape }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot0", new String[] { "_0" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot1", new String[] { "_1" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot2", new String[] { "_2" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot3", new String[] { "_3" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot4", new String[] { "_4" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot5", new String[] { "_5" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot6", new String[] { "_6" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot7", new String[] { "_7" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot8", new String[] { "_8" }, true ),
				new ActionToInputsMapping( "EquipItemSelectedInQuickSlot9", new String[] { "_9" }, true )
			}

		);

		if (includeTitleAndDoneButton)
		{
			var childControls = returnValue.children;

			childControls.splice
			(
				0, 0,
				new ControlLabel
				(
					"labelEquipment",
					Coords.fromXY(100, -5), // pos
					Coords.fromXY(100, 25), // size
					true, // isTextCentered
					"Equip",
					fontHeightLarge
				)
			);
			childControls.push
			(
				ControlButton.from8
				(
					"buttonDone",
					Coords.fromXY(170, 115), // pos
					Coords.fromXY(20, 10), // size
					"Done",
					fontHeightSmall,
					true, // hasBorder
					DataBinding.fromTrue(), // isEnabled
					back // click
				)
			);

			var titleHeight = Coords.fromXY(0, 15);
			sizeBase.add(titleHeight);
			returnValue.size.add(titleHeight);
			returnValue.shiftChildPositions(titleHeight);
		}

		var scaleMultiplier = size.clone().divide(sizeBase);
		returnValue.scalePosAndSize(scaleMultiplier);

		return returnValue;
	}
}
