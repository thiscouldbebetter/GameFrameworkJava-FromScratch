package Display.Visuals;

import Display.*;
import Geometry.*;
import Media.*;
import Model.*;

public class VisualImageImmediate implements Visual
{
	private Image2 _image;
	public boolean isScaled;

	private Coords _drawPos;

	public VisualImageImmediate(Image2 image, boolean isScaled)
	{
		this._image = image;
		this.isScaled = isScaled;

		// Helper variables.

		this._drawPos = Coords.create();
	}

	public static VisualImageImmediate fromImage(Image2 image)
	{
		return new VisualImageImmediate(image, false);
	}

	// Instance methods.

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

	public VisualImageImmediate clone()
	{
		return this; // todo
	}

	public VisualImageImmediate overwriteWith(VisualImageImmediate other)
	{
		return this; // todo
	}

	// Transformable.

	/*
	private VisualImageImmediate transform(TransformBase transformToApply)
	{
		return this; // todo
	}
	*/
}
