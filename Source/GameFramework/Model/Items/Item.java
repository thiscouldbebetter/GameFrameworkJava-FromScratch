
package GameFramework.Model.Items;

import GameFramework.Model.*;

public class Item implements EntityProperty
{
	public String defnName;
	public double quantity;

	private Entity _entity;

	public Item(String defnName, double quantity)
	{
		this.defnName = defnName;
		this.quantity = quantity;
	}

	public ItemDefn defn(World world)
	{
		return world.defn.itemDefnByName(this.defnName);
	}

	public boolean isUsable(World world)
	{
		return true; // hack
	}

	public double mass(World world)
	{
		return this.quantity * this.defn(world).mass;
	}

	public Entity toEntity(UniverseWorldPlaceEntities uwpe)
	{
		if (this._entity == null)
		{
			var defn = this.defn(uwpe.world);
			this._entity = defn.toEntity(uwpe, this);
		}
		return this._entity;
	}

	public String toString(World world)
	{
		return this.defn(world).appearance + " (" + this.quantity + ")";
	}

	public double tradeValue(World world)
	{
		return this.quantity * this.defn(world).tradeValue;
	}

	public Object use(UniverseWorldPlaceEntities uwpe)
	{
		var defn = this.defn(uwpe.world);
		var returnValue = defn.use
		(
			uwpe
		);
		return returnValue;
	}

	// cloneable

	public Item clone()
	{
		return new Item(this.defnName, this.quantity);
	}
	
	public Item overwriteWith(Item other) { return this; }

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
}
