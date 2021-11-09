
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Media.*;
import GameFramework.Model.*;

public class VisualImageFromLibrary implements VisualImage<VisualImageFromLibrary>
{
	public String imageName;

	private Coords _drawPos;

	public VisualImageFromLibrary(String imageName)
	{
		this.imageName = imageName;

		// Helper variables.
		this._drawPos = Coords.create();
	}

	// static methods

	public static VisualImageFromLibrary[] manyFromImages
	(
		Image2[] images, Coords imageSizeScaled
	)
	{
		var returnValues = new VisualImageFromLibrary[images.length];

		for (var i = 0; i < images.length; i++)
		{
			var image = images[i];
			var visual = new VisualImageFromLibrary(image.name());
			returnValues[i] = visual;
		}

		return returnValues;
	}

	// instance methods

	public Image2 image(Universe universe)
	{
		return universe.mediaLibrary.imageGetByName(this.imageName);
	}

	public Coords sizeInPixels(Universe universe)
	{
		return this.image(universe).sizeInPixels();
	}

	// visual

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var universe = uwpe.universe;
		var entity = uwpe.entity;
		var image = this.image(universe);
		var imageSize = image.sizeInPixels();
		var drawPos = this._drawPos.clear().subtract(imageSize).half().add
		(
			entity.locatable().loc.pos
		);
		display.drawImageScaled(image, drawPos, imageSize);
	}

	// Clonable.

	public VisualImageFromLibrary clone()
	{
		return this; // todo
	}

	public VisualImageFromLibrary overwriteWith(VisualImageFromLibrary other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public Visual transform(Transform transformToApply)
	{
		return this; // todo
	}
}
