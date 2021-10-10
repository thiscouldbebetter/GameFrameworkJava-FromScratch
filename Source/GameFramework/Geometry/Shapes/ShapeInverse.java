
package GameFramework.Geometry.Shapes;

public class ShapeInverse implements ShapeBase
{
	public ShapeBase shape;

	public ShapeInverse(ShapeBase shape)
	{
		this.shape = shape;
	}

	// Clonable.

	public ShapeInverse clone()
	{
		return new ShapeInverse(this.shape.clone());
	}

	public ShapeInverse overwriteWith(ShapeInverse other)
	{
		this.shape.overwriteWith(other.shape);
		return this;
	}

	// ShapeBase.

	public Disposition locate(Disposition loc)
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

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}

}
