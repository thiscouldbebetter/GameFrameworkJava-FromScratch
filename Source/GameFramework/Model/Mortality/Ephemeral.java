
package GameFramework.Model.Mortality;

import java.util.*;
import java.util.function.*;

import GameFramework.Model.*;

public class Ephemeral implements EntityProperty
{
	public int ticksToLive;
	public Consumer<UniverseWorldPlaceEntities> expire;

	public Ephemeral(int ticksToLive, Consumer<UniverseWorldPlaceEntities> expire)
	{
		this.ticksToLive = ticksToLive;
		this.expire = expire;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		this.ticksToLive--;
		if (this.ticksToLive <= 0)
		{
			var entityEphemeral = uwpe.entity;
			uwpe.place.entityToRemoveAdd(entityEphemeral);
			if (this.expire != null)
			{
				this.expire.accept(uwpe);
			}
		}
	}

	// cloneable

	public Ephemeral clone()
	{
		return new Ephemeral(this.ticksToLive, this.expire);
	}

	public EntityProperty overwriteWith(EntityProperty otherAsEntityProperty)
	{
		var other = (Ephemeral)otherAsEntityProperty;
		this.ticksToLive = other.ticksToLive;
		return this;
	}
}
