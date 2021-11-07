
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualNone implements Visual<VisualNone>
{
	public static VisualNone Instance = new VisualNone();

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		// do nothing
	}

	// Clonable.

	public VisualNone clone()
	{
		return this; // todo
	}

	public VisualNone overwriteWith(VisualNone other)
	{
		return this; // todo
	}

	// Transformable.

	public VisualNone transform(Transform transformToApply)
	{
		return this; // todo
	}

	// Visual

	public void draw(UniverseWorldPlaceEntity uwpe, Display display)
	{
		// Do nothing.
	}
	
	// Transformable.
	
	public Coords[] coordsGroupToTranslate()
	{
		return new Coords[] {};
	}
}
