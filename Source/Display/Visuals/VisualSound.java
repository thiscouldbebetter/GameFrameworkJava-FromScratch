
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

public class VisualSound implements Visual<VisualSound>
{
	public String soundName;
	public boolean isRepeating;

	public VisualSound(String soundName, boolean isRepeating)
	{
		this.soundName = soundName;
		this.isRepeating = isRepeating;
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		// todo
	}

	// Clonable.

	public VisualSound clone()
	{
		return this; // todo
	}

	public VisualSound overwriteWith(VisualSound other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return new Coords[] {}; }

	public VisualSound transform(Transform transformToApply)
	{
		return this;
	}
}
