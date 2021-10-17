
package GameFramework.Utility;

public class RandomizerSystem implements Randomizer
{
	// Uses the built-in JavaScript randomizer.

	private static RandomizerSystem _instance;
	public static RandomizerSystem Instance()
	{
		if (RandomizerSystem._instance == null)
		{
			RandomizerSystem._instance = new RandomizerSystem();
		}
		return RandomizerSystem._instance;
	}

	public double getNextRandom()
	{
		return Math.random();
	}
}
