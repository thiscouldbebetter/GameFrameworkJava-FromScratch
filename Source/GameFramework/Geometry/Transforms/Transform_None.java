
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public  class Transform_None implements Transform
{
	public Transform_None()
	{}

	public Transform_None overwriteWith(Transform other)
	{
		return this;
	}

	public Transformable transform(Transformable transformable)
	{
		return transformable;
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		return coordsToTransform;
	}
}

}
