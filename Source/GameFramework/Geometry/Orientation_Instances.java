package GameFramework.Geometry;

public class Orientation_Instances
{
	public Orientation ForwardXDownZ;
	public Orientation ForwardZDownY;

	public Orientation_Instances()
	{
		this.ForwardXDownZ = new Orientation
		(
			new Coords(1, 0, 0), // forward
			new Coords(0, 0, 1) // down
		);

		this.ForwardZDownY = new Orientation
		(
			new Coords(0, 0, 1), // forward
			new Coords(0, 1, 0) // down
		);
	}
}
