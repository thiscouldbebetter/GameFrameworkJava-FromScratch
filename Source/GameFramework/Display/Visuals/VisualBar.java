
package GameFramework.Display.Visuals;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class VisualBar implements Visual<VisualBar>
{
	public String abbreviation;
	public Coords size;
	public Color color;
	public DataBinding<Entity,Double> amountCurrent;
	public DataBinding<Entity,Double> amountThreshold;
	public DataBinding<Entity,Double> amountMax;
	public Double fractionBelowWhichToShow;
	public ValueBreakGroup colorForBorderAsValueBreakGroup;
	public DataBinding<Object,String> text;

	private Coords _drawPos;
	private Coords _sizeCurrent;
	private Coords _sizeHalf;

	public VisualBar
	(
		String abbreviation,
		Coords size,
		Color color,
		DataBinding<Entity,Double> amountCurrent,
		DataBinding<Entity,Double> amountThreshold,
		DataBinding<Entity,Double> amountMax,
		Double fractionBelowWhichToShow,
		ValueBreakGroup colorForBorderAsValueBreakGroup,
		DataBinding<Object,String> text
	)
	{
		this.abbreviation = abbreviation;
		this.size = size;
		this.color = color;
		this.amountCurrent = amountCurrent;
		this.amountThreshold = amountThreshold;
		this.amountMax = amountMax;
		this.fractionBelowWhichToShow = fractionBelowWhichToShow;
		this.colorForBorderAsValueBreakGroup = colorForBorderAsValueBreakGroup;
		this.text = text;

		this._drawPos = Coords.create();
		this._sizeCurrent = this.size.clone();
		this._sizeHalf = this.size.clone().half();
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var wasVisible = false;

		var entity = uwpe.entity;
		var pos = this._drawPos.overwriteWith
		(
			entity.locatable().loc.pos
		).subtract(this._sizeHalf);
		var _amountCurrent =
			(double)(this.amountCurrent.contextSet(entity).get());
		var _amountMax =
			(double)(this.amountMax.contextSet(entity).get());
		var fractionCurrent = _amountCurrent / _amountMax;

		var shouldShow =
		(
			fractionCurrent < this.fractionBelowWhichToShow
		);

		if (shouldShow)
		{
			wasVisible = true;

			var widthCurrent = fractionCurrent * this.size.x;
			this._sizeCurrent.x = widthCurrent;
			display.drawRectangle
			(
				pos, this._sizeCurrent, this.color, null, false
			);

			Color colorForBorder = null;
			if (this.colorForBorderAsValueBreakGroup == null)
			{
				colorForBorder = Color.Instances().White;
			}
			else
			{
				colorForBorder =
					(Color)(this.colorForBorderAsValueBreakGroup.valueAtPosition(fractionCurrent));
			}

			if (this.amountThreshold != null)
			{
				var thresholdFraction = this.amountThreshold.contextSet(entity).get();
				this._sizeCurrent.x = thresholdFraction * this.size.x;
				display.drawRectangle
				(
					this._sizeCurrent, // pos
					new Coords(1, this.size.y, 0), // size
					this.color, null, false
				);
			}

			display.drawRectangle
			(
				pos, this.size, null, colorForBorder, false
			);

			pos.add(this._sizeHalf);

			String text;
			if (this.text == null)
			{
				var remainingOverMax =
					Math.round(_amountCurrent) + "/" + _amountMax;

				text =
				(
					this.abbreviation == null
					? ""
					: (this.abbreviation + ":")
				) + remainingOverMax;
			}
			else
			{
				text = this.text.get();
			}
			display.drawText
			(
				text,
				this.size.y, // fontHeightInPixels
				pos,
				colorForBorder,
				Color.byName("Black"), // colorOutline,
				false, // areColorsReversed
				true, // isCentered
				null // widthMaxInPixels
			);
		}
	}

	// Clonable.

	public VisualBar clone()
	{
		return this; // todo
	}

	public VisualBar overwriteWith(VisualBar other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }
	
	public VisualBar transform(Transform transformToApply)
	{
		return this; // todo
	}
}
