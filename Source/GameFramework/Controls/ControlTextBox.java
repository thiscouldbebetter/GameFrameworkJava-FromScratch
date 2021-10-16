
package GameFramework.Controls;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*;

public class ControlTextBox extends ControlBase
{
	public DataBinding<Object,String> _text;
	public int numberOfCharsMax;
	public DataBinding<Object,Boolean> _isEnabled;

	public int cursorPos;

	private Coords _drawPos;
	private Coords _drawPosText;
	private Disposition _drawLoc;
	private Coords _textMargin;
	private Coords _textSize;

	public ControlTextBox
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object,String> text,
		double fontHeightInPixels,
		int numberOfCharsMax,
		DataBinding<Object,Boolean> isEnabled
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this._text = text;
		this.fontHeightInPixels = fontHeightInPixels;
		this.numberOfCharsMax = numberOfCharsMax;
		this._isEnabled = isEnabled;

		this.cursorPos = null;

		// Helper variables.
		this._drawPos = Coords.create();
		this._drawPosText = Coords.create();
		this._drawLoc = Disposition.fromPos(this._drawPos);
		this._textMargin = Coords.create();
		this._textSize = Coords.create();
	}

	public String text()
	{
		return this._text.get();
	}

	// events

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		var text = this.text(null, null);

		var controlActionNames = ControlActionNames.Instances();
		if
		(
			actionNameToHandle == controlActionNames.ControlCancel
			|| actionNameToHandle == Input.Names().Backspace
		)
		{
			this.text(text.substring(0, text.length() - 1), null);

			this.cursorPos = NumberHelper.wrapToRangeMinMax
			(
				this.cursorPos - 1, 0, text.length() + 1
			);
		}
		else if (actionNameToHandle == controlActionNames.ControlConfirm)
		{
			this.cursorPos = NumberHelper.wrapToRangeMinMax
			(
				this.cursorPos + 1, 0, text.length() + 1
			);
		}
		else if
		(
			actionNameToHandle == controlActionNames.ControlIncrement
			|| actionNameToHandle == controlActionNames.ControlDecrement
		)
		{
			// This is a bit counterintuitive.
			var direction =
			(
				actionNameToHandle == controlActionNames.ControlIncrement ? -1 : 1
			);

			var charAtCursor =
			(
				this.cursorPos < text.length()
				? text.charAt(this.cursorPos)
				: "A".charAt(0) - 1
			);

			if (charAtCursor == "Z".charAt(0) && direction == 1)
			{
				charAtCursor = "a".charAt(0);
			}
			else if (charAtCursor == "a".charAt(0) && direction == -1)
			{
				charAtCursor = "Z".charAt(0);
			}
			else
			{
				charAtCursor = charAtCursor + direction;
			}

			charAtCursor = NumberHelper.wrapToRangeMinMax
			(
				charAtCursor,
				"A".charAt(0),
				"z".charAt(0) + 1
			);

			var charAtCursor = String.fromCharCode(charAtCursor);

			var textEdited = text.substring(0, this.cursorPos)
				+ charAtCursor
				+ text.substring(this.cursorPos + 1);

			this.text(textEdited, null);
		}
		else if (actionNameToHandle.length == 1 || actionNameToHandle.startsWith("_") ) // printable character
		{
			if (actionNameToHandle.startsWith("_"))
			{
				if (actionNameToHandle == "_")
				{
					actionNameToHandle = " ";
				}
				else
				{
					actionNameToHandle = actionNameToHandle.substring(1);
				}
			}

			if
			(
				this.numberOfCharsMax == null
				|| text.length() < this.numberOfCharsMax
			)
			{
				var textEdited =
					text.substring(0, this.cursorPos)
						+ actionNameToHandle
						+ text.substring(this.cursorPos);

				text = this.text(textEdited, null);

				this.cursorPos = NumberHelper.wrapToRangeMinMax
				(
					this.cursorPos + 1, 0, text.length() + 1
				);
			}
		}

		return true; // wasActionHandled
	}

	public void focusGain()
	{
		this.isHighlighted = true;
		this.cursorPos = this.text(null, null).length;
	}

	public void focusLose()
	{
		this.isHighlighted = false;
		this.cursorPos = -1;
	}

	public boolean isEnabled()
	{
		return (this._isEnabled.get());
	}

	public boolean mouseClick(Coords mouseClickPos)
	{
		var parent = this.parent;
		var parentAsContainer = (ControlContainer)parent;
		parentAsContainer.indexOfChildWithFocus =
			parentAsContainer.children.indexOf(this);
		this.isHighlighted = true;
		return true;
	}

	public ControlTextBox scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
		return this;
	}

	// drawable

	public void draw(Universe universe, Display display, Disposition drawLoc)
	{
		var drawPos = this._drawPos.overwriteWith(drawLoc.pos).add(this.pos);
		var style = this.style(universe);

		var text = this.text(null, null);

		display.drawRectangle
		(
			drawPos, this.size,
			style.colorFill, style.colorBorder,
			this.isHighlighted // areColorsReversed
		);

		var textWidth =
			display.textWidthForFontHeight(text, this.fontHeightInPixels);
		var textSize =
			this._textSize.overwriteWithDimensions(textWidth, this.fontHeightInPixels, 0);
		var textMargin =
			this._textMargin.overwriteWith(this.size).subtract(textSize).half();
		var drawPosText =
			this._drawPosText.overwriteWith(drawPos).add(textMargin);

		display.drawText
		(
			text,
			this.fontHeightInPixels,
			drawPosText,
			style.colorBorder,
			style.colorFill,
			this.isHighlighted,
			false, // isCentered
			this.size.x // widthMaxInPixels
		);

		if (this.isHighlighted)
		{
			var textBeforeCursor = text.substring(0, this.cursorPos);
			var textAtCursor = text.substring(this.cursorPos, 1);
			var cursorX = display.textWidthForFontHeight
			(
				textBeforeCursor, this.fontHeightInPixels
			);
			var cursorWidth = display.textWidthForFontHeight
			(
				textAtCursor, this.fontHeightInPixels
			);
			drawPosText.x += cursorX;

			display.drawRectangle
			(
				drawPosText,
				new Coords(cursorWidth, this.fontHeightInPixels, 0), // size
				style.colorFill,
				style.colorFill, // ?
				null
			);

			display.drawText
			(
				textAtCursor,
				this.fontHeightInPixels,
				drawPosText,
				style.colorBorder,
				null, // colorBack
				false, // isHighlighted
				false, // isCentered
				this.size.x // widthMaxInPixels
			);
		}
	}
}
