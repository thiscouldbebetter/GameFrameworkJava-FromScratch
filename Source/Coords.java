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

	public Dimension toDimension()
	{
		return new Dimension((int)this.x, (int)this.y);
	}

	// Clonable.

	public Coords clone()
	{
		return new Coords(this.x, this.y);
	}
}
