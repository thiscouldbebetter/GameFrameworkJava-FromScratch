
package Model;

public class UniverseWorldPlaceEntities
{
	public Universe universe;
	public World world;
	public Place place;
	public Entity entity; // Usually the entity acting.
	public Entity entity2; // Usually the entity acted upon.

	public UniverseWorldPlaceEntities
	(
		Universe universe, World world, Place place, Entity entity,
		Entity entity2
	)
	{
		this.universe = universe;
		this.world = world;
		this.place = place;
		this.entity = entity;
		this.entity2 = entity2;
	}

	public static UniverseWorldPlaceEntities create()
	{
		return new UniverseWorldPlaceEntities(null, null, null, null, null);
	}

	public static UniverseWorldPlaceEntities fromUniverse(Universe universe)
	{
		return new UniverseWorldPlaceEntities(universe, null, null, null, null);
	}

	public static UniverseWorldPlaceEntities fromUniverseAndWorld
	(
		Universe universe, World world
	)
	{
		return new UniverseWorldPlaceEntities(universe, world, null, null, null);
	}

	public static UniverseWorldPlaceEntities fromUniverseWorldAndPlace
	(
		Universe universe, World world, Place place
	)
	{
		return new UniverseWorldPlaceEntities(universe, world, place, null, null);
	}

	public UniverseWorldPlaceEntities entitiesSet(Entity entity, Entity entity2)
	{
		this.entity = entity;
		this.entity2 = entity2;
		return this;
	}

	public UniverseWorldPlaceEntities entitiesSwap()
	{
		var temp = this.entity;
		this.entity = this.entity2;
		this.entity2 = temp;
		return this;
	}

	public UniverseWorldPlaceEntities entitySet(Entity value)
	{
		this.entity = value;
		return this;
	}

	public UniverseWorldPlaceEntities entity2Set(Entity value)
	{
		this.entity2 = value;
		return this;
	}

	public UniverseWorldPlaceEntities fieldsSet
	(
		Universe universe, World world, Place place,
		Entity entity, Entity entity2
	)
	{
		this.universe = universe;
		this.world = world;
		this.place = place;
		this.entity = entity;
		this.entity2 = entity2;
		return this;
	}

	public UniverseWorldPlaceEntities placeSet(Place value)
	{
		this.place = value;
		return this;
	}

	public UniverseWorldPlaceEntities worldSet(World value)
	{
		this.world = value;
		return this;
	}

	// Clonable.

	public UniverseWorldPlaceEntities clone()
	{
		return new UniverseWorldPlaceEntities
		(
			this.universe, this.world, this.place, this.entity, this.entity2
		);
	}

	public UniverseWorldPlaceEntities overwriteWith(UniverseWorldPlaceEntities other)
	{
		this.universe = other.universe;
		this.world = other.world;
		this.place = other.place;
		this.entity = other.entity;
		this.entity2 = other.entity2;
		return this;
	}

}
