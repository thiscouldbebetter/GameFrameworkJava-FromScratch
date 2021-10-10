
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;

public class Cylinder
{
	public Coords center;
	public double radius;
	public double length;

	double lengthHalf;

	public Cylinder(Coords center, double radius, double length)
	{
		this.center = center;
		this.radius = radius;
		this.length = length;

		this.lengthHalf = this.length / 2;
	}
}
