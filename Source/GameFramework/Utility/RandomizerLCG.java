
package GameFramework;

public class RandomizerLCG implements Randomizer
{
	// "LCG" = "Linear Congruential Generator"

	private double multiplier;
	private double addend;
	private double modulus;
	public double currentRandom;

	public RandomizerLCG
	(
		double firstRandom, double multiplier, double addend, double modulus
	)
	{
		this.currentRandom = firstRandom;
		this.multiplier = (multiplier != null ? multiplier : 1103515245);
		this.addend = (multiplier != null ? addend : 12345);
		this.modulus = (modulus != null ? modulus : Math.pow(2.0, 31));
	}

	public static RandomizerLCG _default()
	{
		return new RandomizerLCG
		(
			0.12345, // firstRandom
			1103515245, // multiplier
			12345, // addend
			Math.pow(2.0, 31) // modulus
		);
	}

	public double getNextRandom()
	{
		this.currentRandom =
		(
			(
				this.multiplier
				* (this.currentRandom * this.modulus)
				+ this.addend
			)
			% this.modulus
		)
		/ this.modulus;

		return this.currentRandom;
	}
}
