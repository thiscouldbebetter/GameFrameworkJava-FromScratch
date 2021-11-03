
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;

public class ShapeInverse<T extends ShapeBase<T>> implements ShapeBase<ShapeInverse<T>>
{
	public T shape;

	public ShapeInverse(T shape)
	{
		this.shape = shape;
	}

	// Clonable.

	public ShapeInverse<T> clone()
	{
		return new ShapeInverse<T>(this.shape.clone());
	}

	public ShapeInverse<T> overwriteWith(ShapeInverse<T> other)
	{
		this.shape.overwriteWith(other.shape);
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		this.shape.locate(loc);
		return this;
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		return this.shape.normalAtPos(posToCheck, normalOut).invert();
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return this.shape.surfacePointNearPos(posToCheck, surfacePointOut);
	}

	public Box toBox(Box boxOut) { throw new Exception("Not implemented!"); }

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}

}
