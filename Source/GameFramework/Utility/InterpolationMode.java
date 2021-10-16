
package GameFramework.Utility;

import java.util.function.*;

public class InterpolationMode
{
	public Function<Double,Double> _fractionAdjust;

	public InterpolationMode(Function<Double,Double> fractionAdjust)
	{
		this._fractionAdjust = fractionAdjust;
	}

	public double fractionAdjust(double fractionToAdjust)
	{
		var fractionAdjusted = this._fractionAdjust.apply(fractionToAdjust);
		return fractionAdjusted;
	}
}

