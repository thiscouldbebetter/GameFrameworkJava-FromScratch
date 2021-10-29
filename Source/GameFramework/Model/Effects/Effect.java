
package GameFramework.Model.Effects;

import java.util.function.*;

import GameFramework.Display.Visuals.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class Effect implements Clonable<Effect>, Namable
{
	public String _name;
	public int ticksPerCycle;
	public int cyclesToLive;
	public Visual visual;
	public BiFunction<UniverseWorldPlaceEntities,Effect,Object> _updateForCycle;

	public int ticksSoFar;

	public Effect
	(
		String name,
		int ticksPerCycle,
		int cyclesToLive,
		Visual visual,
		BiFunction<UniverseWorldPlaceEntities,Effect,Object> updateForCycle
	)
	{
		this._name = name;
		this.ticksPerCycle = ticksPerCycle;
		this.cyclesToLive = cyclesToLive;
		this.visual = visual;
		this._updateForCycle = updateForCycle;
		this.ticksSoFar = 0;
	}

	private static Effect_Instances _instances;
	public static Effect_Instances Instances()
	{
		if (Effect._instances == null)
		{
			Effect._instances = new Effect_Instances();
		}
		return Effect._instances;
	}

	public boolean isCycleComplete()
	{
		return (this.ticksSoFar % this.ticksPerCycle == 0);
	}

	public boolean isDone()
	{
		return (this.ticksSoFar >= this.ticksToLive());
	}

	public int ticksToLive()
	{
		return this.ticksPerCycle * this.cyclesToLive;
	}

	public Object updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		Object returnValue = null;

		if (this._updateForCycle != null)
		{
			if (this.isCycleComplete())
			{
				returnValue = this._updateForCycle.apply(uwpe, this);
			}
		}

		this.ticksSoFar++;

		return returnValue;
	}

	// Clonable.

	public Effect clone()
	{
		return new Effect
		(
			this._name,
			this.ticksPerCycle,
			this.cyclesToLive,
			this.visual,
			this._updateForCycle
		);
	}
	
	public Effect overwriteWith(Effect other)
	{
		return this; // todo
	}

	// Namable.
	
	public String name()
	{
		return this._name;
	}
}
