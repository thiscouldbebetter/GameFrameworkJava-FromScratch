
package GameFramework.Controls;

import GameFramework.Model.*;

public class Controllable implements EntityProperty
{
	public Object toControl;

	public Controllable(Object toControl)
	{
		this.toControl = toControl;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

}
