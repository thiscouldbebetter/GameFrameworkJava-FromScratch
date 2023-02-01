
package Geometry.Transforms;

import Geometry.*;

public interface Transformable<T extends Transformable>
{
	Coords[] coordsGroupToTranslate();
	T transform(Transform t);
}

