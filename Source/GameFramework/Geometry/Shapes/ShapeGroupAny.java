
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class ShapeGroupAny implements ShapeBase
{
	public ShapeBase shapes[];

	private Coords _displacement;
	private Coords _surfacePointForChild;

	public ShapeGroupAny(ShapeBase[] shapes)
	{
		this.shapes = shapes;

		this._displacement = Coords.create();
		this._surfacePointForChild = Coords.create();
	}

	// Clonable.

	public ShapeGroupAny clone()
	{
		return new ShapeGroupAny(ArrayHelper.clone(this.shapes));
	}

	public ShapeGroupAny overwriteWith(ShapeGroupAny other)
	{
		ArrayHelper.overwriteWith(this.shapes, other.shapes);
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

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		var distanceMinSoFar = Integer.POSITIVE_INFINITY;
		for (var i = 0; i < this.shapes.length; i++)
		{
			var shape = this.shapes[i];

			shape.surfacePointNearPos(posToCheck, this._surfacePointForChild);

			var distanceFromPosToCheck = this._displacement.overwriteWith
			(
				this._surfacePointForChild
			).subtract
			(
				posToCheck
			).magnitude();

			if (distanceFromPosToCheck < distanceMinSoFar)
			{
				distanceMinSoFar = distanceFromPosToCheck;
				surfacePointOut.overwriteWith(this._surfacePointForChild);
			}
		}

		return surfacePointOut;
	}

	public Box toBox(Box boxOut) { throw new Exception("Not implemented!"); }

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		this.shapes.stream().forEach
		(
			(ShapeBase x) -> x.transform(transformToApply)
		);
		return this;
	}
}
