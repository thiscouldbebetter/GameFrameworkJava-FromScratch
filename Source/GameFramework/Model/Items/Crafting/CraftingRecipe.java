
package GameFramework.Model.Items.Crafting;

import java.util.*;

import GameFramework.Helpers.*;
import GameFramework.Model.*;
import GameFramework.Model.Items.*;
import GameFramework.Utility.*;

public class CraftingRecipe implements Clonable<CraftingRecipe>
{
	public String name;
	public int ticksToComplete;
	public Item[] itemsIn;
	public Item[] itemsOut;

	public CraftingRecipe
	(
		String name,
		int ticksToComplete,
		Item[] itemsIn,
		Item[] itemsOut
	)
	{
		this.name = name;
		this.ticksToComplete = ticksToComplete;
		this.itemsIn = itemsIn;
		this.itemsOut = itemsOut;
	}

	public boolean isFulfilledByItemHolder(ItemHolder itemHolderStaged)
	{
		var itemsStaged = itemHolderStaged.items;
		var areAllRequirementsFulfilledSoFar = true;

		for (var i = 0; i < this.itemsIn.length; i++)
		{
			var itemRequired = this.itemsIn[i];
			var itemStaged = itemsStaged.stream().filter
			(
				x -> x.defnName == itemRequired.defnName
			).findFirst().get();
			var isRequirementFulfilled =
			(
				itemStaged != null
				&& itemStaged.quantity >= itemRequired.quantity
			);

			if (isRequirementFulfilled == false)
			{
				areAllRequirementsFulfilledSoFar = false;
				break;
			}
		}

		return areAllRequirementsFulfilledSoFar;
	}

	public String[] itemsInHeldOverRequiredForItemHolder(ItemHolder itemHolder)
	{
		return Arrays.asList
		(
			this.itemsIn
		).stream().map
		(
			x ->
				x.defnName
				+ " ("
				+ itemHolder.itemQuantityByDefnName(x.defnName)
				+ "/"
				+ x.quantity
				+ ")"
		);
	}

	public String nameAndSecondsToCompleteAsString(Universe universe)
	{
		return this.name + " (" + this.secondsToComplete(universe) + "s)";
	}

	public int secondsToComplete(Universe universe)
	{
		return (this.ticksToComplete / universe.timerHelper.ticksPerSecond);
	}

	// Cloneable.

	public CraftingRecipe clone()
	{
		return new CraftingRecipe
		(
			this.name,
			this.ticksToComplete,
			ArrayHelper.clone(this.itemsIn),
			ArrayHelper.clone(this.itemsOut)
		);
	}

	public CraftingRecipe overwriteWith(CraftingRecipe other)
	{
		return this;
	}

}
