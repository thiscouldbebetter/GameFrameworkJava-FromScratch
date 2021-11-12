
package GameFramework.Model.Items.Crafting;

import java.util.*;
import java.util.function.*;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*;
import GameFramework.Input.*;
import GameFramework.Model.*;
import GameFramework.Model.Actors.*;
import GameFramework.Model.Items.*;

public class ItemCrafter implements EntityProperty<ItemCrafter>
{
	public CraftingRecipe[] recipesAvailable;

	public ItemHolder itemHolderStaged;
	public CraftingRecipe recipeAvailableSelected;
	public int recipeInProgressTicksSoFar;
	public CraftingRecipe recipeQueuedSelected;
	public List<CraftingRecipe> recipesQueued;
	public String statusMessage;

	public ItemCrafter(CraftingRecipe[] recipesAvailable)
	{
		this.recipesAvailable = recipesAvailable;

		this.itemHolderStaged = ItemHolder.create();
		this.recipeAvailableSelected = null;
		this.recipeInProgressTicksSoFar = 0;
		this.recipeQueuedSelected = null;
		this.recipesQueued = new ArrayList<CraftingRecipe>();
		this.statusMessage = "-";
	}

	public boolean isRecipeAvailableSelectedFulfilled(ItemHolder itemHolder)
	{
		var returnValue =
		(
			this.recipeAvailableSelected == null
			? false
			: this.recipeAvailableSelected.isFulfilledByItemHolder(itemHolder)
		);

		return returnValue;
	}

	public boolean isRecipeInProgressFulfilled()
	{
		var recipeInProgress = this.recipesQueued.get(0);
		var returnValue =
		(
			recipeInProgress == null
			? false
			: recipeInProgress.isFulfilledByItemHolder(this.itemHolderStaged)
		);

		return returnValue;
	}

	public void recipeInProgressCancel()
	{
		// todo
	}

	public int recipeInProgressSecondsSoFar(Universe universe)
	{
		return this.recipeInProgressTicksSoFar / universe.timerHelper.ticksPerSecond;
	}

	public void recipeInProgressFinish(Entity entityCrafter)
	{
		var recipe = this.recipesQueued.get(0);

		var itemsOut = recipe.itemsOut;
		for (var i = 0; i < itemsOut.length; i++)
		{
			var itemOut = itemsOut[i];
			entityCrafter.itemHolder().itemAdd(itemOut);
		}

		this.itemHolderStaged.items.clear();
		this.recipeInProgressTicksSoFar = 0;
		this.recipesQueued.remove(0);
	}

	public String recipeProgressAsString(Universe universe)
	{
		String returnValue = null;
		var recipeInProgress = this.recipesQueued.get(0);
		if (recipeInProgress == null)
		{
			returnValue = "-";
		}
		else
		{
			returnValue =
				recipeInProgress.name
				+ " ("
				+ this.recipeInProgressSecondsSoFar(universe)
				+ "/"
				+ recipeInProgress.secondsToComplete(universe)
				+ "s)";
		}
		return returnValue;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe){}
	public void initialize(UniverseWorldPlaceEntities uwpe){}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		if (this.recipesQueued.size() > 0)
		{
			var recipeInProgress = this.recipesQueued.get(0);
			if (this.isRecipeInProgressFulfilled())
			{
				if (this.recipeInProgressTicksSoFar >= recipeInProgress.ticksToComplete)
				{
					var entityCrafter = uwpe.entity;
					this.recipeInProgressFinish(entityCrafter);
				}
				else
				{
					this.recipeInProgressTicksSoFar++;
				}
			}
			else
			{
				this.recipeInProgressTicksSoFar = 0;
				this.recipesQueued.remove(0);
			}
		}
	}

	// controls

	public ControlBase toControl
	(
		Universe universe,
		Coords size,
		Entity entityCrafter,
		Entity entityItemHolder,
		Venue venuePrev,
		boolean includeTitleAndDoneButton
	)
	{
		this.statusMessage = "Select a recipe and click Craft.";

		if (size == null)
		{
			size = universe.display.sizeDefault().clone();
		}

		var sizeBase = new Coords(200, 135, 1);

		var fontHeight = 10;
		var fontHeightSmall = fontHeight * 0.6;
		var fontHeightLarge = fontHeight * 1.5;

		var itemHolder = entityItemHolder.itemHolder();
		var crafter = this;

		Runnable back = () ->
		{
			var venueNext = venuePrev;
			venueNext = VenueFader.fromVenuesToAndFrom(venueNext, universe.venueCurrent);
			universe.venueNext = venueNext;
		};

		Runnable addToQueue = () ->
		{
			if (crafter.isRecipeAvailableSelectedFulfilled( entityCrafter.itemHolder() ) )
			{
				var recipe = crafter.recipeAvailableSelected;

				var itemsIn = recipe.itemsIn;
				for (var i = 0; i < itemsIn.length; i++)
				{
					var itemIn = itemsIn[i];
					itemHolder.itemTransferTo(itemIn, this.itemHolderStaged);
				}
				crafter.recipesQueued.add(crafter.recipeAvailableSelected);
			}
		};

		Consumer<Universe> listRecipesConfirm = (Universe u) ->
		{
			addToQueue.run();
		};

		var returnValue = new ControlContainer
		(
			"Craft",
			Coords.create(), // pos
			sizeBase.clone(), // size
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelRecipes",
					Coords.fromXY(10, 5), // pos
					Coords.fromXY(70, 25), // size
					false, // isTextCentered
					DataBinding.fromContext("Recipes:"),
					fontHeightSmall
				),

				new ControlList
				(
					"listRecipes",
					Coords.fromXY(10, 15), // pos
					Coords.fromXY(85, 100), // size
					DataBinding.fromContextAndGet
					(
						crafter,
						(ItemCrafter c) -> c.recipesAvailable
					), // items
					DataBinding.fromGet
					(
						(CraftingRecipe c) -> c.name
					), // bindingForItemText
					fontHeightSmall,
					new DataBinding<ItemCrafter,CraftingRecipe>
					(
						crafter,
						(ItemCrafter c) -> c.recipeAvailableSelected,
						(ItemCrafter c, CraftingRecipe v) ->
							c.recipeAvailableSelected = v
					), // bindingForItemSelected
					DataBinding.fromGet
					(
						(CraftingRecipe c) -> c
					), // bindingForItemValue
					DataBinding.fromTrue(), // isEnabled
					listRecipesConfirm,
					null
				),

				/*
				new ControlLabel
				(
					"labelRecipeSelected",
					Coords.fromXY(105, 5), // pos
					Coords.fromXY(70, 25), // size
					false, // isTextCentered
					"Recipe Selected:",
					fontHeightSmall
				),

				new ControlButton
				(
					"buttonCraft",
					Coords.fromXY(170, 5), // pos
					Coords.fromXY(20, 10), // size
					"Craft",
					fontHeightSmall,
					true, // hasBorder
					DataBinding.fromContextAndGet
					(
						this,
						(ItemCrafter c) ->
							c.isRecipeAvailableSelectedFulfilled(entityCrafter.itemHolder())
					), // isEnabled
					addToQueue, // click
					null, null
				),

				new ControlLabel
				(
					"infoRecipeSelected",
					Coords.fromXY(105, 10), // pos
					Coords.fromXY(75, 25), // size
					false, // isTextCentered
					DataBinding.fromContextAndGet
					(
						this,
						(ItemCrafter c) ->
							(
								(c.recipeAvailableSelected == null)
								? "-"
								: c.recipeAvailableSelected.nameAndSecondsToCompleteAsString(universe)
							)
					),
					fontHeightSmall
				),

				ControlList.from8
				(
					"listItemsInRecipe",
					Coords.fromXY(105, 20), // pos
					Coords.fromXY(85, 25), // size
					DataBinding.fromContextAndGet
					(
						this,
						(ItemCrafter c) ->
							(
								c.recipeAvailableSelected == null
								? []
								: c.recipeAvailableSelected.itemsInHeldOverRequiredForItemHolder(itemHolder)
							)
					), // items
					DataBinding.fromGet
					(
						(string c) -> c
					), // bindingForItemText
					fontHeightSmall,
					null, // bindingForItemSelected
					DataBinding.fromGet( (Entity c) -> c ) // bindingForItemValue
				),

				new ControlLabel
				(
					"labelCrafting",
					Coords.fromXY(105, 50), // pos
					Coords.fromXY(75, 25), // size
					false, // isTextCentered
					DataBinding.fromContext("Crafting:"),
					fontHeightSmall
				),

				ControlButton.from8
				(
					"buttonCancel",
					Coords.fromXY(170, 50), // pos
					Coords.fromXY(20, 10), // size
					"Cancel",
					fontHeightSmall,
					true, // hasBorder
					DataBinding.fromContextAndGet
					(
						this,
						(ItemCrafter c) -> (c.recipesQueued.length > 0)
					), // isEnabled
					crafter.recipeInProgressCancel // click
				),

				new ControlLabel
				(
					"infoCrafting",
					Coords.fromXY(105, 55), // pos
					Coords.fromXY(75, 25), // size
					false, // isTextCentered
					DataBinding.fromContextAndGet
					(
						this,
						(ItemCrafter c) -> c.recipeProgressAsString(universe)
					),
					fontHeightSmall
				),

				ControlList.from8
				(
					"listCraftingsQueued",
					Coords.fromXY(105, 65), // pos
					Coords.fromXY(85, 35), // size
					DataBinding.fromContextAndGet
					(
						this,
						(ItemCrafter c) -> c.recipesQueued
					), // items
					DataBinding.fromGet
					(
						(CraftingRecipe c) -> c.name
					), // bindingForItemText
					fontHeightSmall,
					new DataBinding
					(
						this,
						(ItemCrafter c) -> c.recipeQueuedSelected,
						(ItemCrafter c, CraftingRecipe v) ->
							c.recipeQueuedSelected = v
					), // bindingForItemSelected
					DataBinding.fromGet( (Entity c) -> c ) // bindingForItemValue
				),

				new ControlLabel
				(
					"infoStatus",
					Coords.fromXY(100, 125), // pos
					Coords.fromXY(200, 15), // size
					true, // isTextCentered
					DataBinding.fromContextAndGet
					(
						this,
						(ItemCrafter c) -> c.statusMessage
					), // text
					fontHeightSmall
				)
				*/

			}, // end children

			new ActorAction[]
			{
				new ActorAction("Back", (UniverseWorldPlaceEntities uwpeBack) -> back.run() ),
			},

			new ActionToInputsMapping[]
			{
				new ActionToInputsMapping("Back", new String[] { Input.Names().Escape }, true ),
			}
		);

		if (includeTitleAndDoneButton)
		{
			returnValue.children.add
			(
				0,
				new ControlLabel
				(
					"labelCrafting",
					Coords.fromXY(100, -5), // pos
					Coords.fromXY(100, 25), // size
					true, // isTextCentered
					DataBinding.fromContext("Craft"),
					fontHeightLarge
				)
			);

			returnValue.children.add
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

	// cloneable

	public ItemCrafter clone()
	{
		return new ItemCrafter
		(
			ArrayHelper.clone(this.recipesAvailable).toArray(new CraftingRecipe[] {})
		);
	}

	public ItemCrafter overwriteWith(ItemCrafter other)
	{
		return this;
	}
}
