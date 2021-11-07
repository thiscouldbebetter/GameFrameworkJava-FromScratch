
package GameFramework.Model.Physics;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Collisions.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class Collidable implements EntityProperty<Collidable>
{
	public Integer ticksToWaitBetweenCollisions;
	public ShapeBase colliderAtRest;
	public String[] entityPropertyNamesToCollideWith;
	BiConsumer<UniverseWorldPlaceEntities,Collision> _collideEntities;

	public ShapeBase collider;
	public Disposition locPrev;
	public int ticksUntilCanCollide;
	public List<Entity> entitiesAlreadyCollidedWith;
	public boolean isDisabled;

	private List<CollisionTrackerMapCell> _collisionTrackerMapCellsOccupied;
	private List<Collision> _collisions;

	public Collidable
	(
		Integer ticksToWaitBetweenCollisions,
		ShapeBase colliderAtRest,
		String[] entityPropertyNamesToCollideWith,
		BiConsumer<UniverseWorldPlaceEntities,Collision> collideEntities
	)
	{
		this.ticksToWaitBetweenCollisions = ticksToWaitBetweenCollisions;
		this.colliderAtRest = colliderAtRest;
		this.entityPropertyNamesToCollideWith =
		(
			entityPropertyNamesToCollideWith != null
			? entityPropertyNamesToCollideWith
			: new String[] {}
		);
		this._collideEntities = collideEntities;

		this.collider = (ShapeBase)this.colliderAtRest.clone();
		this.locPrev = Disposition.create();
		this.ticksUntilCanCollide = 0;
		this.entitiesAlreadyCollidedWith = new ArrayList<Entity>();
		this.isDisabled = false;

		// Helper variables.

		this._collisionTrackerMapCellsOccupied =
			new ArrayList<CollisionTrackerMapCell>();
		this._collisions = new ArrayList<Collision>();
	}

	public static Collidable fromCollider(ShapeBase colliderAtRest)
	{
		return new Collidable(null, colliderAtRest, null, null);
	}

	public static Collidable fromColliderAndCollideEntities
	(
		ShapeBase colliderAtRest,
		BiConsumer<UniverseWorldPlaceEntities,Collision> collideEntities
	)
	{
		return new Collidable(null, colliderAtRest, null, collideEntities);
	}

	public Collision collideEntities
	(
		UniverseWorldPlaceEntities uwpe, Collision collision
	)
	{
		if (this._collideEntities != null)
		{
			this._collideEntities.accept(uwpe, collision);
		}
		return collision;
	}

	public void colliderLocateForEntity(Entity entity)
	{
		this.collider.overwriteWith(this.colliderAtRest);
		this.collider.locate(entity.locatable().loc);
	}

	public void collisionHandle
	(
		UniverseWorldPlaceEntities uwpe, Collision collision
	)
	{
		var entitiesColliding = collision.entitiesColliding;
		var entity = entitiesColliding.get(0);
		var entityOther = entitiesColliding.get(1);

		uwpe.entity = entity;
		uwpe.entity2 = entityOther;

		this.collideEntities
		(
			uwpe, collision
		);

		var entityOtherCollidable = entityOther.collidable();
		entityOtherCollidable.collideEntities
		(
			uwpe.clone().entitiesSwap(), collision
		);
	}

	public void collisionsFindAndHandle(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isDisabled == false)
		{
			var entity = uwpe.entity;
			var entityLoc = entity.locatable().loc;
			this.locPrev.overwriteWith(entityLoc);

			if (this.ticksUntilCanCollide > 0)
			{
				this.ticksUntilCanCollide--;
			}
			else
			{
				this.colliderLocateForEntity(entity);

				var collisions = this.collisionsFindForEntity
				(
					uwpe, ArrayHelper.clear(this._collisions)
				);

				collisions.forEach
				(
					collision -> this.collisionHandle(uwpe, collision)
				);
			}
		}
	}

	public List<Collision> collisionsFindForEntity
	(
		UniverseWorldPlaceEntities uwpe, List<Collision> collisionsSoFar
	)
	{
		var place = uwpe.place;
		var entity = uwpe.entity;

		var collisionTracker = place.collisionTracker();
		var entityBoundable = entity.boundable();

		if
		(
			collisionTracker == null
			|| entityBoundable == null
			|| entityBoundable.bounds.getClass().getName() != Box.class.getName()
		)
		{
			collisionsSoFar = this.collisionsFindForEntity_WithoutTracker
			(
				uwpe, collisionsSoFar
			);
		}
		else
		{
			collisionsSoFar = this.collisionsFindForEntity_WithTracker
			(
				uwpe, collisionsSoFar, collisionTracker
			);
		}

		return collisionsSoFar;
	}
	
	public void collisionTrackerMapCellOccupy
	(
		CollisionTrackerMapCell cellToOccupy
	)
	{
		this._collisionTrackerMapCellsOccupied.add(cellToOccupy);
	}

	public void collisionTrackerMapCellsOccupy
	(
		List<CollisionTrackerMapCell> cellsToOccupy
	)
	{
		this._collisionTrackerMapCellsOccupied.addAll(cellsToOccupy);
	}

	public List<Collision> collisionsFindForEntity_WithTracker
	(
		UniverseWorldPlaceEntities uwpe,
		List<Collision> collisionsSoFar,
		CollisionTracker collisionTracker
	)
	{
		var universe = uwpe.universe;
		var entity = uwpe.entity;

		this._collisionTrackerMapCellsOccupied.forEach
		(
			x -> ArrayHelper.remove(x.entitiesPresent, entity)
		);
		this._collisionTrackerMapCellsOccupied.clear();

		collisionsSoFar = collisionTracker.entityCollidableAddAndFindCollisions
		(
			entity, universe.collisionHelper, collisionsSoFar
		);
		collisionsSoFar = collisionsSoFar.stream().filter
		(
			collision ->
				this.entityPropertyNamesToCollideWith.stream().anyMatch
				(
					propertyName ->
						collision.entitiesColliding.get(1).propertyByName(propertyName) != null
				)
		).collect(Collectors.toList());
		return collisionsSoFar;
	}

	public List<Collision> collisionsFindForEntity_WithoutTracker
	(
		UniverseWorldPlaceEntities uwpe,
		List<Collision> collisionsSoFar
	)
	{
		var universe = uwpe.universe;
		var place = uwpe.place;
		var entity = uwpe.entity;

		var collisionHelper = universe.collisionHelper;

		for (var p = 0; p < this.entityPropertyNamesToCollideWith.length; p++)
		{
			var entityPropertyName = this.entityPropertyNamesToCollideWith[p];
			var entitiesWithProperty = place.entitiesByPropertyName(entityPropertyName);
			if (entitiesWithProperty != null)
			{
				for (var e = 0; e < entitiesWithProperty.size(); e++)
				{
					var entityOther = entitiesWithProperty.get(e);
					if (entityOther != entity)
					{
						var doEntitiesCollide = this.doEntitiesCollide
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
		}

		return collisionsSoFar;
	}

	public boolean doEntitiesCollide
	(
		Entity entity0, Entity entity1, CollisionHelper collisionHelper
	)
	{
		var collidable0 = entity0.collidable();
		var collidable1 = entity1.collidable();

		var collidable0EntitiesAlreadyCollidedWith =
			collidable0.entitiesAlreadyCollidedWith;
		var collidable1EntitiesAlreadyCollidedWith =
			collidable1.entitiesAlreadyCollidedWith;

		var doEntitiesCollide = false;

		var canCollidablesCollideYet =
			(
				collidable0.ticksUntilCanCollide <= 0
				&& collidable1.ticksUntilCanCollide <= 0
			);

		if (canCollidablesCollideYet)
		{
			var collidable0Boundable = entity0.boundable();
			var collidable1Boundable = entity1.boundable();
			var isEitherUnboundableOrDoBoundsCollide =
			(
				collidable0Boundable == null
				|| collidable1Boundable == null
				|| collisionHelper.doCollidersCollide
					(
						collidable0Boundable.bounds, collidable1Boundable.bounds
					)
			);

			if (isEitherUnboundableOrDoBoundsCollide)
			{
				var collider0 = collidable0.collider;
				var collider1 = collidable1.collider;

				doEntitiesCollide =
					collisionHelper.doCollidersCollide(collider0, collider1);
			}
		}

		var wereEntitiesAlreadyColliding =
		(
			collidable0EntitiesAlreadyCollidedWith.indexOf(entity1) >= 0
			|| collidable1EntitiesAlreadyCollidedWith.indexOf(entity0) >= 0
		);

		if (doEntitiesCollide)
		{
			if (wereEntitiesAlreadyColliding)
			{
				doEntitiesCollide = false;
			}
			else
			{
				this.ticksUntilCanCollide = this.ticksToWaitBetweenCollisions;
				collidable0EntitiesAlreadyCollidedWith.add(entity1);
				collidable1EntitiesAlreadyCollidedWith.add(entity0);
			}
		}
		else if (wereEntitiesAlreadyColliding)
		{
			ArrayHelper.remove(collidable0EntitiesAlreadyCollidedWith, entity1);
			ArrayHelper.remove(collidable1EntitiesAlreadyCollidedWith, entity0);
		}

		return doEntitiesCollide;
	}

	public boolean isEntityStationary(Entity entity)
	{
		// This way would be better, but it causes strange glitches.
		// In the demo game, when you walk into view of three
		// of the four corners of the 'Battlefield' rooms,
		// the walls shift inward suddenly!
		//return (entity.locatable().loc.equals(this.locPrev));

		return (entity.movable() == null);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}

	public void initialize(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isEntityStationary(uwpe.entity))
		{
			this.collisionsFindAndHandle(uwpe);
		}
	}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isEntityStationary(uwpe.entity))
		{
			this.entitiesAlreadyCollidedWith.clear();
		}
		else
		{
			this.collisionsFindAndHandle(uwpe);
		}
	}

	// cloneable

	public Collidable clone()
	{
		return new Collidable
		(
			this.ticksToWaitBetweenCollisions,
			(ShapeBase)(this.colliderAtRest.clone() ),
			this.entityPropertyNamesToCollideWith,
			this._collideEntities
		);
	}

	public Collidable overwriteWith(Collidable otherAsProperty)
	{
		return this; // todo
	}
}
