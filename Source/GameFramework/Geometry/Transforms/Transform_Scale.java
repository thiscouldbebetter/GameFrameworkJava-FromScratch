
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public class Transform_Scale implements Transform
{
	public Coords scaleFactors;

	public Transform_Scale(Coords scaleFactors)
	{
		this.scaleFactors = scaleFactors;
	}

	public static Transform_Scale fromScalar(double scalar)
	{
		return new Transform_Scale(new Coords(1, 1, 1).multiplyScalar(scalar));
	}

	public Transform overwriteWith(Transform other)
	{
		var otherAsScale = (Transform_Scale)other;
		this.scaleFactors.overwriteWith(otherAsScale.scaleFactors);
		return this;
	}

	public Transformable transform(Transformable transformable)
	{
		return transformable.transform(this);
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		return coordsToTransform.multiply(this.scaleFactors);
	}
}
