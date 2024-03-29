
package Controls;

import Display.*;
import Geometry.*;
import Helpers.*;
import Model.*;
import Utility.*;

public class ControlScrollbar<TContext,TItem> extends ControlBase
{
	public double itemHeight;
	private DataBinding<TContext,TItem[]> _items;
	private double _sliderPosInItems;

	public ControlButton buttonScrollDown;
	public ControlButton buttonScrollUp;
	public Coords handleSize;
	public double windowSizeInItems;

	private Coords _drawPos;

	public ControlScrollbar
	(
		Coords pos,
		Coords size,
		double fontHeightInPixels,
		double itemHeight,
		DataBinding<TContext,TItem[]> items,
		double sliderPosInItems
	)
	{
		super(null, pos, size, fontHeightInPixels);
		this.itemHeight = itemHeight;
		this._items = items;
		this._sliderPosInItems = sliderPosInItems;

		this.windowSizeInItems = Math.floor(this.size.y / itemHeight);

		this.handleSize = new Coords(this.size.x, this.size.x, 0);

		this.buttonScrollUp = ControlButton.from8
		(
			"buttonScrollUp", // name
			Coords.create(), // pos
			this.handleSize.clone(), // size
			"-", // text
			this.fontHeightInPixels,
			true, // hasBorder
			DataBinding.fromTrue(), // isEnabled
			() -> this.scrollUp() // click
		);

		this.buttonScrollDown = ControlButton.from8
		(
			"buttonScrollDown", // name
			new Coords(0, this.size.y - this.handleSize.y, 0), // pos
			this.handleSize.clone(), // size
			"+", // text
			this.fontHeightInPixels,
			true, // hasBorder
			DataBinding.fromTrue(), // hasBorder
			() -> this.scrollDown() // click
		);

		// Helper variables.
		this._drawPos = Coords.create();
	}

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		return true;
	}

	public boolean isVisible()
	{
		return (this.windowSizeInItems < this.items().length);
	}

	public TItem[] items()
	{
		return this._items.get();
	}

	public boolean mouseClick(Coords pos)
	{
		return false;
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.handleSize.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
		this.buttonScrollUp.scalePosAndSize(scaleFactor);
		this.buttonScrollDown.scalePosAndSize(scaleFactor);
	}

	public void scrollDown()
	{
		var sliderPosInItems = (int)NumberHelper.trimToRangeMinMax
		(
			this.sliderPosInItems() + 1, 0, this.sliderMaxInItems()
		);

		this._sliderPosInItems = sliderPosInItems;
	}

	public void scrollUp()
	{
		var sliderPosInItems = (int)NumberHelper.trimToRangeMinMax
		(
			this.sliderPosInItems() - 1, 0, this.sliderMaxInItems()
		);

		this._sliderPosInItems = sliderPosInItems;
	}

	public Coords slideSizeInPixels()
	{
		var slideSizeInPixels = new Coords
		(
			this.handleSize.x,
			this.size.y - 2 * this.handleSize.y,
			0
		);

		return slideSizeInPixels;
	}

	public double sliderPosInItems()
	{
		return this._sliderPosInItems;
	}

	public int sliderMaxInItems()
	{
		return this.items().length - (int)Math.floor(this.windowSizeInItems);
	}

	public Coords sliderPosInPixels()
	{
		var sliderPosInPixels = new Coords
		(
			this.size.x - this.handleSize.x,
			this.handleSize.y
				+ this.sliderPosInItems()
				* this.slideSizeInPixels().y
				/ this.items().length,
			0
		);

		return sliderPosInPixels;
	}

	public Coords sliderSizeInPixels()
	{
		var sliderSizeInPixels = this.slideSizeInPixels().multiply
		(
			new Coords(1, this.windowSizeInItems / this.items().length, 0)
		);

		return sliderSizeInPixels;
	}

	// drawable

	public void draw
	(
		Universe universe, Display display, Disposition drawLoc,
		ControlStyle style
	)
	{
		if (this.isVisible())
		{
			style = (style != null ? style : this.style(universe) );
			var colorFore = (this.isHighlighted ? style.colorFill : style.colorBorder);
			var colorBack = (this.isHighlighted ? style.colorBorder : style.colorFill);

			var drawPos = this._drawPos.overwriteWith(drawLoc.pos).add(this.pos);
			display.drawRectangle(drawPos, this.size, colorFore, null, false);

			drawLoc.pos.add(this.pos);
			this.buttonScrollDown.draw(universe, display, drawLoc, style);
			this.buttonScrollUp.draw(universe, display, drawLoc, style);

			var sliderPosInPixels = this.sliderPosInPixels().add(drawPos);
			var sliderSizeInPixels = this.sliderSizeInPixels();

			display.drawRectangle
			(
				sliderPosInPixels, sliderSizeInPixels,
				colorBack, colorFore,
				false // areColorsReversed
			);
		}
	}
}
