
package Display.Visuals;

import Display.*;
import Display.Visuals.*;
import Helpers.*;
import Geometry.*;
import Geometry.Transforms.*;
import Model.*;
import Utility.*;

import java.util.*;
import java.util.stream.*;

public class VisualNone implements Visual<VisualNone>
{
	public VisualNone()
	{
		// Do nothing.
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		// Do nothing.
	}

	// Clonable.

	public VisualNone clone()
	{
		return new VisualNone();
	}

	public VisualNone overwriteWith(VisualNone other)
	{
		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return new Coords[] {}; }

	public VisualNone transform(Transform transformToApply)
	{
		return this;
	}
}
