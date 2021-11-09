
package GameFramework.Geometry.Collisions;

import java.util.*;
import java.util.stream.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Shapes.Maps.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class CollisionTracker implements EntityProperty<CollisionTracker>
{
	public MapOfCells<CollisionTrackerMapCell> collisionMap;

	private List<CollisionTrackerMapCell> _cells;

	public CollisionTracker(Coords size, Coords collisionMapSizeInCells)
	{
		collisionMapSizeInCells =
		(
			collisionMapSizeInCells != null
			? collisionMapSizeInCells
			: Coords.fromXY(1, 1).multiplyScalar(4)
		);

		var collisionMapCellSize = size.clone().divide(collisionMapSizeInCells);

		this.collisionMap = new MapOfCells
		(
			CollisionTracker.class.getName(),
			collisionMapSizeInCells,
			collisionMapCellSize,
			null, // cellCreate?
			null, // cellAtPos?
			new ArrayList<CollisionTrackerMapCell>()
		);

		this._cells = new ArrayList<CollisionTrackerMapCell>();
	}

	static CollisionTracker fromSize(Coords size)
	{
		return new CollisionTracker(size, Coords.fromXY(4, 4));
	}

	public List<Collision> entityCollidableAddAndFindCollisions
	(
		Entity entity,
		CollisionHelper collisionHelper,
		List<Collision> collisionsSoFar
	)
	{
		collisionsSoFar.clear();

		var entityBoundable = entity.boundable();
		var entityCollidable = entity.collidable();

		var entityBounds = (Box)(entityBoundable.bounds);
		var cellsToAddEntityTo = this.collisionMap.cellsInBoxAddToList
		(
			entityBounds, ArrayHelper.clear(this._cells)
		);

		entityCollidable.collisionTrackerMapCellsOccupy
		(
			cellsToAddEntityTo
		);

		for (var c = 0; c < cellsToAddEntityTo.size(); c++)
		{
			var cell = cellsToAddEntityTo.get(c);
			var cellEntitiesPresent = cell.entitiesPresent;
			if (cellEntitiesPresent.size() > 0)
			{
				for (var e = 0; e < cellEntitiesPresent.size(); e++)
				{
					var entityOther = cellEntitiesPresent.get(e);

					if (entityOther == entity)
					{
						// This shouldn't happen!
						//Debug.doNothing();
						throw new Exception("todo");
					}
					else
					{
						var doEntitiesCollide = entityCollidable.doEntitiesCollide
						(
							entity, entityOther, collisionHelper
						);

						if (doEntitiesCollide)
						{
							var collision = collisionHelper.collisionOfEntities
							(
								entity, entityOther, Collision.create()
							);
							collisionsSoFar.add(collision);
						}
					}
				}
			}

			cellEntitiesPresent.add(entity);
		}

		return collisionsSoFar;
	}

	public Entity toEntity()
	{
		return new Entity
		(
			CollisionTracker.class.getName(), new EntityProperty[] { this }
		);
	}

	// Clonable.

	public CollisionTracker clone() { return this; } // todo

	public CollisionTracker overwriteWith(CollisionTracker other) { return this; } // todo

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}

	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var cellsAll = this.collisionMap.cellSource;
		cellsAll.forEach(x ->
		{
			x.entitiesPresent = x.entitiesPresent.stream().filter
			(
				y -> y.collidable().isEntityStationary(y)
			).collect
			(
				Collectors.toList()
			);
		});
	}
}
