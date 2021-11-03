
package GameFramework.Geometry.Shapes;

import java.util.*;
import java.util.stream.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class ShapeGroupAll implements ShapeBase<ShapeGroupAll>
{
	public ShapeBase[] shapes;

	public ShapeGroupAll(ShapeBase[] shapes)
	{
		this.shapes = shapes;
	}

	// Clonable.

	public ShapeBase overwriteWith(ShapeBase otherAsShapeBase)
	{
		var other = (ShapeGroupAll)otherAsShapeBase;
		ArrayHelper.overwriteWith(this.shapes, other.shapes);
		return this;
	}

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

	public Coords[] coordsGroupToTranslate() { return null; }

	public Transformable transform(Transform transformToApply)
	{
		Arrays.asList(this.shapes).stream().forEach
		(
			(ShapeBase x) -> x.transform(transformToApply)
		);
		return this;
	}
}
