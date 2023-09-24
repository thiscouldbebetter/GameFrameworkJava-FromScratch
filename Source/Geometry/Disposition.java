package Geometry;

public class Disposition
{
	public Coords pos;
	public Coords vel;

	public Disposition(Coords pos, Coords vel)
	{
		this.pos = pos;
		this.vel = vel;
	}

	public static Disposition create()
	{
		return new Disposition(Coords.zeroes(), Coords.zeroes() );
	}

	public static Disposition fromPos(Coords pos)
	{
		return new Disposition(pos, Coords.zeroes());
	}

	public Disposition overwriteWith(Disposition other)
	{
		this.pos.overwriteWith(other.pos);
		this.vel.overwriteWith(other.vel);
		return this;
	}
}
