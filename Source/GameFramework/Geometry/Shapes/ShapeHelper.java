
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;

public class ShapeHelper
{
	private Transform_Locate _transformLocate;

	public constructor()
	{
		this._transformLocate = new Transform_Locate(null);
	}

	private static ShapeHelper _instance;
	public static ShapeHelper Instance()
	{
		if (ShapeHelper._instance == null)
		{
			ShapeHelper._instance = new ShapeHelper();
		}
		return ShapeHelper._instance;
	}

	public ShapeBase applyLocationToShapeDefault
	(
		Disposition loc, ShapeBase shape
	)
	{
		this._transformLocate.loc = loc;

		Transforms.applyTransformToCoordsMObject
		(
			this._transformLocate,
			shape.coordsGroupToTranslate()
		);

		return shape;
	}
}
