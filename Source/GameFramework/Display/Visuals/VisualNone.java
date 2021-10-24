
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Model.*;

public class VisualNone implements Visual
{
	public static VisualNone Instance = new VisualNone();

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		// do nothing
	}

	// Clonable.

	public Visual clone()
	{
		return this; // todo
	}

	public Visual overwriteWith(Visual other)
	{
		return this; // todo
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}

	// Visual

	void draw
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		// Do nothing.
	}
	
	// Transformable.
	
	public Coords[] coordsGroupToTranslate()
	{
		return new Coords[] {};
	}
}
