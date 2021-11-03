
package GameFramework.Model.Items;

import java.util.function.*;

import GameFramework.Display.*;
import GameFramework.Display.Visuals.*;
import GameFramework.Model.*;

public class ItemDefn implements EntityProperty<ItemDefn>
{
	public String name;
	public String appearance;
	public String description;
	public double mass;
	public Double tradeValue;
	public Double stackSizeMax;
	public String[] categoryNames;
	public Function<UniverseWorldPlaceEntities,String> _use;
	public Visual visual;
	private BiFunction<UniverseWorldPlaceEntities,Item,Entity> _toEntity;

	public ItemDefn
	(
		String name,
		String appearance,
		String description,
		double mass,
		Double tradeValue,
		Double stackSizeMax,
		String[] categoryNames,
		Function<UniverseWorldPlaceEntities,String> use,
		Visual visual,
		BiFunction<UniverseWorldPlaceEntities,Item,Entity> toEntity
	)
	{
		this.name = name;

		this.appearance = (appearance != null ? appearance : name);
		this.description = description;
		this.mass = (mass >= 0 ? mass : 1);
		this.tradeValue = tradeValue;
		this.stackSizeMax = (stackSizeMax >= 0 ? stackSizeMax : Double.POSITIVE_INFINITY);
		this.categoryNames = (categoryNames != null ? categoryNames : new String[] {});
		this._use = use;
		this.visual = visual;
		this._toEntity = toEntity;
	}

	public static ItemDefn fromName(String name)
	{
		return new ItemDefn
		(
			name,
			name, // appearance
			name, // description
			0, // mass
			0, // tradeValue
			null, // stackSizeMax
			null, // categoryNames
			null, // use
			null, // visual
			null // toEntity
		);
	}

	public static ItemDefn fromNameCategoryNameAndUse
	(
		String name,
		String categoryName,
		Function<UniverseWorldPlaceEntities,String> use
	)
	{
		var returnValue = ItemDefn.fromName(name);
		returnValue.categoryNames = new String[] { categoryName };
		returnValue._use = use;
		return returnValue;
	}

	public static ItemDefn fromNameAndUse
	(
		String name,
		Function<UniverseWorldPlaceEntities,String> use
	)
	{
		var returnValue = ItemDefn.fromName(name);
		returnValue._use = use;
		return returnValue;
	}

	public static ItemDefn fromNameMassValueAndVisual
	(
		String name, double mass, double tradeValue, Visual visual
	)
	{
		return new ItemDefn
		(
			name, null, null, mass, tradeValue, 0, null, null, visual, null
		);
	}

	public Entity toEntity(UniverseWorldPlaceEntities uwpe, Item item)
	{
		Entity returnValue;
		if (this._toEntity == null)
		{
			returnValue = new Entity(this.name, new EntityProperty[] { item } );
		}
		else
		{
			returnValue = this._toEntity.apply(uwpe, item);
		}

		return returnValue;
	}

	public Object use(UniverseWorldPlaceEntities uwpe)
	{
		Object returnValue;
		if (this._use == null)
		{
			returnValue = "Can't use " + this.appearance + ".";
		}
		else
		{
			returnValue = this._use.apply(uwpe);
		}

		return returnValue;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
	
	// Clonable.
	
	public ItemDefn clone() { return this; }
	public ItemDefn overwriteWith(ItemDefn other) { return this; }
}
