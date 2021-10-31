
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualImageImmediate implements VisualImage
{
	public Image2 _image;
	public boolean isScaled;

	private Coords _drawPos;

	public VisualImageImmediate(Image2 image, boolean isScaled)
	{
		this._image = image;
		this.isScaled = isScaled;

		// Helper variables.

		this._drawPos = Coords.create();
	}

	// instance methods

	public Image2 image(Universe universe)
	{
		return this._image;
	}

	public Coords sizeInPixels(Universe universe)
	{
		return this.image(universe).sizeInPixels;
	}

	// visual

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var universe = uwpe.universe;
		var entity = uwpe.entity;

		var image = this.image(universe);
		var imageSize = image.sizeInPixels;
		var drawPos = this._drawPos.clear().subtract(imageSize).half().add
		(
			entity.locatable().loc.pos
		);
		if (this.isScaled)
		{
			display.drawImageScaled(image, drawPos, imageSize);
		}
		else
		{
			display.drawImage(image, drawPos);
		}
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

	public Coords[] coordsGroupToTranslate() { return null; } // todo

	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}
