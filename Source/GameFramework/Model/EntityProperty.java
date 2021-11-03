
package GameFramework.Model;

import GameFramework.Utility.*;

public interface EntityProperty<T extends Clonable<T>> extends Clonable<T>
{
	void finalize(UniverseWorldPlaceEntities uwpe);
	void initialize(UniverseWorldPlaceEntities uwpe);
	void updateForTimerTick(UniverseWorldPlaceEntities uwpe);
}
