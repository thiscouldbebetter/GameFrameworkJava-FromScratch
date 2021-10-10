
package GameFramework.Geometry;

import java.awt.*;

public class Coords
{
	public double x;
	public double y;
	public double z;

	public Coords(int x, int y)
	{
		this(x, y, 0);
	}

	public Coords(int x, int y, int z)
	{
		this((double)x, (double)y, (double)z);
	}

	public Coords(double x, double y)
	{
		this(x, y, 0.0);
	}

	public Coords(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
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

	public Coords multiplyScalar(double scalar)
	{
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
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
}
