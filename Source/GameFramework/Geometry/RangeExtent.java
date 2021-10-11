
package GameFramework.Geometry;

import java.util.*;

public class RangeExtent
{
	public double min;
	public double max;

	public RangeExtent(double min, double max)
	{
		this.min = min;
		this.max = max;
	}

	public static RangeExtent create()
	{
		return new RangeExtent(0, 0);
	}

	private static RangeExtent_Instances _instances;
	public static RangeExtent_Instances Instances()
	{
		if (RangeExtent._instances == null)
		{
			RangeExtent._instances = new RangeExtent_Instances();
		}
		return RangeExtent._instances;
	}

	public RangeExtent clone()
	{
		return new RangeExtent(this.min, this.max);
	}

	public boolean contains(double valueToCheck)
	{
		return (valueToCheck >= this.min && valueToCheck <= this.max);
	}

	public RangeExtent intersectWith(RangeExtent other)
	{
		this.min = (this.min >= other.min ? this.min : other.min);
		this.max = (this.max <= other.max ? this.max : other.max);
		return this;
	}

	public double midpoint()
	{
		return (this.min + this.max) / 2;
	}

	public double[] minAndMax()
	{
		return new double[] { this.min, this.max };
	}

	public boolean overlapsWith(RangeExtent other)
	{
		var returnValue =
		(
			this.min < other.max
			&& this.max > other.min
		);

		return returnValue;
	}

	public RangeExtent overwriteWith(RangeExtent other)
	{
		this.min = other.min;
		this.max = other.max;
		return this;
	}

	public RangeExtent overwriteWithMinAndMax(double min, double max)
	{
		this.min = min;
		this.max = max;
		return this;
	}

	public double random(Randomizer randomizer)
	{
		return this.min + (this.max - this.min) * randomizer.getNextRandom();
	}

	public double size()
	{
		return this.max - this.min;
	}

	public List<RangeExtent> subtract(RangeExtent other)
	{
		var returnValues = new ArrayList<RangeExtent>();

		if (this.overlapsWith(other))
		{
			if (this.min <= other.min)
			{
				var segment = new RangeExtent(this.min, other.min);
				returnValues.add(segment);
			}

			if (this.max >= other.max)
			{
				var segment = new RangeExtent(other.max, this.max);
				returnValues.add(segment);
			}
		}
		else
		{
			returnValues.add(this);
		}

		return returnValues;
	}

	public double trimValue(double valueToTrim)
	{
		if (valueToTrim < this.min)
		{
			valueToTrim = this.min;
		}
		else if (valueToTrim > this.max)
		{
			valueToTrim = this.max;
		}
		return valueToTrim;
	}

	public boolean touches(RangeExtent other)
	{
		var returnValue =
		(
			this.min <= other.max
			&& this.max >= other.min
		);

		return returnValue;
	}

	public double wrapValue(double valueToWrap)
	{
		var returnValue = valueToWrap;

		var size = this.size();

		while (returnValue < this.min)
		{
			returnValue += size;
		}
		while (returnValue > this.max)
		{
			returnValue -= size;
		}

		return returnValue;
	}
}

class RangeExtent_Instances
{
	public RangeExtent ZeroToOne;

	public RangeExtent_Instances()
	{
		this.ZeroToOne = new RangeExtent(0, 1);
	}
}
