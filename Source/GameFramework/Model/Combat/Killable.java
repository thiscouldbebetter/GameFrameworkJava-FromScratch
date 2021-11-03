
package GameFramework.Model.Combat;

import java.util.function.*;

import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class Killable implements EntityProperty<Killable>
{
	public double integrityMax;
	public BiFunction<UniverseWorldPlaceEntities, Damage, Double> _damageApply;
	public Consumer<UniverseWorldPlaceEntities> _die;

	public double integrity;

	public Killable
	(
		double integrityMax,
		BiFunction<UniverseWorldPlaceEntities, Damage, Double> damageApply,
		Consumer<UniverseWorldPlaceEntities> die
	)
	{
		this.integrityMax = integrityMax;
		this._damageApply = damageApply;
		this._die = die;

		this.integrity = this.integrityMax;
	}

	public static Killable fromIntegrityMax(double integrityMax)
	{
		return new Killable(integrityMax, null, null);
	}

	public double damageApply
	(
		UniverseWorldPlaceEntities uwpe, Damage damageToApply
	)
	{
		var universe = uwpe.universe;
		var entityKillable = uwpe.entity;
		var entityDamager = uwpe.entity2;

		double damageApplied;
		if (this._damageApply == null)
		{
			var randomizer = universe.randomizer;

			damageApplied =
			(
				damageToApply == null
				? entityDamager.damager().damagePerHit.amount(randomizer)
				: damageToApply.amount(randomizer)
			);

			var killable = entityKillable.killable();
			killable.integritySubtract(damageApplied);
		}
		else
		{
			damageApplied = this._damageApply.apply(uwpe, damageToApply);
		}
		return damageApplied;
	}

	public void die(UniverseWorldPlaceEntities uwpe)
	{
		if (this._die != null)
		{
			this._die.accept(uwpe);
		}
	}

	public void integrityAdd(double amountToAdd)
	{
		this.integrity += amountToAdd;
		this.integrity = NumberHelper.trimToRangeMax
		(
			this.integrity,
			this.integrityMax
		);
	}

	public void integritySubtract(double amountToSubtract)
	{
		this.integrityAdd(0 - amountToSubtract);
	}

	public void kill()
	{
		this.integrity = 0;
	}

	public boolean isAlive()
	{
		return (this.integrity > 0);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		if (this.isAlive() == false)
		{
			var place = uwpe.place;
			var entityKillable = uwpe.entity;
			place.entityToRemoveAdd(entityKillable);
			this.die(uwpe);
		}
	}

	// cloneable

	public Killable clone()
	{
		return new Killable(this.integrityMax, this._damageApply, this._die);
	}
	
	public Killable overwriteWith(Killable other)
	{
		return this;
	}
}
