
package GameFramework.Model.Combat;

import java.util.*;

import GameFramework.Model.Effects.*;
import GameFramework.Utility.*;

public class Damage
{
	public DiceRoll amountAsDiceRoll;
	public String typeName;
	public List<Pair<Effect,Double>> effectsAndChances;

	public Damage
	(
		DiceRoll amountAsDiceRoll,
		String typeName,
		List<Pair<Effect,Double>> effectsAndChances
	)
	{
		this.amountAsDiceRoll = amountAsDiceRoll;
		this.typeName = typeName;
		this.effectsAndChances = effectsAndChances;
	}

	public static Damage fromAmount(double amount)
	{
		var amountAsDiceRoll = DiceRoll.fromOffset(amount);
		return new Damage(amountAsDiceRoll, null, null);
	}

	public static Damage fromAmountAndTypeName(double amount, String typeName)
	{
		var amountAsDiceRoll = DiceRoll.fromOffset(amount);
		return new Damage(amountAsDiceRoll, typeName, null);
	}

	public static Damage fromAmountAsDiceRoll(DiceRoll amountAsDiceRoll)
	{
		return new Damage(amountAsDiceRoll, null, null);
	}

	public double amount(Randomizer randomizer)
	{
		var valueRolled = this.amountAsDiceRoll.roll(randomizer);
		return valueRolled;
	}

	public Effect[] effectsOccurring(Randomizer randomizer)
	{
		var effectsOccurring = new ListArray<Effect>();

		if (this.effectsAndChances != null)
		{
			for (var i = 0; i < this.effectsAndChances.length; i++)
			{
				var effectAndChance = this.effectsAndChances[i];
				var chance = effectAndChance[1];
				var roll = randomizer.getNextRandom();
				if (roll <= chance)
				{
					var effect = effectAndChance[0];
					effectsOccurring.push(effect);
				}
			}
		}

		return effectsOccurring;
	}

	public String toString()
	{
		return this.amount + " " + (this.typeName == null ? "" : this.typeName);
	}

	public DamageType type()
	{
		return DamageType.byName(this.typeName);
	}
}
