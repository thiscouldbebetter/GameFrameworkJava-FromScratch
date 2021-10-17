
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public class Transform_Locate implements Transform
{
	public Disposition loc;

	public Transform transformOrient_Orient;
	public Transform transformTranslate_Translate;

	public Transform_Locate(Disposition loc)
	{
		this.loc = (loc != null ? loc : Disposition.create());

		this.transformOrient = new Transform_Orient(null);
		this.transformTranslate = new Transform_Translate(null);
	}

	public Transform_Locate overwriteWith(Transform other)
	{
		return this; // todo
	}

	public Transformable transform(Transformable transformable)
	{
		return transformable.transform(this);
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		this.transformOrient.orientation = this.loc.orientation;
		this.transformOrient.transformCoords(coordsToTransform);

		this.transformTranslate.displacement = this.loc.pos;
		this.transformTranslate.transformCoords(coordsToTransform);

		return coordsToTransform;
	}
}