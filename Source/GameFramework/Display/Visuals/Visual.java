
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public interface Visual extends Clonable<Visual>, Transformable
{
	void draw(UniverseWorldPlaceEntities uwpe, Display display);

	Visual clone();
	Visual overwriteWith(Visual other);
}
