
package GameFramework.Model.Combat;

import GameFramework.Model.*;

public class Damager implements EntityProperty<Damager>
{
	public Damage damagePerHit;

	public Damager(Damage damagePerHit)
	{
		this.damagePerHit = damagePerHit;
	}

	// Clonable.

	public Damager clone() { return this; }

	public Damager overwriteWith(Damager other) { return this; }

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

}
