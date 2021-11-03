
package GameFramework.Model;

import java.util.function.*;

import GameFramework.Model.*;

public class Selectable implements EntityProperty<Selectable>
{
	private Consumer<UniverseWorldPlaceEntities> _select;
	private Consumer<UniverseWorldPlaceEntities> _deselect;

	public Selectable
	(
		Consumer<UniverseWorldPlaceEntities> select,
		Consumer<UniverseWorldPlaceEntities> deselect
	)
	{
		this._select = select;
		this._deselect = deselect;
	}

	public void deselect(UniverseWorldPlaceEntities uwpe)
	{
		if (this._deselect != null)
		{
			this._deselect(uwpe);
		}
	}

	public void select(UniverseWorldPlaceEntities uwpe)
	{
		if (this._select != null)
		{
			this._select(uwpe);
		}
	}

	// Clonable.

	public Selectable clone()
	{
		return new Selectable(this._select, this._deselect);
	}

	public Selectable overwriteWith(Selectable other)
	{
		this._select = other._select;
		this._deselect = other._deselect;
		return this;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
}
