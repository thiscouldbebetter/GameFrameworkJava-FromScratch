
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Utility.*;

public interface ShapeBase<T extends ShapeBase> extends Clonable<T>, Transformable
{
	ShapeBase locate(Disposition loc);
	Coords normalAtPos(Coords posToCheck, Coords normalOut);
	Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut);
	Box toBox(Box boxOut);
}
