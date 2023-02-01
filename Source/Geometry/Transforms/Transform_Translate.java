
package Geometry.Transforms;

import Geometry.*;

public class Transform_Translate implements Transform
{
	public Coords displacement;

	public Transform_Translate(Coords displacement)
	{
		this.displacement = displacement;
	}

	public Transform_Translate displacementSet(Coords value)
	{
		this.displacement.overwriteWith(value);
		return this;
	}

	// transform

	public Transform_Translate overwriteWith(Transform other)
	{
		return this; // todo
	}

	public Transformable transform(Transformable transformable)
	{
		return transformable.transform(this);
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		return coordsToTransform.add(this.displacement);
	}
}
