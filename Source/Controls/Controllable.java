
package Controls;

import Model.*;

import java.util.function.*;

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

	public Controllable overwriteWith(Controllable other) { return this; }

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}

}
