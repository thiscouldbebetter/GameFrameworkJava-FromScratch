
package GameFramework.Geometry.Shapes;

import java.util.stream.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class ShapeGroupAll implements ShapeBase
{
	public ShapeBase[] shapes;

	public ShapeGroupAll(ShapeBase[] shapes)
	{
		this.shapes = shapes;
	}

	// Clonable.

	public ShapeGroupAll clone()
	{
		return new ShapeGroupAll(ArrayHelper.clone(this.shapes));
	}

	public ShapeGroupAll overwriteWith(ShapeGroupAll other)
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
		throw new Exception("Not implemented!");
	}

	public Box toBox(Box boxOut)
	{
		throw new Exception("Not implemented!");
	}

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
