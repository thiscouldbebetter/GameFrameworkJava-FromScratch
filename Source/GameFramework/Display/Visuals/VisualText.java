
package GameFramework.Display.Visuals;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualText implements Visual
{
	public DataBinding<Object,String> _text;
	public boolean shouldTextContextBeReset;
	public Color colorFill;
	public Color colorBorder;
	public double heightInPixels;

	private UniverseWorldPlaceEntities _universeWorldPlaceEntities;

	public VisualText
	(
		DataBinding<Object,String> text,
		boolean shouldTextContextBeReset,
		Double heightInPixels,
		Color colorFill,
		Color colorBorder
	)
	{
		this._text = text;
		this.shouldTextContextBeReset = shouldTextContextBeReset;
		this.heightInPixels = (heightInPixels != null ? heightInPixels : 10);
		this.colorFill = colorFill;
		this.colorBorder = colorBorder;

		this._universeWorldPlaceEntities = UniverseWorldPlaceEntities.create();
	}

	public static VisualText fromTextAndColor(String text, Color colorFill)
	{
		return new VisualText
		(
			DataBinding.fromContext(text),
			false, // shouldTextContextBeReset
			null, // heightInPixels
			colorFill,
			null // colorBorder
		);
	}

	public static VisualText fromTextAndColors
	(
		String text, Color colorFill, Color colorBorder
	)
	{
		return new VisualText
		(
			DataBinding.fromContext(text),
			false, // shouldTextContextBeReset
			null, // heightInPixels
			colorFill,
			colorBorder
		);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;

		var text = this.text(uwpe, display);
		display.drawText
		(
			text,
			this.heightInPixels,
			entity.locatable().loc.pos,
			this.colorFill,
			this.colorBorder,
			false, // areColorsReversed
			true, // isCentered
			null // widthMaxInPixels
		);
	}

	public String text(UniverseWorldPlaceEntities uwpe, Display display)
	{
		if (this.shouldTextContextBeReset)
		{
			this._universeWorldPlaceEntities.overwriteWith
			(
				uwpe
			);
			this._text.contextSet(this._universeWorldPlaceEntities);
		}

		var returnValue = this._text.get();

		return returnValue;
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

	// transformable

	public Coords[] coordsGroupToTranslate() { return new Coords[] {}; }

	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}
