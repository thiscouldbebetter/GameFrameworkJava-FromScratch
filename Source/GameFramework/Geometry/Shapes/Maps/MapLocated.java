
package GameFramework.Geometry.Shapes.Maps;

import GameFramework.Geometry.*;

public class MapLocated implements ShapeBase
{
	public MapOfCells<Object> map;
	public Disposition loc;

	public Box box;

	public MapLocated(MapOfCells<Object> map, Disposition loc)
	{
		this.map = map;
		this.loc = loc;

		this.box = new Box(this.loc.pos, this.map.size);
	}

	// cloneable

	public MapLocated clone()
	{
		return new MapLocated(this.map, this.loc.clone());
	}

	public MapLocated overwriteWith(MapLocated other)
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

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut) { throw new Error("Not implemented!"); }

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}
}
