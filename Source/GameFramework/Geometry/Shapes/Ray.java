
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;

public class Ray
{
	public Coords vertex;
	public Coords direction;

	public Ray(Coords vertex, Coords direction)
	{
		this.vertex = vertex;
		this.direction = direction;
	}
}
