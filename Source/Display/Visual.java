
package Display;

import Display.*;
import Geometry.Transforms.*;
import Model.*;
import Utility.*;

public interface Visual<T extends Visual> extends Clonable<T>, Transformable<T>
{
	void draw(UniverseWorldPlaceEntities uwpe, Display display);

	T clone();
	T overwriteWith(T other);
}
