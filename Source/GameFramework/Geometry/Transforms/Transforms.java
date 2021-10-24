
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public class Transforms
{
	public static void applyTransformToCoordsArrays
	(
		Transform transformToApply, Coords[][] coordsArraysToTransform
	)
	{
		if (coordsArraysToTransform == null)
		{
			return;
		}

		for (var i = 0; i < coordsArraysToTransform.length; i++)
		{
			var coordsArray = coordsArraysToTransform[i];
			Transforms.applyTransformToCoordsMany
			(
				transformToApply, coordsArray
			);
		}
	}

	public static applyTransformToCoordsMany
	(
		Transform transformToApply, Coords[] coordsSetToTransform
	)
	{
		for (var i = 0; i < coordsSetToTransform.length; i++)
		{
			transformToApply.transformCoords(coordsSetToTransform[i]);
		}
	}
}
