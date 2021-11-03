
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;

public class Hemispace implements ShapeBase<Hemispace>
{
	// A hemispace is that infinitely large half of all space
	// that lies beneath a specified plane.

	public Plane plane;

	private Coords _displacement;

	public Hemispace(Plane plane)
	{
		this.plane = plane;

		this._displacement = Coords.create();
	}

	public boolean containsPoint(Coords pointToCheck)
	{
		var distanceOfPointAbovePlane =
			pointToCheck.dotProduct(this.plane.normal)
			- this.plane.distanceFromOrigin;
		var returnValue = (distanceOfPointAbovePlane <= 0);
		return returnValue;
	}

	public Coords trimCoords(Coords coordsToTrim)
	{
		var distanceOfPointAbovePlane =
			this.plane.distanceToPointAlongNormal(coordsToTrim);
		var areCoordsOutsideHemispace = (distanceOfPointAbovePlane > 0);
		if (areCoordsOutsideHemispace)
		{
			var displacementToClosestPointOnPlane =
				this._displacement.overwriteWith
				(
					this.plane.normal
				).multiplyScalar
				(
					0 - distanceOfPointAbovePlane
				);
			coordsToTrim.add(displacementToClosestPointOnPlane);
		}
		return coordsToTrim;
	}

	// Clonable.

	public Hemispace clone()
	{
		return new Hemispace(this.plane.clone());
	}

	public Hemispace overwriteWith(Hemispace other)
	{
		this.plane.overwriteWith(other.plane);
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		throw new Error("Not implemented!");
	}

	public  Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		return this.plane.normal;
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return surfacePointOut.overwriteWith
		(
			this.plane.pointOnPlaneNearestPos(posToCheck)
		);
	}

	public Box toBox(Box boxOut)
	{
		throw new Error("Not implemented!");
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public Transformable transform(Transform transformToApply)
	{
		this.plane.transform(transformToApply);
		return this;
	}
}
