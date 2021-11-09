
package GameFramework.Geometry.Shapes.Maps;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Transforms.*;

public class MapLocated<T extends Clonable> implements ShapeBase<T>
{
	public MapOfCells<T> map;
	public Disposition loc;

	public Box box;

	public MapLocated(MapOfCells<T> map, Disposition loc)
	{
		this.map = map;
		this.loc = loc;

		this.box = new Box(this.loc.pos, this.map.size);
	}

	// cloneable

	public MapLocated<T> clone()
	{
		return new MapLocated<T>(this.map, this.loc.clone());
	}

	public MapLocated<T> overwriteWith(MapLocated<T> other)
	{
		this.loc.overwriteWith(other.loc);
		return this;
	}

	// translatable

	public Coords[] coordsGroupToTranslate()
	{
		return new Coords[] { this.loc.pos };
	}

	// Shape.

	public ShapeBase locate(Disposition loc)
	{
		return ShapeHelper.Instance().applyLocationToShapeDefault(loc, this);
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		return normalOut.overwriteWith(posToCheck).subtract(this.loc.pos).normalize();
	}

	public Coords surfacePointNearPos
	(
		Coords posToCheck, Coords surfacePointOut
	)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut) { return null; }

	// Transformable.

	public MapLocated<T> transform(Transform transformToApply)
	{
		return null;
	}
}
