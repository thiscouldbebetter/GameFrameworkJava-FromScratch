
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;

public class ShapeContainer implements ShapeBase<ShapeContainer>
{
	public ShapeBase shape;

	public ShapeContainer(ShapeBase shape)
	{
		this.shape = shape;
	}

	// Clonable.

	public ShapeContainer clone()
	{
		return new ShapeContainer((ShapeBase)(this.shape.clone()));
	}

	public ShapeContainer overwriteWith(ShapeContainer other)
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
		return this.shape.normalAtPos(posToCheck, normalOut);
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return this.shape.surfacePointNearPos(posToCheck, surfacePointOut);
	}

	public Box toBox(Box boxOut)
	{
		return this.shape.toBox(boxOut);
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate()
	{
		return null;
	}

	public ShapeContainer transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}

}
