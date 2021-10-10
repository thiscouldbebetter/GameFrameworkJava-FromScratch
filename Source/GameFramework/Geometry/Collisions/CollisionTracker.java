
package GameFramework.Geometry.Collisions;

import java.util.*;

public class CollisionTracker implements EntityProperty
{
	public MapOfCells<CollisionTrackerMapCell> collisionMap;

	private _CollisionTrackerMapCell cells[];

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
			CollisionTracker.name,
			collisionMapSizeInCells,
			collisionMapCellSize,
			() -> new CollisionTrackerMapCell(),
			null, // cellAtPosInCells,
			new Array<CollisionTrackerMapCell>() // cellSource
		);

		this._cells = new Array<CollisionTrackerMapCell>();
	}

	static fromSize(Coords size): CollisionTracker
	{
		return new CollisionTracker(size, Coords.fromXY(4, 4));
	}

	public List<Collision> entityCollidableAddAndFindCollisions
	(
		Entity, entity CollisionHelper, collisionHelper Collision collisionsSoFar[]
	)
	{
		collisionsSoFar.length = 0;

		var entityBoundable = entity.boundable();
		var entityCollidable = entity.collidable();

		var entityBounds = entityBoundable.bounds;
		var cellsToAddEntityTo = this.collisionMap.cellsInBoxAddToList
		(
			entityBounds, ArrayHelper.clear(this._cells)
		);

		entityCollidable._collisionTrackerMapCellsOccupied.addAll
		(
			cellsToAddEntityTo
		);

		for (var c = 0; c < cellsToAddEntityTo.length; c++)
		{
			var cell = cellsToAddEntityTo[c];
			var cellEntitiesPresent = cell.entitiesPresent;
			if (cellEntitiesPresent.length > 0)
			{
				for (var e = 0; e < cellEntitiesPresent.length; e++)
				{
					var entityOther = cellEntitiesPresent[e];

					if (entityOther == entity)
					{
						// This shouldn't happen!
						Debug.doNothing();
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
							collisionsSoFar.push(collision);
						}
					}
				}
			}

			cellEntitiesPresent.push(entity);
		}

		return collisionsSoFar;
	}

	toEntity(): Entity
	{
		return new Entity
		(
			CollisionTracker.name, new EntityProperty[] { this }
		);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}

	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var cellsAll = (CollisionTrackerMapCell[])this.collisionMap.cellSource;
		cellsAll.forEach(x ->
		{
			x.entitiesPresent = x.entitiesPresent.filter
			(
				y -> y.collidable().isEntityStationary(y)
			)
		});
	}
}

class CollisionTrackerMapCell implements MapCell
{
	Entity entitiesPresent[];

	constructor()
	{
		this.entitiesPresent = new List<Entity>();
	}
}
