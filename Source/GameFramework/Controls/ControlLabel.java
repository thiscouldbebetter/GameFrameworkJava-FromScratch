
package GameFramework.Controls;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;

public class ControlLabel extends ControlBase
{
	public boolean isTextCentered;
	private Object _text;

	public ControlBase parent;

	private Coords _drawPos;

	public ControlLabel
	(
		String name, Coords pos, Coords size, boolean isTextCentered,
		Object text, double fontHeightInPixels
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this.isTextCentered = isTextCentered;
		this._text = text;

		// Helper variables.

		this._drawPos = Coords.create();
	}

	public static ControlLabel fromPosAndText(Coords pos, Object text)
	{
		return new ControlLabel
		(
			null, //name
			pos,
			null, // size
			false, // isTextCentered
			text,
			10 // fontHeightInPixels
		);
	}

	public static ControlLabel from5
	(
		String name, Coords pos, Coords size, boolean isTextCentered,
		Object text
	)
	{
		return new ControlLabel
		(
			name,
			pos,
			size,
			isTextCentered,
			text,
			null // fontHeightInPixels
		);
	}

	public boolean actionHandle(String actionName)
	{
		return false; // wasActionHandled
	}

	public boolean isEnabled()
	{
		return false;
	}

	public boolean mouseClick(Coords pos)
	{
		return false;
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
	}

	public String text()
	{
		return (this._text.get == null ? this._text : this._text.get() );
	}

	// drawable

	public void draw
	(
		Universe universe, Display display, Disposition drawLoc,
		ControlStyle style
	)
	{
		var drawPos = this._drawPos.overwriteWith(drawLoc.pos).add(this.pos);
		var style = (style != null ? style : this.style(universe) );
		var text = this.text();

		if (text != null)
		{
			var textAsLines = ("" + text).split("\n");
			var widthMaxInPixels = (this.size == null ? null : this.size.x);
			for (var i = 0; i < textAsLines.length; i++)
			{
				var textLine = textAsLines[i];
				display.drawText
				(
					textLine,
					this.fontHeightInPixels,
					drawPos,
					style.colorBorder,
					style.colorFill, // colorOutline
					null, // areColorsReversed
					this.isTextCentered,
					widthMaxInPixels
				);

				drawPos.y += this.fontHeightInPixels;
			}
		}
	}
}
