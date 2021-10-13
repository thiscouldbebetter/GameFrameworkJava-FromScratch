
package GameFramework.Helpers;

public class NumberHelper
{
	// static class

	public static double isInRangeMinMax(double n, double min, double max)
	{
		return (n >= min && n <= max);
	}

	public static double reflectNumberOffRange
	(
		double doubleToReflect, double rangeMin, double rangeMax
	)
	{
		while (doubleToReflect < rangeMin)
		{
			doubleToReflect = rangeMin + rangeMin - doubleToReflect;
		}

		while (doubleToReflect > rangeMax)
		{
			doubleToReflect = rangeMax - (doubleToReflect - rangeMax);
		}

		return NumberHelper.trimToRangeMinMax(doubleToReflect, rangeMin, rangeMax);
	}

	public static double roundToDecimalPlaces(double n, double doubleOfPlaces)
	{
		var multiplier = Math.pow(10, doubleOfPlaces);
		return Math.round(n * multiplier) / multiplier;
	}

	public static double subtractWrappedToRangeMax
	(
		double n, double subtrahend, double max
	)
	{
		var differenceUnwrapped = n - subtrahend;
		var differenceAbsolute = Math.abs(differenceUnwrapped);
		var differenceAbsoluteLeastSoFar = differenceAbsolute;
		var returnValue = differenceUnwrapped;

		for (var i = -1; i <= 1; i += 2)
		{
			var differenceWrapped = differenceUnwrapped + max * i;
			differenceAbsolute = Math.abs(differenceWrapped);
			if (differenceAbsolute < differenceAbsoluteLeastSoFar)
			{
				differenceAbsoluteLeastSoFar = differenceAbsolute;
				returnValue = differenceWrapped;
			}
		}

		return returnValue;
	}

	public static double trimToRangeMax(double n, double max)
	{
		return NumberHelper.trimToRangeMinMax(n, 0, max);
	}

	public static double trimToRangeMinMax(double n, double min, double max)
	{
		var value = n;

		if (value < min)
		{
			value = min;
		}
		else if (value > max)
		{
			value = max;
		}

		return value;
	}

	public static double wrapToRangeMax(double n, double max)
	{
		return NumberHelper.wrapToRangeMinMax(n, 0, max);
	}

	public static double wrapToRangeMinMax(double n, double min, double max)
	{
		var value = n;

		var rangeSize = max - min;

		if (rangeSize == 0)
		{
			value = min;
		}
		else
		{
			while (value < min)
			{
				value += rangeSize;
			}

			while (value >= max)
			{
				value -= rangeSize;
			}
		}

		return value;
	}

	public static double wrapToRangeZeroOne(double n)
	{
		return NumberHelper.wrapToRangeMinMax(n, 0, 1);
	}
}
