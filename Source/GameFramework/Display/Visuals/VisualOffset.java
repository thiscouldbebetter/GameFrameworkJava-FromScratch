
package GameFramework.Display.Visuals;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Tirable.*;

public class VisualOffset implements Visual
{
	public Visual child;
	public Coords offset;

	public Coords _posSaved;

	public VisualOffset(Visual child, Coords offset)
	{
		this.child = child;
		this.offset = offset;

		// Helper variables.
		this._posSaved = Coords.create();
	}

	public static VisualOffset fromOffsetAndChild
	(
		Coords offset, Visual child
	)
	{
		return new VisualOffset(child, offset);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		var drawablePos = entity.locatable().loc.pos;
		this._posSaved.overwriteWith(drawablePos);
		drawablePos.add(this.offset);
		this.child.draw(uwpe, display);
		drawablePos.overwriteWith(this._posSaved);
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

	public Coords[] coordsGroupToTranslate() { return null; }
	
	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}
