
package GameFramework.Model.Combat;

import GameFramework.Model.*;

public class Damager implements EntityProperty<Damager>
{
	public Damage damagePerHit;

	public Damager(Damage damagePerHit)
	{
		this.damagePerHit = damagePerHit;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
}

}
