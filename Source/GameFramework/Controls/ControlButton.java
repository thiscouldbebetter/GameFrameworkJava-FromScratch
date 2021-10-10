package GameFramework.Controls;

import GameFramework.Geometry.*;

public class ControlButton extends ControlBase
{
	public String text;
	public boolean hasBorder;
	public _any isEnabled;
	public Object click;
	public Object context;
	public boolean canBeHeldDown;

	_Disposition drawLoc;
	_Coords sizeHalf;

	public ControlButton
	(
		String name,
		Coords pos,
		Coords size,
		String text,
		number fontHeightInPixels,
		boolean hasBorder,
		Object isEnabled,
		Object click,
		Object context,
		boolean canBeHeldDown
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this.text = text;
		this.hasBorder = hasBorder;
		this._isEnabled = isEnabled;
		this.click = click;
		this.context = context;
		this.canBeHeldDown = (canBeHeldDown == null ? false : canBeHeldDown);

		// Helper variables.
		this._drawLoc = Disposition.create();
		this._sizeHalf = Coords.create();
	}

	static from8
	(
		String name,
		Coords pos,
		Coords size,
		String text,
		number fontHeightInPixels,
		boolean hasBorder,
		Object isEnabled,
		Object click
	)
	{
		return new ControlButton
		(
			name, pos, size, text, fontHeightInPixels, hasBorder,
			isEnabled, click, null, null
		);
	}

	static from9
	(
		String name,
		Coords pos,
		Coords size,
		String text,
		number fontHeightInPixels,
		boolean hasBorder,
		Object isEnabled,
		Object click,
		Object context
	)
	{
		return new ControlButton
		(
			name, pos, size, text, fontHeightInPixels, hasBorder,
			isEnabled, click, context, null
		);
	}

	actionHandle(String actionNameToHandle, Universe universe)
	{
		if (actionNameToHandle == ControlActionNames.Instances().ControlConfirm)
		{
			this.click(this.context);
		}

		return (this.canBeHeldDown == false); // wasActionHandled
	}

	isEnabled()
	{
		return (this._isEnabled.get == null ? this._isEnabled : this._isEnabled.get() );
	}

	// events

	mouseClick(Coords clickPos)
	{
		if (this.isEnabled())
		{
			this.click(this.context);
		}
		return (this.canBeHeldDown == false); // wasClickHandled
	}

	scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
	}

	// drawable

	draw(Universe universe, Display display, Disposition drawLoc, ControlStyle style)
	{
		var drawPos = this._drawLoc.overwriteWith(drawLoc).pos;
		drawPos.add(this.pos);

		var isEnabled = this.isEnabled();
		var isHighlighted = this.isHighlighted && isEnabled;

		style = style || this.style(universe);
		var colorFill = style.colorFill;
		var colorBorder = style.colorBorder;

		if (this.hasBorder)
		{
			display.drawRectangle
			(
				drawPos, this.size,
				colorFill,
				colorBorder,
				isHighlighted // areColorsReversed
			);
		}

		drawPos.add(this._sizeHalf.overwriteWith(this.size).half());

		var colorText = (isEnabled ? colorBorder : style.colorDisabled);

		display.drawText
		(
			this.text,
			this.fontHeightInPixels,
			drawPos,
			colorText,
			colorFill,
			isHighlighted,
			true, // isCentered
			this.size.x // widthMaxInPixels
		);
	}
}

}