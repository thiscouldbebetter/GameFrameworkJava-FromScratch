
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;
import GameFramework.Utility.*;

public class BoxRotated implements ShapeBase<BoxRotated>
{
	public Box box;
	public double angleInTurns;

	public BoxRotated(Box box, double angleInTurns)
	{
		this.box = box;
		this.angleInTurns = angleInTurns;
	}

	public Sphere sphereSwept()
	{
		return new Sphere(this.box.center, this.box.sizeHalf().magnitude());
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		return ShapeHelper.Instance().applyLocationToShapeDefault(loc, this);
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		// todo - Adapt or call Box.normalAtPos() instead.

		var plane = new Plane(Coords.create(), 0);
		var polar = new Polar(0, 1, 0);
		var box = this.box;
		var center = box.center;
		var sizeHalf = box.sizeHalf();
		var displacementToSurface = Coords.create();
		var distanceMinSoFar = Double.POSITIVE_INFINITY;

		for (var d = 0; d < 2; d++)
		{
			polar.azimuthInTurns = this.angleInTurns + (d * .25);
			var dimensionHalf = sizeHalf.dimensionGet(d);

			for (var m = 0; m < 2; m++)
			{
				var directionToSurface = polar.toCoords(plane.normal);
				displacementToSurface.overwriteWith
				(
					directionToSurface
				).multiplyScalar
				(
					dimensionHalf
				);
				var pointOnSurface = displacementToSurface.add(center);
				plane.distanceFromOrigin =
					pointOnSurface.dotProduct(plane.normal);

				var distanceOfPosToCheckFromPlane = Math.abs
				(
					plane.distanceToPointAlongNormal(posToCheck)
				);
				if (distanceOfPosToCheckFromPlane < distanceMinSoFar)
				{
					distanceMinSoFar = distanceOfPosToCheckFromPlane;
					normalOut.overwriteWith(plane.normal);
				}

				polar.azimuthInTurns += .5;
				polar.azimuthInTurns =
					NumberHelper.wrapToRangeZeroOne(polar.azimuthInTurns);
			}
		}

		return normalOut;
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut) { throw new Exception("Not implemented!"); }

	// Clonable.

	public BoxRotated clone()
	{
		return new BoxRotated(this.box.clone(), this.angleInTurns);
	}

	public BoxRotated overwriteWith(BoxRotated other)
	{
		this.box.overwriteWith(other.box);
		this.angleInTurns = other.angleInTurns;
		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate()
	{
		return new Coords[] { this.box.center };
	}

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}
}
