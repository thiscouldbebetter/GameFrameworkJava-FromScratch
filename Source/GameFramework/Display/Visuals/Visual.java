
namespace GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.Transformable.*;
import GameFramework.Model.*;

public interface Visual extends Transformable
{
	Object draw(UniverseWorldPlaceEntities uwpe, Display display);

	Visual clone();
	Visual overwriteWith(Visual other)
}
