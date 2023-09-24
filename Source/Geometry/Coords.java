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

	public Coords multiplyScalar(double scalar)
	{
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}

	public Coords overwriteWith(Coords other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
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
