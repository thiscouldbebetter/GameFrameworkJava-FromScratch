package Model;

import Display.*;

public class Place
{
	public String name;
	public Entity[] entities;

	public Place(String name, Entity[] entities)
	{
		this.name = name;
		this.entities = entities;
	}

	public void draw(Universe universe, World world, Display display)
	{
		var uwpe =
			UniverseWorldPlaceEntities.fromUniverseAndWorld(universe, world);

		for (var i = 0; i < this.entities.length; i++)
		{
			var entity = this.entities[i];
			uwpe.entitySet(entity);
			var drawable = entity.drawable();
			if (drawable != null)
			{
				drawable.updateForTimerTick(uwpe);
			}
		}
	}

	public void entitySpawn(UniverseWorldPlaceEntities uwpe)
	{
		// todo
	}

	public void updateForTimerTick(Universe universe, World world)
	{
		var uwpe = UniverseWorldPlaceEntities.fromUniverseWorldAndPlace
		(
			universe, world, this
		);

		for (var i = 0; i < this.entities.length; i++)
		{
			var entity = this.entities[i];
			// todo - Exclude .drawable() property, as it's handled in .draw().
			entity.updateForTimerTick(uwpe);
		}
	}
}
