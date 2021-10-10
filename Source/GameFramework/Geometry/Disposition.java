
package GameFramework.Geometry;

public class Disposition
{
	public Coords pos;
	public Orientation orientation;
	public String placeName;

	public Coords vel;
	public Coords accel;
	public Coords force;
	public Rotation spin;
	public number timeOffsetInTicks;

	public Disposition
	(
		Coords pos, Orientation orientation, String placeName
	)
	{
		this.pos = pos || Coords.create();

		if (orientation == null)
		{
			orientation = Orientation.Instances().ForwardXDownZ.clone();
		}
		this.orientation = orientation;

		this.placeName = placeName;

		this.vel = Coords.create();
		this.accel = Coords.create();
		this.force = Coords.create();

		this.spin = new Rotation(this.orientation.down, new Reference(0));

		this.timeOffsetInTicks = 0;
	}

	public static Disposition create()
	{
		return new Disposition(Coords.create(), Orientation._default(), null);
	}

	public static Disposition fromOrientation(Orientation orientation)
	{
		return new Disposition(Coords.create(), orientation, null);
	}

	public static Disposition fromPos(Coords pos)
	{
		return new Disposition(pos, Orientation._default(), null);
	}

	public static Disposition fromPosAndOrientation
	(
		Coords pos, Orientation orientation
	)
	{
		return new Disposition(pos, orientation, null);
	}

	public static Disposition fromPosAndVel(Coords pos, Coords vel)
	{
		var returnValue = Disposition.fromPos(pos);
		returnValue.vel = vel;
		return returnValue;
	}

	public boolean equals(Disposition other)
	{
		var returnValue =
		(
			this.placeName == other.placeName
			&& this.pos.equals(other.pos)
			&& this.orientation.equals(other.orientation)
		);

		return returnValue;
	}

	public Place place(World world)
	{
		return world.placesByName.get(this.placeName);
	}

	public Disposition velSet(Coords value)
	{
		this.vel.overwriteWith(value);
		return this;
	}

	// cloneable

	public Disposition clone()
	{
		var returnValue = new Disposition
		(
			this.pos.clone(),
			this.orientation.clone(),
			this.placeName
		);

		returnValue.vel = this.vel.clone();
		returnValue.accel = this.accel.clone();
		returnValue.force = this.force.clone();
		returnValue.timeOffsetInTicks = this.timeOffsetInTicks;

		return returnValue;
	}

	public Disposition overwriteWith(Disposition other)
	{
		this.placeName = other.placeName;
		this.pos.overwriteWith(other.pos);
		this.orientation.overwriteWith(other.orientation);
		this.vel.overwriteWith(other.vel);
		this.accel.overwriteWith(other.accel);
		this.force.overwriteWith(other.force);
		return this;
	}

	// strings

	public String toString()
	{
		return this.pos.clone().round().toString();
	}
}
