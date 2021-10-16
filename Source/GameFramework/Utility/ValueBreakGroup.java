
package GameFramework.Utility;

public class ValueBreakGroup<T extends Interpolatable<T>>
{
	ValueBreak<T>[] stops;
	InterpolationMode interpolationMode;

	public ValueBreakGroup(ValueBreak<T>[] stops, InterpolationMode interpolationMode)
	{
		this.stops = stops;
		this.interpolationMode = interpolationMode;
	}

	public T valueAtPosition(double positionToCheck)
	{
		T returnValue = null;

		var stopPrev = this.stops[0];
		ValueBreak stop = null;

		for (var i = 1; i < this.stops.length; i++)
		{
			stop = this.stops[i];
			if
			(
				positionToCheck >= stopPrev.position
				&& positionToCheck <= stop.position
			)
			{
				break;
			}
			stopPrev = stop;
		}

		if (this.interpolationMode != null)
		{
			var stopPrevValue = (T)(stopPrev.value);
			var stopValue = (T)(stop.value);
			var positionOfStopThisMinusPrev = stop.position - stopPrev.position;
			var positionToCheckMinusStopPrev = positionToCheck - stopPrev.position;
			var fraction = positionToCheckMinusStopPrev / positionOfStopThisMinusPrev;
			fraction = this.interpolationMode.fractionAdjust(fraction);
			var valueInterpolated = stopPrevValue.interpolateWith(stopValue, fraction);
			returnValue = valueInterpolated;
		}
		else
		{
			returnValue = stopPrev.value;
		}

		return returnValue;
	}
}
