
package GameFramework.Controls;

import java.util.function.*;

import GameFramework.Model.*;

public class Controllable implements EntityProperty
{
	public Function<UniverseWorldPlaceEntities,ControlBase> _toControl;

	public Controllable(Function<UniverseWorldPlaceEntities,Control> toControl)
	{
		this._toControl = toControl;
	}

	public ControlBase toControl(UniverseWorldPlaceEntities uwpe)
	{
		return this._toControl.apply(uwpe);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

}
