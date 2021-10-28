
package GameFramework.Display.Visuals;

import java.util.function.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;

public class VisualDynamic implements Visual
{
	Function<UniverseWorldPlaceEntities,Visual> methodForVisual;

	public VisualDynamic(Function<UniverseWorldPlaceEntities,Visual> methodForVisual)
	{
		this.methodForVisual = methodForVisual;
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var visual = this.methodForVisual.apply(uwpe);
		visual.draw(uwpe, display);
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

	public Coords[] coordsGroupToTranslate() { return new Coords[] {}; );

	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}
