
package GameFramework.Model.Physics;

import java.util.function.*;

import GameFramework.Geometry.*;
import GameFramework.Model.*;
import GameFramework.Model.Actors.*;

public class Movable implements EntityProperty
{
	public double accelerationPerTick;
	public double speedMax;
	public BiConsumer<UniverseWorldPlaceEntities,Double> _accelerate;

	public Movable
	(
		double accelerationPerTick,
		double speedMax,
		BiConsumer<UniverseWorldPlaceEntities,Double> accelerate
	)
	{
		this.accelerationPerTick = accelerationPerTick;
		this.speedMax = speedMax;
		this._accelerate = accelerate;
	}

	public static Movable _default()
	{
		return new Movable(.1, 3, null);
	}

	public static Movable fromAccelerationAndSpeedMax
	(
		double accelerationPerTick, double speedMax
	)
	{
		return new Movable(accelerationPerTick, speedMax, null);
	}

	public void accelerate
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		if (this._accelerate == null)
		{
			this.accelerateForward(uwpe);
		}
		else
		{
			this._accelerate.accept(uwpe, this.accelerationPerTick);
		}
	}

	public void accelerateForward(UniverseWorldPlaceEntities uwpe)
	{
		var entityMovable = uwpe.entity;
		var entityLoc = entityMovable.locatable().loc;
		entityLoc.accel.overwriteWith
		(
			entityLoc.orientation.forward
		).multiplyScalar
		(
			this.accelerationPerTick
		);
	}

	public void accelerateInDirection
	(
		UniverseWorldPlaceEntities uwpe, Coords directionToMove
	)
	{
		var entity = uwpe.entity;
		var entityLoc = entity.locatable().loc;
		var isEntityStandingOnGround =
			(entityLoc.pos.z >= 0 && entityLoc.vel.z >= 0);
		if (isEntityStandingOnGround)
		{
			entityLoc.orientation.forwardSet(directionToMove);
			entity.movable().accelerate(uwpe);
		}
	}

	// Clonable.

	public Movable clone()
	{
		return new Movable
		(
			this.accelerationPerTick,
			this.speedMax,
			this._accelerate
		);
	}

	public Movable overwriteWith(Movable other)
	{
		this.accelerationPerTick = other.accelerationPerTick;
		this.speedMax = other.speedMax;
		this._accelerate = other._accelerate;
		return this;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

	// Actions.

	public static ActorAction actionAccelerateDown()
	{
		return new ActorAction
		(
			"AccelerateDown",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var actor = uwpe.entity;
				actor.movable().accelerateInDirection
				(
					uwpe, Coords.Instances().ZeroOneZero
				);
			}
		);
	}

	public static ActorAction actionAccelerateLeft()
	{
		return new ActorAction
		(
			"AccelerateLeft",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var actor = uwpe.entity;
				actor.movable().accelerateInDirection
				(
					uwpe, Coords.Instances().MinusOneZeroZero
				);
			}
		);
	}

	public static ActorAction actionAccelerateRight()
	{
		return new ActorAction
		(
			"AccelerateRight",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var actor = uwpe.entity;
				actor.movable().accelerateInDirection
				(
					uwpe, Coords.Instances().OneZeroZero
				);
			}
		);
	}

	public static ActorAction actionAccelerateUp()
	{
		return new ActorAction
		(
			"AccelerateUp",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var actor = uwpe.entity;
				actor.movable().accelerateInDirection
				(
					uwpe, Coords.Instances().ZeroMinusOneZero
				);
			}
		);
	}

	// Activities.

	public static ActivityDefn activityDefnWanderBuild()
	{
		var returnValue = new ActivityDefn
		(
			"Wander",
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var entityActor = uwpe.entity;

				var actor = entityActor.actor();
				var activity = actor.activity;
				var targetEntity = ((Entity)(activity.target()));
				if (targetEntity == null)
				{
					var place = uwpe.place;
					var randomizer = uwpe.universe.randomizer;

					var targetPos = Coords.create().randomize
					(
						randomizer
					).multiply
					(
						place.size
					);

					targetEntity = new Entity
					(
						"Target", new EntityProperty[] { Locatable.fromPos(targetPos) }
					);
					activity.targetSet(targetEntity);
				}

				var movable = entityActor.movable();
				var actorLocatable = entityActor.locatable();
				var targetLocatable = targetEntity.locatable();
				var distanceToTarget =
					actorLocatable.approachOtherWithAccelerationAndSpeedMax
					(
						targetLocatable,
						movable.accelerationPerTick,
						movable.speedMax
					);

				if (distanceToTarget < movable.speedMax)
				{
					activity.targetSet(null);
				}
			}
		);

		return returnValue;
	}
}
