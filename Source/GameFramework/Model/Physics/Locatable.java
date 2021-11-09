
package GameFramework.Model.Physics;

import java.util.*;
import java.util.stream.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Model.*;

public class Locatable implements EntityProperty<Locatable>
{
	public Disposition loc;

	public Locatable(Disposition loc)
	{
		this.loc = (loc != null ? loc : Disposition.create() );
	}

	public static Locatable create()
	{
		return new Locatable(null);
	}

	public static Locatable fromPos(Coords pos)
	{
		return new Locatable(Disposition.fromPos(pos));
	}

	public static List<Entity> entitiesSortByZThenY(List<Entity> entitiesToSort)
	{
		entitiesToSort = entitiesToSort.stream().sorted
		(
			(Entity a, Entity b) ->
			{
				var aPos = a.locatable().loc.pos;
				var bPos = b.locatable().loc.pos;
				double difference;
				if (aPos.z != bPos.z)
				{
					difference = bPos.z - aPos.z;
				}
				else
				{
					difference = aPos.y - bPos.y;
				}
				var returnValue = (int)(1000000 * difference);

				return returnValue;
			}
		).collect(Collectors.toList());

		return entitiesToSort;
	}

	public double approachOtherWithAccelerationAndSpeedMax
	(
		Locatable locatableToApproach,
		double accelerationPerTick,
		double speedMax// ,number distanceMin
	)
	{
		accelerationPerTick = accelerationPerTick ;
		speedMax = speedMax;
		//distanceMin = distanceMin || 1;

		var targetLoc = locatableToApproach.loc;
		var targetPos = targetLoc.pos;

		var actorLoc = this.loc;
		var actorPos = actorLoc.pos;
		var actorOri = actorLoc.orientation;
		var actorVel = actorLoc.vel;

		var targetPosRelative = targetPos.clone().subtract(actorPos);
		var distanceToTarget = targetPosRelative.magnitude();


		actorVel.trimToMagnitudeMax(speedMax);

		// hack
		var ticksToApproach =
			Math.sqrt(2 * distanceToTarget / accelerationPerTick);
		var targetVelRelative = targetLoc.vel.clone().subtract(actorVel);
		var targetPosRelativeProjected = targetVelRelative.multiplyScalar
		(
			ticksToApproach
		).add
		(
			targetPosRelative
		);

		actorLoc.accel.overwriteWith
		(
			targetPosRelativeProjected
		).normalize().multiplyScalar(accelerationPerTick).clearZ();

		actorOri.forwardSet(actorLoc.accel.clone().normalize());

		return distanceToTarget;
	}

	public double distanceFromEntity(Entity entity)
	{
		return this.distanceFromPos(entity.locatable().loc.pos);
	}

	public double distanceFromPos(Coords posToCheck)
	{
		return this.loc.pos.clone().subtract(posToCheck).magnitude();
	}

	public Entity entitySpawnWithDefnName
	(
		UniverseWorldPlaceEntities uwpe, String entityToSpawnDefnName
	)
	{
		var world = uwpe.world;
		var place = uwpe.place;
		var entitySpawning = uwpe.entity;

		var entityDefnToSpawn =
			world.defn.entityDefnByName(entityToSpawnDefnName);
		var entityToSpawn = entityDefnToSpawn.clone();
		var loc = entityToSpawn.locatable().loc;
		loc.overwriteWith(entitySpawning.locatable().loc);
		loc.accel.clear();
		loc.vel.clear();
		place.entitySpawn(uwpe);
		return entityToSpawn;
	}

	// EntityProperty.

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var loc = this.loc;

		loc.vel.add(loc.accel);
		loc.accel.clear();
		loc.pos.add(loc.vel);

		var spin = loc.spin;
		if (spin.angleInTurns() != 0)
		{
			loc.spin.transformOrientation(loc.orientation);
		}
	}

	// Clonable.

	public Locatable clone()
	{
		return new Locatable(this.loc.clone());
	}

	public Locatable overwriteWith(Locatable other)
	{
		this.loc.overwriteWith(other.loc);
		return this;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

}
