
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public interface Transform
{
	Transform overwriteWith(Transform other);
	Transformable transform(Transformable transformable);
	Coords transformCoords(Coords coordsToTransform);
}
