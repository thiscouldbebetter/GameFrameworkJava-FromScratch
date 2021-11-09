
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Media.*;
import GameFramework.Model.*;

public interface VisualImage<T> extends Visual<T>
{
	Image2 image(Universe u);
	Coords sizeInPixels(Universe u);
}
