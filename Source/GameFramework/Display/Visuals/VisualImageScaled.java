
namespace GameFramework.Display.Visuals;

public class VisualImageScaled implements VisualImage
{
	public VisualImage visualImage;
	public Coords sizeToDraw;

	private Coords sizeToDrawHalf;
	private Coords _posSaved;

	public VisualImageScaled(VisualImage visualImage, Coords sizeToDraw)
	{
		this.visualImage = visualImage;
		this.sizeToDraw = sizeToDraw;

		this.sizeToDrawHalf = this.sizeToDraw.clone().half();
		this._posSaved = Coords.create();
	}

	public static VisualImageScaled[] manyFromSizeAndVisuals
	(
		Coords sizeToDraw, VisualImage[] visualsToScale
	)
	{
		var returnValues = new VisualImageScaled[visualsToScale.length);
		for (var i = 0; i < visualsToScale.length; i++)
		{
			var visualToScale = visualsToScale[i];
			var visualScaled = new VisualImageScaled(visualToScale, sizeToDraw);
			returnValues[i] = visualScaled;
		}
		return returnValues;
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var universe = uwpe.universe;
		var entity = uwpe.entity;
		var image = this.visualImage.image(universe);
		var entityPos = entity.locatable().loc.pos;
		this._posSaved.overwriteWith(entityPos);
		entityPos.subtract(this.sizeToDrawHalf);
		display.drawImageScaled(image, entityPos, this.sizeToDraw);
		entityPos.overwriteWith(this._posSaved);
	}

	public Image2 image(Universe universe)
	{
		return this.visualImage.image(universe);
	}

	public Coords sizeInPixels(Universe universe)
	{
		return this.sizeToDraw;
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

	public Coords[] coordsGroupToTranslate { return null; }

	public Transformalbe transform(Transform transformToApply)
	{
		return this; // todo
	}
}
