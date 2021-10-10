
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;

public class Sphere implements ShapeBase
{
	public Coords center;
	public double radius;

	private Coords[] _centerAsArray;
	private Coords _displacement;
	private Coords _pointRandom;

	public Sphere(Coords center, double radius)
	{
		this.center = center;
		this.radius = radius;

		// Helper variables.
		this._centerAsArray = new Coords[] { this.center };
		this._displacement = Coords.create();
	}

	public static Sphere _default()
	{
		return new Sphere(Coords.create(), 1);
	}

	public static Sphere fromRadius(double radius)
	{
		return new Sphere(Coords.create(), radius);
	}

	public static Spehre fromRadiusAndCenter(double radius, Coords center)
	{
		return new Sphere(center, radius);
	}

	public boolean containsOther(Sphere other)
	{
		var displacementOfOther =
			this._displacement.overwriteWith(other.center).subtract(this.center);
		var distanceOfOther = displacementOfOther.magnitude();
		var returnValue = (distanceOfOther + other.radius <= this.radius);
		return returnValue;
	}

	public Coords pointRandom()
	{
		return new Polar
		(
			0, this.radius, 0
		).random
		(
			null
		).toCoords
		(
			this._pointRandom
		).add
		(
			this.center
		);
	}

	// cloneable

	public Sphere clone()
	{
		return new Sphere(this.center.clone(), this.radius);
	}

	public boolean equals(Sphere other)
	{
		return
		(
			this.center.equals(other.center)
			&& this.radius == other.radius
		);
	}

	public Sphere overwriteWith(Sphere other)
	{
		this.center.overwriteWith(other.center);
		this.radius = other.radius;
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		this.center.overwriteWith(loc.pos);
		return this;
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		return normalOut.overwriteWith
		(
			posToCheck
		).subtract
		(
			this.center
		).normalize();
	}

	public Coords surfacePointNearPos
	(
		Coords posToCheck, Coords surfacePointOut
	)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut)
	{
		var diameter = this.radius * 2;
		boxOut.size.overwriteWithDimensions(diameter, diameter, diameter);
		return boxOut;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate()
	{
		return this._centerAsArray;
	}

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}
}

}
