
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public interface Visual extends Transformable
{
	void draw(UniverseWorldPlaceEntities uwpe, Display display);

	Visual clone();
	Visual overwriteWith(Visual other);
}
