
package GameFramework.Model.Items;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import GameFramework.Controls.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;
import GameFramework.Model.Places.*;

public class ItemHolder implements EntityProperty
{
	public List<Item> items;
	public Double massMax;
	public Double reachRadius;

	public Item itemSelected;
	public String statusMessage;

	public ItemHolder
	(
		Item[] items, Double massMax, Double reachRadius
	)
	{
		this.items = new ArrayList<Item>();
		this.massMax = massMax;
		this.reachRadius = reachRadius;

		this.itemsAdd(items);
	}

	public static ItemHolder create()
	{
		return new ItemHolder(new Item[] {}, null, null);
	}

	public static ItemHolder fromItems(Item[] items)
	{
		return new ItemHolder(items, null, null);
	}

	// Instance methods.

	public ItemHolder clear()
	{
		this.items.length = 0;
		this.itemSelected = null;
		this.statusMessage = "";

		return this;
	}

	public void equipItemInNumberedSlot
	(
		Universe universe, Entity entityItemHolder, double slotNumber
	)
	{
		var itemToEquip = this.itemSelected;
		if (itemToEquip != null)
		{
			var world = universe.world;
			var place = world.placeCurrent;
			var equipmentUser = entityItemHolder.equipmentUser();
			var socketName = "Item" + slotNumber;
			var includeSocketNameInMessage = true;
			var itemEntityToEquip = itemToEquip.toEntity
			(
				new UniverseWorldPlaceEntities
				(
					universe, world, place, entityItemHolder, null
				)
			);
			var uwpe = new UniverseWorldPlaceEntities
			(
				universe, world, place, entityItemHolder, itemEntityToEquip
			);
			var message = equipmentUser.equipItemEntityInSocketWithName
			(
				uwpe, socketName, includeSocketNameInMessage
			);
			this.statusMessage = message;
		}
	}

	public boolean hasItem(Item itemToCheck)
	{
		return this.hasItemWithDefnNameAndQuantity
		(
			itemToCheck.defnName, itemToCheck.quantity
		);
	}

	public boolean hasItemWithDefnNameAndQuantity
	(
		String defnName, double quantityToCheck
	)
	{
		var itemExistingQuantity = this.itemQuantityByDefnName(defnName);
		var returnValue = (itemExistingQuantity >= quantityToCheck);
		return returnValue;
	}

	public List<Entity> itemEntities(UniverseWorldPlaceEntities uwpe)
	{
		return this.items.stream().map(x -> x.toEntity(uwpe)).toList();
	}

	public void itemsAdd(Item[] itemsToAdd)
	{
		itemsToAdd.forEach( (Item x) -> this.itemAdd(x));
	}

	public void itemsAllTransferTo(ItemHolder other)
	{
		this.itemsTransferTo(this.items, other);
	}

	public List<Item> itemsByDefnName(String defnName)
	{
		return this.items.stream().filter
		(
			x -> x.defnName == defnName
		).toList();
	}

	public void itemsTransferTo(Item itemsToTransfer[], ItemHolder other)
	{
		if (itemsToTransfer == this.items)
		{
			// Create a new array to avoid modifying the one being looped through.
			itemsToTransfer = new ArrayList<Item>();
			itemsToTransfer.addAll(this.items);
		}

		for (var i = 0; i < itemsToTransfer.length; i++)
		{
			var item = itemsToTransfer[i];
			this.itemTransferTo(item, other);
		}
	}

	public Item itemsWithDefnNameJoin(String defnName)
	{
		var itemsMatching = this.items.filter
		(
			x -> x.defnName == defnName
		);
		var itemJoined = itemsMatching[0];
		if (itemJoined != null)
		{
			for (var i = 1; i < itemsMatching.length; i++)
			{
				var itemToJoin = itemsMatching[i];
				itemJoined.quantity += itemToJoin.quantity;
				ArrayHelper.remove(this.items, itemToJoin);
			}
		}

		return itemJoined;
	}

	public void itemAdd(Item itemToAdd)
	{
		var itemDefnName = itemToAdd.defnName;
		var itemExisting = this.itemsByDefnName(itemDefnName).get(0);
		if (itemExisting == null)
		{
			this.items.add(itemToAdd);
		}
		else
		{
			itemExisting.quantity += itemToAdd.quantity;
		}
	}

	public Entity itemEntityFindClosest(UniverseWorldPlaceEntities uwpe)
	{
		var place = uwpe.place;
		var entityItemHolder = uwpe.entity;

		var entityItemsInPlace = place.items();
		var entityItemClosest = entityItemsInPlace.filter
		(
			x ->
				x.locatable().distanceFromEntity(entityItemHolder) < this.reachRadius
		).sort
		(
			(a, b) ->
				a.locatable().distanceFromEntity(entityItemHolder)
				- b.locatable().distanceFromEntity(entityItemHolder)
		)[0];

		return entityItemClosest;
	}

	public boolean itemCanPickUp
	(
		Universe universe, World world, Place place, Item itemToPickUp
	)
	{
		var massAlreadyHeld = this.massOfAllItems(world);
		var massOfItem = itemToPickUp.mass(world);
		var massAfterPickup = massAlreadyHeld + massOfItem;
		var canPickUp = (massAfterPickup <= this.massMax);
		return canPickUp;
	}

	public void itemEntityPickUp
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		var place = uwpe.place;
		var itemEntityToPickUp = uwpe.entity2;
		var itemToPickUp = itemEntityToPickUp.item();
		this.itemAdd(itemToPickUp);
		place.entityToRemoveAdd(itemEntityToPickUp);
	}

	public void itemRemove(Item itemToRemove)
	{
		var doesExist = this.items.indexOf(itemToRemove) >= 0;
		if (doesExist)
		{
			ArrayHelper.remove(this.items, itemToRemove);
		}
	}

	public Item itemSplit(Item itemToSplit, double quantityToSplit)
	{
		var itemSplitted = null;

		if (itemToSplit.quantity <= 1)
		{
			itemSplitted = itemToSplit;
		}
		else
		{
			quantityToSplit =
				quantityToSplit || Math.floor(itemToSplit.quantity / 2);
			if (quantityToSplit >= itemToSplit.quantity)
			{
				itemSplitted = itemToSplit;
			}
			else
			{
				itemToSplit.quantity -= quantityToSplit;

				itemSplitted = itemToSplit.clone();
				itemSplitted.quantity = quantityToSplit;
				// Add with no join.
				ArrayHelper.insertElementAfterOther
				(
					this.items, itemSplitted, itemToSplit
				);
			}
		}

		return itemSplitted;
	}

	public void itemTransferTo(Item item, ItemHolder other)
	{
		other.itemAdd(item);
		ArrayHelper.remove(this.items, item);
		if (this.itemSelected == item)
		{
			this.itemSelected = null;
		}
	}

	public void itemTransferSingleTo(Item item, ItemHolder other)
	{
		var itemSingle = this.itemSplit(item, 1);
		this.itemTransferTo(itemSingle, other);
	}

	public double itemQuantityByDefnName(String defnName)
	{
		return this.itemsByDefnName(defnName).map
		(
			y -> y.quantity
		).reduce
		(
			(a,b) -> a + b, 0
		);
	}

	public void itemSubtract(Item itemToSubtract)
	{
		this.itemSubtractDefnNameAndQuantity
		(
			itemToSubtract.defnName, itemToSubtract.quantity
		);
	}

	public void itemSubtractDefnNameAndQuantity
	(
		String itemDefnName, double quantityToSubtract
	)
	{
		this.itemsWithDefnNameJoin(itemDefnName);
		var itemExisting = this.itemsByDefnName(itemDefnName)[0];
		if (itemExisting != null)
		{
			itemExisting.quantity -= quantityToSubtract;
			if (itemExisting.quantity <= 0)
			{
				var itemExisting = this.itemsByDefnName(itemDefnName)[0];
				ArrayHelper.remove(this.items, itemExisting);
			}
		}
	}

	/*
	itemTransferTo2(Item itemToTransfer, ItemHolder other): void
	{
		var itemDefnName = itemToTransfer.defnName;
		this.itemsWithDefnNameJoin(itemDefnName);
		var itemExisting = this.itemsByDefnName(itemDefnName)[0];
		if (itemExisting != null)
		{
			var itemToTransfer =
				this.itemSplit(itemExisting, itemToTransfer.quantity);
			other.itemAdd(itemToTransfer.clone());
			this.itemSubtract(itemToTransfer);
		}
	}
	*/

	public List<Item> itemsByDefnName2(String defnName)
	{
		return this.itemsByDefnName(defnName);
	}

	public double massOfAllItems(World world)
	{
		var massTotal = this.items.reduce
		(
			(sumSoFar, item) -> sumSoFar + item.mass(world),
			0 // sumSoFar
		);

		return massTotal;
	}

	public String massOfAllItemsOverMax(World world)
	{
		return "" + Math.ceil(this.massOfAllItems(world)) + "/" + this.massMax;
	}

	public double tradeValueOfAllItems(World world)
	{
		var tradeValueTotal = this.items.reduce
		(
			(sumSoFar, item) -> sumSoFar + item.tradeValue(world),
			0 // sumSoFar
		);

		return tradeValueTotal;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

	// Controllable.

	public ControlBase toControl
	(
		Universe universe, Coords size, Entity entityItemHolder,
		Venue venuePrev, boolean includeTitleAndDoneButton
	)
	{
		this.statusMessage = "Use, drop, and sort items.";

		if (size == null)
		{
			size = universe.display.sizeDefault().clone();
		}

		var uwpe = new UniverseWorldPlaceEntities
		(
			universe, universe.world, universe.world.placeCurrent,
			entityItemHolder, null
		);

		var sizeBase = new Coords(200, 135, 1);

		var fontHeight = 10;
		var fontHeightSmall = fontHeight * .6;
		var fontHeightLarge = fontHeight * 1.5;

		var itemHolder = this;
		var world = universe.world;

		Runnable back = () ->
		{
			Venue venueNext = venuePrev;
			venueNext = VenueFader.fromVenuesToAndFrom
			(
				venueNext, universe.venueCurrent
			);
			universe.venueNext = venueNext;
		};

		Runnable drop = () ->
		{
			if (itemHolder.itemSelected == null)
			{
				return;
			}
			var itemToKeep = itemHolder.itemSelected;
			if (itemToKeep != null)
			{
				var world = universe.world;
				var place = world.placeCurrent;

				var itemToDrop = itemToKeep.clone();
				itemToDrop.quantity = 1;
				var itemToDropDefn = itemToDrop.defn(world);

				var itemEntityToDrop = itemToDrop.toEntity(uwpe);
				var itemLocatable = itemEntityToDrop.locatable();
				if (itemLocatable == null)
				{
					itemLocatable = Locatable.create();
					itemEntityToDrop.propertyAdd(itemLocatable);
					itemEntityToDrop.propertyAdd
					(
						Drawable.fromVisual(itemToDropDefn.visual)
					);
					// todo - Other Collidable properties, etc.
				}

				var posToDropAt = itemLocatable.loc.pos;
				var holderPos = entityItemHolder.locatable().loc.pos;
				posToDropAt.overwriteWith(holderPos);

				var collidable = itemEntityToDrop.collidable();
				if (collidable != null)
				{
					collidable.ticksUntilCanCollide =
						collidable.ticksToWaitBetweenCollisions;
				}

				place.entitySpawn
				(
					new UniverseWorldPlaceEntities
					(
						universe, world, place, itemEntityToDrop, null
					)
				);
				itemHolder.itemSubtract(itemToDrop);
				if (itemToKeep.quantity == 0)
				{
					itemHolder.itemSelected = null;
				}

				itemHolder.statusMessage =
					itemToDropDefn.appearance + " dropped.";

				var equipmentUser = entityItemHolder.equipmentUser();
				if (equipmentUser != null)
				{
					equipmentUser.unequipItemsNoLongerHeld
					(
						uwpe
					);
				}
			}
		};

		Runnable use = () ->
		{
			var itemEntityToUse = itemHolder.itemSelected.toEntity
			(
				uwpe
			);
			if (itemEntityToUse != null)
			{
				var itemToUse = itemEntityToUse.item();
				if (itemToUse.use != null)
				{
					itemHolder.statusMessage =
						itemToUse.use(uwpe);
					if (itemToUse.quantity <= 0)
					{
						itemHolder.itemSelected = null;
					}
				}
			}
		};

		Runnable up = () ->
		{
			var itemToMove = itemHolder.itemSelected;
			var itemsAll = itemHolder.items;
			var index = itemsAll.indexOf(itemToMove);
			if (index > 0)
			{
				itemsAll.splice(index, 1);
				itemsAll.splice(index - 1, 0, itemToMove);
			}
		};

		Runnable down = () ->
		{
			var itemToMove = itemHolder.itemSelected;
			var itemsAll = itemHolder.items;
			var index = itemsAll.indexOf(itemToMove);
			if (index < itemsAll.length - 1)
			{
				itemsAll.splice(index, 1);
				itemsAll.splice(index + 1, 0, itemToMove);
			}
		};

		Runnable split = (UniverseWorldPlaceEntities uwpe) ->
		{
			itemHolder.itemSplit(itemHolder.itemSelected, null);
		};

		Runnable join = () ->
		{
			var itemToJoin = itemHolder.itemSelected;
			var itemJoined =
				itemHolder.itemsWithDefnNameJoin(itemToJoin.defnName);
			itemHolder.itemSelected = itemJoined;
		};

		Runnable sort = () ->
		{
			itemHolder.items.sort
			(
				(x, y) -> (x.defnName > y.defnName ? 1 : -1)
			);
		};

		var buttonSize = Coords.fromXY(20, 10);
		var visualNone = new VisualNone();

		/*
		// todo
		var controlVisualBackground = ControlVisual.from4
		(
			"imageBackground",
			Coords.zeroes(),
			sizeBase.clone(), // size
			DataBinding.fromContext<Visual>
			(
				new VisualGroup
				([
					new VisualImageScaled
					(
						new VisualImageFromLibrary("Title"), size
					)
				])
			)
		);
		*/

		var childControls = new ControlBase[]
		{
			//controlVisualBackground,

			new ControlLabel
			(
				"labelItemsHeld",
				Coords.fromXY(10, 5), // pos
				Coords.fromXY(70, 25), // size
				false, // isTextCentered
				"Items Held:",
				fontHeightSmall
			),

			ControlList.from10
			(
				"listItems",
				Coords.fromXY(10, 15), // pos
				Coords.fromXY(70, 100), // size
				DataBinding.fromContext(this.items), // items
				DataBinding.fromGet
				(
					(Item c) -> c.toString(world)
				), // bindingForItemText
				fontHeightSmall,
				new DataBinding
				(
					this,
					(ItemHolder c) -> c.itemSelected,
					(ItemHolder c, Item v) -> c.itemSelected = v
				), // bindingForItemSelected
				DataBinding.fromGet( (Item c) -> c ), // bindingForItemValue
				DataBinding.fromTrue(), // isEnabled
				use
			),

			new ControlLabel
			(
				"infoWeight",
				Coords.fromXY(10, 115), // pos
				Coords.fromXY(100, 25), // size
				false, // isTextCentered
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) -> "Weight: " + c.massOfAllItemsOverMax(world)
				),
				fontHeightSmall
			),

			ControlButton.from8
			(
				"buttonUp",
				Coords.fromXY(85, 15), // pos
				Coords.fromXY(15, 10), // size
				"Up",
				fontHeightSmall,
				true, // hasBorder
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) ->
					{
						var returnValue =
						(
							c.itemSelected != null
							&& c.items.indexOf(c.itemSelected) > 0
						);
						return returnValue;
					}
				), // isEnabled
				up // click
			),

			ControlButton.from8
			(
				"buttonDown",
				Coords.fromXY(85, 30), // pos
				Coords.fromXY(15, 10), // size
				"Down",
				fontHeightSmall,
				true, // hasBorder
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) ->
					{
						var returnValue =
						(
							c.itemSelected != null
							&& c.items.indexOf(c.itemSelected) < c.items.length - 1
						);
						return returnValue;
					}
				), // isEnabled
				down
			),

			ControlButton.from8
			(
				"buttonSplit",
				Coords.fromXY(85, 45), // pos
				Coords.fromXY(15, 10), // size
				"Split",
				fontHeightSmall,
				true, // hasBorder
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) ->
					{
						var item = c.itemSelected;
						var returnValue =
						(
							item != null
							&& (item.quantity > 1)
						);
						return returnValue;
					}
				), // isEnabled
				split
			),

			ControlButton.from8
			(
				"buttonJoin",
				Coords.fromXY(85, 60), // pos
				Coords.fromXY(15, 10), // size
				"Join",
				fontHeightSmall,
				true, // hasBorder
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) ->
						c.itemSelected != null
						&&
						(
							c.items.filter
							(
								(Item x) -> x.defnName == c.itemSelected.defnName
							).length > 1
						)
				), // isEnabled
				join
			),

			ControlButton.from8
			(
				"buttonSort",
				Coords.fromXY(85, 75), // pos
				Coords.fromXY(15, 10), // size
				"Sort",
				fontHeightSmall,
				true, // hasBorder
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) -> (c.itemEntities.length > 1)
				), // isEnabled
				sort
			),

			new ControlLabel
			(
				"labelItemSelected",
				Coords.fromXY(150, 10), // pos
				Coords.fromXY(100, 15), // size
				true, // isTextCentered
				"Item Selected:",
				fontHeightSmall
			),

			new ControlLabel
			(
				"infoItemSelected",
				Coords.fromXY(150, 20), // pos
				Coords.fromXY(200, 15), // size
				true, // isTextCentered
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) ->
					{
						var i = c.itemSelected;
						return (i == null ? "-" : i.toString(world));
					}
				), // text
				fontHeightSmall
			),

			ControlVisual.from5
			(
				"visualImage",
				Coords.fromXY(125, 25), // pos
				Coords.fromXY(50, 50), // size
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) ->
					{
						var i = c.itemSelected;
						return (i == null ? visualNone : i.defn(world).visual);
					}
				),
				Color.byName("Black") // colorBackground
			),

			new ControlLabel
			(
				"infoStatus",
				Coords.fromXY(150, 115), // pos
				Coords.fromXY(200, 15), // size
				true, // isTextCentered
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) -> c.statusMessage
				), // text
				fontHeightSmall
			),

			ControlButton.from8
			(
				"buttonUse",
				Coords.fromXY(132.5, 95), // pos
				Coords.fromXY(15, 10), // size
				"Use",
				fontHeightSmall,
				true, // hasBorder
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) ->
					{
						var item = c.itemSelected;
						return (item != null && item.isUsable(world));
					}
				), // isEnabled
				use // click
			),

			ControlButton.from8
			(
				"buttonDrop",
				Coords.fromXY(152.5, 95), // pos
				Coords.fromXY(15, 10), // size
				"Drop",
				fontHeightSmall,
				true, // hasBorder
				DataBinding.fromContextAndGet
				(
					this,
					(ItemHolder c) -> (c.itemSelected != null)
				), // isEnabled
				drop // click
			)
		};

		var returnValue = new ControlContainer
		(
			"Items",
			Coords.create(), // pos
			sizeBase.clone(), // size
			childControls,
			new ActorAction[]
			{
				new ActorAction("Back", back),

				new ActorAction("Up", up),
				new ActorAction("Down", down),
				new ActorAction("Split", split),
				new ActorAction("Join", join),
				new ActorAction("Sort", sort),
				new ActorAction("Drop", drop),
				new ActorAction("Use", use),

				new ActorAction("Item0", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, null) ),
				new ActorAction("Item1", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 1) ),
				new ActorAction("Item2", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 2) ),
				new ActorAction("Item3", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 3) ),
				new ActorAction("Item4", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 4) ),
				new ActorAction("Item5", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 5) ),
				new ActorAction("Item6", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 6) ),
				new ActorAction("Item7", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 7) ),
				new ActorAction("Item8", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 8) ),
				new ActorAction("Item9", () -> itemHolder.equipItemInNumberedSlot(universe, entityItemHolder, 9) ),
			},

			new ActionToInputsMapping[]
			{
				new ActionToInputsMapping( "Back", new String[] { Input.Names().Escape }, true ),

				new ActionToInputsMapping( "Up", new String[] { "new String[] {" }, true ),
				new ActionToInputsMapping( "Down", new String[] { "}" }, true ),
				new ActionToInputsMapping( "Sort", new String[] { "\\" }, true ),
				new ActionToInputsMapping( "Split", new String[] { "/" }, true ),
				new ActionToInputsMapping( "Join", new String[] { "=" }, true ),
				new ActionToInputsMapping( "Drop", new String[] { "d" }, true ),
				new ActionToInputsMapping( "Use", new String[] { "e" }, true ),

				new ActionToInputsMapping( "Item0", new String[] { "_0" }, true ),
				new ActionToInputsMapping( "Item1", new String[] { "_1" }, true ),
				new ActionToInputsMapping( "Item2", new String[] { "_2" }, true ),
				new ActionToInputsMapping( "Item3", new String[] { "_3" }, true ),
				new ActionToInputsMapping( "Item4", new String[] { "_4" }, true ),
				new ActionToInputsMapping( "Item5", new String[] { "_5" }, true ),
				new ActionToInputsMapping( "Item6", new String[] { "_6" }, true ),
				new ActionToInputsMapping( "Item7", new String[] { "_7" }, true ),
				new ActionToInputsMapping( "Item8", new String[] { "_8" }, true ),
				new ActionToInputsMapping( "Item9", new String[] { "_9" }, true ),
			}
		);

		if (includeTitleAndDoneButton)
		{
			childControls.insert
			(
				0, // indexToInsertAt
				new ControlLabel
				(
					"labelItems",
					Coords.fromXY(100, -5), // pos
					Coords.fromXY(100, 25), // size
					true, // isTextCentered
					"Items",
					fontHeightLarge
				)
			);
			childControls.add
			(
				ControlButton.from8
				(
					"buttonDone",
					Coords.fromXY(170, 115), // pos
					buttonSize.clone(),
					"Done",
					fontHeightSmall,
					true, // hasBorder
					true, // isEnabled
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

	// cloneable

	public ItemHolder clone()
	{
		return new ItemHolder
		(
			ArrayHelper.clone(this.items),
			this.massMax,
			this.reachRadius
		);
	}
	
	public ItemHolder overwriteWith(ItemHolder other) { return this; }
}