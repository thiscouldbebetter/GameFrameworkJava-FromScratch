package Display.Visuals;

import Display.*;
import Geometry.*;
import Geometry.Transforms.*;
import Model.*;

public class VisualRectangle implements Visual<VisualRectangle>
{
	public Coords size;
	public Color colorFill;
	public Color colorBorder;
	public boolean isCentered;

	private Coords sizeHalf;

	private Coords _drawPos;

	public VisualRectangle
	(
		Coords size,
		Color colorFill,
		Color colorBorder,
		boolean isCentered
	)
	{
		this.size = size;
		this.colorFill = colorFill;
		this.colorBorder = colorBorder;
		this.isCentered = isCentered;

		this.sizeHalf = this.size.clone().half();

		this._drawPos = Coords.create();
	}

	public static VisualRectangle default_()
	{
		// For rapid prototyping.
		return VisualRectangle.fromColorFill(Color.Instances().Cyan);
	}

	public static VisualRectangle fromColorFill(Color colorFill)
	{
		// For rapid prototyping.
		return new VisualRectangle
		(
			Coords.fromXY(1, 1).multiplyScalar(10), null, colorFill, true
		);
	}

	public static VisualRectangle fromSize(Coords size)
	{
		// For rapid prototyping.
		return new VisualRectangle
		(
			size, null, Color.Instances().Cyan, true
		);
	}

	public static VisualRectangle fromSizeAndColorFill
	(
		Coords size, Color colorFill
	)
	{
		return new VisualRectangle(size, colorFill, null, true);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		var drawPos = this._drawPos.overwriteWith
		(
			entity.locatable().loc.pos
		);

		if (this.isCentered)
		{
			drawPos.subtract(this.sizeHalf);
		}

		display.drawRectangle
		(
			drawPos, this.size, this.colorFill, this.colorBorder, false
		);
	}

	// Clonable.

	public VisualRectangle clone()
	{
		return this; // todo
	}

	public VisualRectangle overwriteWith(VisualRectangle other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public VisualRectangle transform(Transform transformToApply)
	{
		return this; // todo
	}
}
