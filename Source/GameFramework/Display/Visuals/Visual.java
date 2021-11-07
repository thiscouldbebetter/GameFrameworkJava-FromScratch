
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public interface Visual<T extends Visual> extends Clonable<T>, Transformable<T>
{
	void draw(UniverseWorldPlaceEntities uwpe, Display display);

	T clone();
	T overwriteWith(T other);
}
