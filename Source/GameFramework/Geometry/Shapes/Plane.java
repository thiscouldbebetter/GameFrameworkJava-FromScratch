
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;

public class Plane implements ShapeBase
{
	public Coords normal;
	public double distanceFromOrigin;

	private Coords _displacementFromPoint0To2;

	public Plane(Coords normal, double distanceFromOrigin)
	{
		this.normal = normal;
		this.distanceFromOrigin = distanceFromOrigin;

		this._displacementFromPoint0To2 = Coords.create();
	}

	public double distanceToPointAlongNormal(Coords point)
	{
		return point.dotProduct
		(
			this.normal
		) - this.distanceFromOrigin;
	}

	public boolean equals(Plane other)
	{
		return
		(
			this.normal.equals(other.normal)
			&& this.distanceFromOrigin == other.distanceFromOrigin
		);
	}

	public Plane fromPoints(Coords point0, Coords point1, Coords point2)
	{
		this.normal.overwriteWith
		(
			point1
		).subtract
		(
			point0
		).crossProduct
		(
			this._displacementFromPoint0To2.overwriteWith
			(
				point2
			).subtract
			(
				point0
			)
		).normalize();

		this.distanceFromOrigin = point0.dotProduct(this.normal);

		return this;
	}

	public Coords pointClosestToOrigin(Coords point)
	{
		return point.overwriteWith
		(
			this.normal
		).multiplyScalar
		(
			this.distanceFromOrigin
		);
	}

	public Coords pointOnPlaneNearestPos(Coords posToCheck)
	{
		var distanceToPoint = this.distanceToPointAlongNormal(posToCheck);

		return this.normal.clone().multiplyScalar
		(
			distanceToPoint
		).invert().add
		(
			posToCheck
		);
	}

	// Clonable.

	public Plane clone()
	{
		return new Plane(this.normal.clone(), this.distanceFromOrigin);
	}

	public Plane overwriteWith(Plane other)
	{
		this.normal.overwriteWith(other.normal);
		this.distanceFromOrigin = other.distanceFromOrigin;
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		throw new Exception("Not implemented!");
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		throw new Exception("Not implemented!");
	}

	public Coords surfacePointNearPos
	(
		Coords posToCheck, Coords surfacePointOut
	)
	{
		throw new Exception("Not implemented!");
	}

	public Box toBox(Box boxOut)
	{
		throw new Exception("Not implemented!");
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}
}
