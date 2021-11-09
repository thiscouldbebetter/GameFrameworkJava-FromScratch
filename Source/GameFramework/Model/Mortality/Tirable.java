
package GameFramework.Model.Mortality;

import java.util.function.*;

import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class Tirable implements EntityProperty<Tirable>
{
	public double staminaMaxAfterSleep;
	public double staminaRecoveredPerTick;
	public double staminaMaxLostPerTick;
	public double staminaMaxRecoveredPerTickOfSleep;
	public Consumer<UniverseWorldPlaceEntities> _fallAsleep;

	public double staminaMaxRemainingBeforeSleep;
	public double stamina;

	public Tirable
	(
		double staminaMaxAfterSleep,
		double staminaRecoveredPerTick,
		double staminaMaxLostPerTick,
		double staminaMaxRecoveredPerTickOfSleep,
		Consumer<UniverseWorldPlaceEntities> fallAsleep
	)
	{
		this.staminaMaxAfterSleep = staminaMaxAfterSleep;
		this.staminaRecoveredPerTick = staminaRecoveredPerTick;
		this.staminaMaxLostPerTick = staminaMaxLostPerTick;
		this.staminaMaxRecoveredPerTickOfSleep = staminaMaxRecoveredPerTickOfSleep;
		this._fallAsleep = fallAsleep;

		this.stamina = this.staminaMaxAfterSleep;
		this.staminaMaxRemainingBeforeSleep = this.staminaMaxAfterSleep;
	}

	public void fallAsleep(UniverseWorldPlaceEntities uwpe)
	{
		var staminaMaxToRecover =
			this.staminaMaxAfterSleep - this.staminaMaxRemainingBeforeSleep;
		var ticksToRecover = Math.ceil
		(
			staminaMaxToRecover / this.staminaMaxRecoveredPerTickOfSleep
		);
		var world = uwpe.world;
		world.timerTicksSoFar += ticksToRecover;

		if (this._fallAsleep != null)
		{
			this._fallAsleep.accept(uwpe);
		}
	}

	public boolean isExhausted()
	{
		return (this.staminaMaxRemainingBeforeSleep <= 0);
	}

	public void staminaAdd(double amountToAdd)
	{
		this.stamina += amountToAdd;
		this.stamina = NumberHelper.trimToRangeMax
		(
			this.stamina, this.staminaMaxRemainingBeforeSleep
		);
	}

	public void staminaSubtract(double amountToSubtract)
	{
		this.staminaAdd(0 - amountToSubtract);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isExhausted())
		{
			this.fallAsleep(uwpe);
		}
		else
		{
			this.staminaMaxRemainingBeforeSleep -= this.staminaMaxLostPerTick;
			this.staminaAdd(this.staminaRecoveredPerTick);
		}
	}

	// cloneable

	public Tirable clone()
	{
		return new Tirable
		(
			this.staminaMaxAfterSleep,
			this.staminaRecoveredPerTick,
			this.staminaMaxLostPerTick,
			this.staminaMaxRecoveredPerTickOfSleep,
			this._fallAsleep
		);
	}

	public Tirable overwriteWith(Tirable other) { return this; }
}
