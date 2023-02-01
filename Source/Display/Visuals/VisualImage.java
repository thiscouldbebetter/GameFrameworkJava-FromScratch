
package Display.Visuals;

import Display.*;
import Geometry.*;
import Media.*;
import Model.*;

public interface VisualImage<T extends Visual> extends Visual<T>
{
	Image2 image(Universe u);
	Coords sizeInPixels(Universe u);
}
