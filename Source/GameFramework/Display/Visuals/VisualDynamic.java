
package GameFramework.Display.Visuals;

import java.util.function.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualDynamic implements Visual<VisualDynamic>
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

	public VisualDynamic clone()
	{
		return this; // todo
	}

	public VisualDynamic overwriteWith(VisualDynamic other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return new Coords[] {}; }

	public VisualDynamic transform(Transform transformToApply)
	{
		return this; // todo
	}
}
