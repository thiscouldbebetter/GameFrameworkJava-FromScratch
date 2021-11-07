
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualAnchor implements Visual<VisualAnchor>
{
	public Visual child;
	public Coords posToAnchorAt;
	public Orientation orientationToAnchorAt;

	private Coords _posSaved;
	private Orientation _orientationSaved;

	public VisualAnchor
	(
		Visual child, Coords posToAnchorAt, Orientation orientationToAnchorAt
	)
	{
		this.child = child;
		this.posToAnchorAt = posToAnchorAt;
		this.orientationToAnchorAt = orientationToAnchorAt;

		// Helper variables.
		this._posSaved = Coords.create();
		this._orientationSaved = new Orientation(null, null);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		var drawableLoc = entity.locatable().loc;
		var drawablePos = drawableLoc.pos;
		var drawableOrientation = drawableLoc.orientation;

		this._posSaved.overwriteWith(drawablePos);
		this._orientationSaved.overwriteWith(drawableOrientation);

		if (this.posToAnchorAt != null)
		{
			drawablePos.overwriteWith(this.posToAnchorAt);
		}
		if (this.orientationToAnchorAt != null)
		{
			drawableOrientation.overwriteWith(this.orientationToAnchorAt);
		}

		this.child.draw(uwpe, display);

		drawablePos.overwriteWith(this._posSaved);
		drawableOrientation.overwriteWith(this._orientationSaved);
	}

	// Clonable.

	public VisualAnchor clone()
	{
		return this; // todo
	}

	public VisualAnchor overwriteWith(VisualAnchor other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }
	
	public VisualAnchor transform(Transform transformToApply)
	{
		return this; // todo
	}
}
