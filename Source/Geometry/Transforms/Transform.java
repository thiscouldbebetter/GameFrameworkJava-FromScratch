
package Geometry.Transforms;

import Geometry.*;

public interface Transform
{
	Transform overwriteWith(Transform other);
	Transformable transform(Transformable transformable);
	Coords transformCoords(Coords coordsToTransform);
}
