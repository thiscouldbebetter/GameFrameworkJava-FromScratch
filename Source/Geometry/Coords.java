package Geometry;

import java.awt.*;

public class Coords
{
	public double x;
	public double y;

	public Coords(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public static Coords create()
	{
		return Coords.zeroes();
	}

	public static Coords fromXY(double x, double y)
	{
		return new Coords(x, y);
	}

	public static Coords zeroes()
	{
		return new Coords(0, 0);
	}

	public Coords add(Coords other)
	{
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public Coords clear()
	{
		this.x = 0;
		this.y = 0;
		return this;
	}

	public Coords divideScalar(double scalar)
	{
		this.x /= scalar;
		this.y /= scalar;
		return this;
	}

	public Coords half()
	{
		return this.divideScalar(2);
	}

	public Coords multiplyScalar(double scalar)
	{
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	public Coords subtract(Coords other)
	{
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public Dimension toDimension()
	{
		return new Dimension((int)this.x, (int)this.y);
	}

	// Clonable.

	public Coords clone()
	{
		return new Coords(this.x, this.y);
	}

	public Coords overwriteWith(Coords other)
	{
		this.x = other.x;
		this.y = other.y;
		return this;
	}
}
