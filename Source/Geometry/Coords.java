package Geometry;

import java.awt.*;

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

	public static Coords create()
	{
		return Coords.zeroes();
	}

	public static Coords fromXY(double x, double y)
	{
		return new Coords(x, y, 0);
	}

	public static Coords zeroes()
	{
		return new Coords(0, 0, 0);
	}

	private static Coords_Instances _instances;
	public static Coords_Instances Instances()
	{
		if (Coords._instances == null)
		{
			Coords._instances = new Coords_Instances();
		}
		return Coords._instances;
	}

	public Coords add(Coords other)
	{
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
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

	public Coords crossProduct(Coords other)
	{
		return this.overwriteWithDimensions
		(
			this.y * other.z - this.z * other.y,
			this.z * other.x - this.x * other.z,
			this.x * other.y - this.y * other.x
		);
	}

	public double dimensionGet(int dimensionIndex)
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
			returnValue = 0; // hack
			// throw new Exception("Invalid dimensionIndex: " + dimensionIndex);
		}
		return returnValue;
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

	public Coords half()
	{
		return this.divideScalar(2);
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

	public Coords multiply(Coords other)
	{
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;
		return this;
	}

	public double magnitude()
	{
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
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

	public Coords overwriteWithDimensions(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Coords round()
	{
		this.x = Math.round(this.x);
		this.y = Math.round(this.y);
		this.z = Math.round(this.z);
		return this;
	}

	public Coords subtract(Coords other)
	{
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	public Dimension toDimension()
	{
		return new Dimension((int)this.x, (int)this.y);
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

	// Clonable.

	public Coords clone()
	{
		return new Coords(this.x, this.y, this.z);
	}

	public Coords overwriteWith(Coords other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		return this;
	}
}
