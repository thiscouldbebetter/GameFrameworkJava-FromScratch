
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public interface Transformable<T extends Transformable>
{
	Coords[] coordsGroupToTranslate();
	T transform(Transform t);
}

