
package GameFramework.Model.Mortality;

import java.util.function.*;

import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class Starvable implements EntityProperty<Starvable>
{
	public double satietyMax;
	public double satietyLostPerTick;
	public Consumer<UniverseWorldPlaceEntities> _starve;

	public double satiety;

	public Starvable
	(
		double satietyMax,
		double satietyLostPerTick,
		Consumer<UniverseWorldPlaceEntities> starve
	)
	{
		this.satietyMax = satietyMax;
		this.satietyLostPerTick = satietyLostPerTick;
		this._starve = starve;

		this.satiety = this.satietyMax;
	}

	public static Starvable fromSatietyMax(double satietyMax)
	{
		return new Starvable(satietyMax, 1, null);
	}

	public void starve(UniverseWorldPlaceEntities uwpe)
	{
		if (this._starve != null)
		{
			this._starve.accept(uwpe);
		}
	}

	public void satietyAdd(double amountToAdd)
	{
		this.satiety += amountToAdd;
		this.satiety = NumberHelper.trimToRangeMax
		(
			this.satiety, this.satietyMax
		);
	}

	public void satietySubtract(double amountToSubtract)
	{
		this.satietyAdd(0 - amountToSubtract);
	}

	public boolean isStarving()
	{
		return (this.satiety <= 0);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		if (this.isStarving())
		{
			this.starve(uwpe);
		}
		else
		{
			this.satiety -= this.satietyLostPerTick;
			if (this.satiety < 0)
			{
				this.satiety = 0;
			}
		}
	}

	// cloneable

	public Starvable clone()
	{
		return new Starvable
		(
			this.satietyMax, this.satietyLostPerTick, this._starve
		);
	}
}
