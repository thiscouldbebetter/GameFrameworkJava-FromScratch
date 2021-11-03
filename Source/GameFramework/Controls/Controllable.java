
package GameFramework.Controls;

import java.util.function.*;

import GameFramework.Model.*;

public class Controllable implements EntityProperty<Controllable>
{
	public Function<UniverseWorldPlaceEntities,ControlBase> _toControl;

	public Controllable(Function<UniverseWorldPlaceEntities,ControlBase> toControl)
	{
		this._toControl = toControl;
	}

	public ControlBase toControl(UniverseWorldPlaceEntities uwpe)
	{
		return this._toControl.apply(uwpe);
	}

	// Clonable.

	public Controllable clone()
	{
		return this; // todo
	}

	public EntityProperty overwriteWith(EntityProperty other) { return this; }

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

}
