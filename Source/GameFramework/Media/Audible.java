
package GameFramework.Media;

import GameFramework.Model.*;

public class Audible implements EntityProperty
{
	public boolean hasBeenHeard;

	public Audible()
	{
		this.hasBeenHeard = false;
	}

	// Cloneable

	public Audible clone()
	{
		return new Audible();
	}

	public Audible overwriteWith(Audible other)
	{
		this.hasBeenHeard = other.hasBeenHeard;
		return this;
	}

	// EntityProperty.
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
}
