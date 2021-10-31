
package GameFramework.Model.Usables;

import java.util.function.*;

import GameFramework.Model.*;

public class Usable implements EntityProperty
{
	public Function<UniverseWorldPlaceEntities,String> _use;

	public boolean isDisabled;

	public Usable(Function<UniverseWorldPlaceEntities,String> use)
	{
		this._use = use;

		this.isDisabled = false;
	}

	public String use(UniverseWorldPlaceEntities uwpe)
	{
		return (this.isDisabled ? null : this._use.apply(uwpe));
	}

	// Clonable.

	public Usable clone()
	{
		return new Usable(this._use);
	}

	public Usable overwriteWith(Usable other)
	{
		this._use = other._use;
		return this;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
}
