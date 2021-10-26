
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public interface Transformable
{
	Coords[] coordsGroupToTranslate();
	Transformable transform(Transform t);
}

