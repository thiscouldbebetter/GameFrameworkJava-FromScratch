
package GameFramework.Model;

import GameFramework.Utility.*;

public interface EntityProperty implements Clonable<EntityProperty>
{
	void finalize(UniverseWorldPlaceEntities uwpe);
	void initialize(UniverseWorldPlaceEntities uwpe);
	void updateForTimerTick(UniverseWorldPlaceEntities uwpe);
}
