
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Utility.*;

public interface ShapeBase extends Clonable<ShapeBase>, Transformable
{
	ShapeBase locate(Disposition loc);
	Coords normalAtPos(Coords posToCheck, Coords normalOut);
	Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut);
	Box toBox(Box boxOut);
}
