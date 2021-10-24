
package GameFramework.Model.Items.Equipment;

import java.util.function.*;

import GameFramework.Model.*;

public class Equippable implements EntityProperty
{
	public Consumer<UniverseWorldPlaceEntities> _equip;
	public Consumer<UniverseWorldPlaceEntities> _unequip;

	public boolean isEquipped;

	public Equippable
	(
		Consumer<UniverseWorldPlaceEntities> equip,
		Consumer<UniverseWorldPlaceEntities> unequip
	)
	{
		this._equip = equip;
		this._unequip = unequip;
		this.isEquipped = false;
	}

	public static Equippable _default()
	{
		return new Equippable(null, null);
	}

	public void equip(UniverseWorldPlaceEntities uwpe)
	{
		if (this._equip != null)
		{
			this._equip(uwpe);
		}
		this.isEquipped = true;
	}

	public void unequip(UniverseWorldPlaceEntities uwpe)
	{
		if (this._unequip != null)
		{
			this._unequip(uwpe);
		}
		this.isEquipped = false;
	}

	// Clonable.

	public Equippable clone(): Equippable
	{
		return this;
	}

	public Equippable overwriteWith(Equippable other)
	{
		return this;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
}

}
