
package GameFramework.Geometry;

import GameFramework.Helpers.*;
import GameFramework.Utility.*;

public class Coords
{
	public double x;
	public double y;
	public double z;

	public Coords(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// constants

	public static int NumberOfDimensions = 3;

	// instances

	public static Coords_Instances _instances;
	public static Coords_Instances Instances()
	{
		if (Coords._instances == null)
		{
			Coords._instances = new Coords_Instances();
		}
		return Coords._instances;
	}

	// Static methods.

	public static Coords create()
	{
		return new Coords(0, 0, 0);
	}

	public static Coords _default()
	{
		// Same as create().
		return new Coords(0, 0, 0);
	}

	public static Coords fromXY(double x, double y)
	{
		return new Coords(x, y, 0);
	}

	public static Coords ones()
	{
		return new Coords(1, 1, 1);
	}

	public static Coords random(Randomizer randomizer)
	{
		return Coords._default().randomize(randomizer);
	}

	public static Coords twos()
	{
		return new Coords(2, 2, 2);
	}

	public static Coords zeroes()
	{
		// Same as create().
		return new Coords(0, 0, 0);
	}

	// Instance methods.

	public Coords absolute()
	{
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		this.z = Math.abs(this.z);
		return this;
	}

	public Coords add(Coords other)
	{
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	public Coords addDimensions(double x, double y, double z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Coords addXY(double x, double y)
	{
		this.x += x;
		this.y += y;
		return this;
	}

	public Coords ceiling()
	{
		this.x = Math.ceil(this.x);
		this.y = Math.ceil(this.y);
		this.z = Math.ceil(this.z);
		return this;
	}

	public Coords clear()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		return this;
	}

	public Coords clearZ()
	{
		this.z = 0;
		return this;
	}

	public Coords clone()
	{
		return new Coords(this.x, this.y, this.z);
	}

	public Coords crossProduct(Coords other)
	{
		return this.overwriteWithDimensions
		(
			this.y * other.z - this.z * other.y,
			this.z * other.x - this.x * other.z,
			this.x * other.y - this.y * other.x
		);
	}

	public double dimensionGet(double dimensionIndex)
	{
		double returnValue;

		if (dimensionIndex == 0)
		{
			returnValue = this.x;
		}
		else if (dimensionIndex == 1)
		{
			returnValue = this.y;
		}
		else if (dimensionIndex == 2)
		{
			returnValue = this.z;
		}
		else
		{
			//throw new Exception("Unrecognized dimension index: " + dimensionIndex);
			return Double.NaN;
		}

		return returnValue;
	}

	public Coords dimensionSet(double dimensionIndex, double valueToSet)
	{
		if (dimensionIndex == 0)
		{
			this.x = valueToSet;
		}
		else if (dimensionIndex == 1)
		{
			this.y = valueToSet;
		}
		else if (dimensionIndex == 2)
		{
			this.z = valueToSet;
		}

		return this;
	}

	public double[] dimensions()
	{
		return new double[] { this.x, this.y, this.z };
	}

	public Coords directions()
	{
		if (this.x < 0)
		{
			this.x = -1;
		}
		else if (this.x > 0)
		{
			this.x = 1;
		}

		if (this.y < 0)
		{
			this.y = -1;
		}
		else if (this.y > 0)
		{
			this.y = 1;
		}

		if (this.z < 0)
		{
			this.z = -1;
		}
		else if (this.z > 0)
		{
			this.z = 1;
		}

		return this;
	}

	public Coords divide(Coords other)
	{
		this.x /= other.x;
		this.y /= other.y;
		this.z /= other.z;
		return this;
	}

	public Coords divideScalar(double scalar)
	{
		this.x /= scalar;
		this.y /= scalar;
		this.z /= scalar;
		return this;
	}

	public double dotProduct(Coords other)
	{
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	public Coords doublify()
	{
		return this.multiplyScalar(2);
	}

	public boolean equals(Coords other)
	{
		return (this.x == other.x && this.y == other.y && this.z == other.z);
	}

	public boolean equalsWithinError(Coords other, double errorMax)
	{
		// Because in JavaScript, 1.1 - 1.0 = 0.10000000000000009.
		return (this.clone().subtract(other).magnitude() <= errorMax);
	}

	public boolean equalsWithinOneBillionth(Coords other)
	{
		return this.equalsWithinError(other, 0.000000001);
	}

	public boolean equalsXY(Coords other)
	{
		return (this.x == other.x && this.y == other.y);
	}

	public Coords floor()
	{
		this.x = Math.floor(this.x);
		this.y = Math.floor(this.y);
		this.z = Math.floor(this.z);
		return this;
	}

	public Coords fromHeadingInTurns(double headingInTurns)
	{
		var headingInRadians = headingInTurns * Polar.RadiansPerTurn;

		this.x = Math.cos(headingInRadians);
		this.y = Math.sin(headingInRadians);

		return this;
	}

	public Coords half()
	{
		return this.divideScalar(2);
	}

	public double headingInTurns()
	{
		double returnValue;

		if (this.x == 0 && this.y == 0)
		{
			returnValue = 0;
		}
		else
		{
			returnValue = Math.atan2(this.y, this.x) / (Math.PI * 2);

			if (returnValue < 0)
			{
				returnValue += 1;
			}

			returnValue = NumberHelper.wrapToRangeMinMax(returnValue, 0, 1);
		}

		return returnValue;
	}

	public Coords invert()
	{
		this.x = 0 - this.x;
		this.y = 0 - this.y;
		this.z = 0 - this.z;
		return this;
	}

	public boolean isInRangeMax(Coords max)
	{
		return this.isInRangeMinMax(Coords.Instances().Zeroes, max);
	}

	public boolean isInRangeMaxExclusive(Coords max)
	{
		return this.isInRangeMinInclusiveMaxExclusive(Coords.Instances().Zeroes, max);
	}

	public boolean isInRangeMinMax(Coords min, Coords max)
	{
		var returnValue =
		(
			this.x >= min.x
			&& this.x <= max.x
			&& this.y >= min.y
			&& this.y <= max.y
			&& this.z >= min.z
			&& this.z <= max.z
		);

		return returnValue;
	}

	public boolean isInRangeMinInclusiveMaxExclusive(Coords min, Coords max)
	{
		var returnValue =
		(
			this.x >= min.x
			&& this.x < max.x
			&& this.y >= min.y
			&& this.y < max.y
			&& this.z >= min.z
			&& this.z < max.z
		);

		return returnValue;
	}

	public double magnitude()
	{
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	public double magnitudeXY()
	{
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	public Coords multiply(Coords other)
	{
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;
		return this;
	}

	public Coords multiplyDimensions(double x, double y, double z)
	{
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Coords multiplyScalar(double scalar)
	{
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}

	public Coords normalize()
	{
		var magnitude = this.magnitude();
		if (magnitude > 0)
		{
			this.divideScalar(magnitude);
		}
		return this;
	}

	public Coords overwriteWith(Coords other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		return this;
	}

	public Coords overwriteWithDimensions(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Coords overwriteWithXY(Coords other)
	{
		this.x = other.x;
		this.y = other.y;
		return this;
	}

	public double productOfDimensions()
	{
		return this.x * this.y * this.z;
	}

	public Coords randomize(Randomizer randomizer)
	{
		if (randomizer == null)
		{
			randomizer = RandomizerSystem.Instance();
		}
		this.x = randomizer.getNextRandom();
		this.y = randomizer.getNextRandom();
		this.z = randomizer.getNextRandom();
		return this;
	}

	public Coords right()
	{
		var temp = this.y;
		this.y = this.x;
		this.x = 0 - temp;
		return this;
	}

	public Coords round()
	{
		this.x = Math.round(this.x);
		this.y = Math.round(this.y);
		this.z = Math.round(this.z);
		return this;
	}

	public Coords roundToDecimalPlaces(double doubleOfPlaces)
	{
		this.x = NumberHelper.roundToDecimalPlaces(this.x, doubleOfPlaces);
		this.y = NumberHelper.roundToDecimalPlaces(this.y, doubleOfPlaces);
		this.z = NumberHelper.roundToDecimalPlaces(this.z, doubleOfPlaces);
		return this;
	}

	public Coords subtract(Coords other)
	{
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	public Coords subtractWrappedToRangeMax(Coords other, Coords max)
	{
		this.x = NumberHelper.subtractWrappedToRangeMax(this.x, other.x, max.x);
		this.y = NumberHelper.subtractWrappedToRangeMax(this.y, other.y, max.y);
		this.z = NumberHelper.subtractWrappedToRangeMax(this.z, other.z, max.z);
		return this;
	}

	public double sumOfDimensions()
	{
		return this.x + this.y + this.z;
	}

	public Coords trimToMagnitudeMax(double magnitudeMax)
	{
		var magnitude = this.magnitude();
		if (magnitude > magnitudeMax)
		{
			this.divideScalar(magnitude).multiplyScalar(magnitudeMax);
		}
		return this;
	}

	public Coords trimToRangeMax(Coords max)
	{
		if (this.x < 0)
		{
			this.x = 0;
		}
		else if (this.x > max.x)
		{
			this.x = max.x;
		}

		if (this.y < 0)
		{
			this.y = 0;
		}
		else if (this.y > max.y)
		{
			this.y = max.y;
		}

		if (this.z < 0)
		{
			this.z = 0;
		}
		else if (this.z > max.z)
		{
			this.z = max.z;
		}

		return this;
	}

	public Coords trimToRangeMinMax(Coords min, Coords max)
	{
		if (this.x < min.x)
		{
			this.x = min.x;
		}
		else if (this.x >= max.x)
		{
			this.x = max.x;
		}

		if (this.y < min.y)
		{
			this.y = min.y;
		}
		else if (this.y >= max.y)
		{
			this.y = max.y;
		}

		if (this.z < min.z)
		{
			this.z = min.z;
		}
		else if (this.z >= max.z)
		{
			this.z = max.z;
		}

		return this;
	}

	public Coords wrapToRangeMax(Coords max)
	{
		while (this.x < 0)
		{
			this.x += max.x;
		}
		while (this.x >= max.x)
		{
			this.x -= max.x;
		}

		while (this.y < 0)
		{
			this.y += max.y;
		}
		while (this.y >= max.y)
		{
			this.y -= max.y;
		}

		if (max.z > 0)
		{
			while (this.z < 0)
			{
				this.z += max.z;
			}
			while (this.z >= max.z)
			{
				this.z -= max.z;
			}
		}

		return this;
	}

	public Coords xSet(double value)
	{
		this.x = value;
		return this;
	}

	public Coords ySet(double value)
	{
		this.y = value;
		return this;
	}

	public Coords zSet(double value)
	{
		this.z = value;
		return this;
	}

	// string

	public String toString()
	{
		return this.x + "x" + this.y + "x" + this.z;
	}

	public String toStringXY()
	{
		return this.x + "x" + this.y;
	}
}

class Coords_Instances
{
	public Coords HalfHalfZero;
	public Coords Halves;
	public Coords MinusOneZeroZero;
	public Coords Ones;
	public Coords OneOneZero;
	public Coords OneZeroZero;
	public Coords TwoTwoZero;
	public Coords ZeroZeroOne;
	public Coords ZeroMinusOneZero;
	public Coords ZeroOneZero;
	public Coords Zeroes;

	public Coords_Instances()
	{
		this.HalfHalfZero = new Coords(.5, .5, 0);
		this.Halves = new Coords(.5, .5, .5);
		this.MinusOneZeroZero = new Coords(-1, 0, 0);
		this.Ones = new Coords(1, 1, 1);
		this.OneOneZero = new Coords(1, 1, 0);
		this.OneZeroZero = new Coords(1, 0, 0);
		this.TwoTwoZero = new Coords(2, 2, 0);
		this.ZeroZeroOne = new Coords(0, 0, 1);
		this.ZeroMinusOneZero = new Coords(0, -1, 0);
		this.ZeroOneZero = new Coords(0, 1, 0);
		this.Zeroes = Coords.create();
	}
}
