
package GameFramework.Model.Physics;

import GameFramework.Model.*;

public class Traversable implements EntityProperty<Traversable>
{
	public boolean isBlocking;

	public Traversable(boolean isBlocking)
	{
		this.isBlocking = isBlocking;
	}

	// Clonable.

	public Traversable clone() { return this; }
	public Traversable overwriteWith(Traversable other) { return this; }

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe){}
	public void initialize(UniverseWorldPlaceEntities uwpe){}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe){}
}
