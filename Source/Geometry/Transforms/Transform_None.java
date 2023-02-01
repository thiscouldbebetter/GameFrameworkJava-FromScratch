
package Geometry.Transforms;

import Geometry.*;

public  class Transform_None implements Transform
{
	public Transform_None()
	{}

	public Transform_None overwriteWith(Transform other)
	{
		return this;
	}

	public T transform<T> extends Transformable<T>>(T transformable)
	{
		return transformable;
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		return coordsToTransform;
	}
}
