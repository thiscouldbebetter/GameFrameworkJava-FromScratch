
package Controls;

import Display.*;
import Geometry.*;
import Model.*;

import java.util.*;

public class ControlButton<TContext> extends ControlBase
{
	public String text;
	public boolean hasBorder;
	public DataBinding<TContext,Boolean> _isEnabled;
	public Runnable click;
	public Object context;
	public boolean canBeHeldDown;

	private Disposition _drawLoc;
	private Coords _sizeHalf;

	public ControlButton
	(
		String name,
		Coords pos,
		Coords size,
		String text,
		double fontHeightInPixels,
		boolean hasBorder,
		DataBinding<TContext,Boolean> isEnabled,
		Runnable click,
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
		this.canBeHeldDown = canBeHeldDown;

		// Helper variables.
		this._drawLoc = Disposition.create();
		this._sizeHalf = Coords.create();
	}

	public static <TContext> ControlButton<TContext> from8
	(
		String name,
		Coords pos,
		Coords size,
		String text,
		double fontHeightInPixels,
		boolean hasBorder,
		DataBinding<TContext,Boolean> isEnabled,
		Runnable click
	)
	{
		return new ControlButton<TContext>
		(
			name, pos, size, text, fontHeightInPixels, hasBorder,
			isEnabled, click, null, false
		);
	}

	public static <TContext> ControlButton<TContext> from9
	(
		String name,
		Coords pos,
		Coords size,
		String text,
		double fontHeightInPixels,
		boolean hasBorder,
		DataBinding<TContext,Boolean> isEnabled,
		Runnable click,
		Object context
	)
	{
		return new ControlButton<TContext>
		(
			name, pos, size, text, fontHeightInPixels, hasBorder,
			isEnabled, click, context, false
		);
	}

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		if (actionNameToHandle == ControlActionNames.Instances().ControlConfirm)
		{
			this.click.run(); // this.context);
		}

		return (this.canBeHeldDown == false); // wasActionHandled
	}

	public boolean isEnabled()
	{
		return this._isEnabled.get();
	}

	// events

	public boolean mouseClick(Coords clickPos)
	{
		if (this.isEnabled())
		{
			this.click.run(); // (this.context);
		}
		return (this.canBeHeldDown == false); // wasClickHandled
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
		return this;
	}

	// drawable

	public void draw
	(
		Universe universe, Display display, Disposition drawLoc,
		ControlStyle style
	)
	{
		var drawPos = this._drawLoc.overwriteWith(drawLoc).pos;
		drawPos.add(this.pos);

		var isEnabled = this.isEnabled();
		var isHighlighted = this.isHighlighted && isEnabled;

		style = (style != null ? style : this.style(universe) );
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
