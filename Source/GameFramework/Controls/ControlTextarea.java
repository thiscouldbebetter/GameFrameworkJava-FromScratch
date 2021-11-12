
package GameFramework.Controls;

import java.util.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*;
import GameFramework.Input.*;
import GameFramework.Model.*;

public class ControlTextarea extends ControlBase
{
	private DataBinding<Object,String> _text;
	private DataBinding<Object,Boolean> _isEnabled;

	public Integer charCountMax;
	public Integer cursorPos;
	public double lineSpacing;
	public ControlScrollbar scrollbar;

	private Coords _drawPos;
	private Disposition _drawLoc;
	private int _indexOfLineSelected;
	private Coords _mouseClickPos;
	private List<String> _textAsLines;

	public ControlTextarea
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object,String> text,
		double fontHeightInPixels,
		DataBinding<Object,Boolean> isEnabled
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this._text = text;
		this._isEnabled = isEnabled;

		this.charCountMax = null;
		this.cursorPos = null;

		this.lineSpacing = 1.2 * this.fontHeightInPixels; // hack

		var scrollbarWidth = this.lineSpacing;
		this.scrollbar = new ControlScrollbar
		(
			new Coords(this.size.x - scrollbarWidth, 0, 0), // pos
			new Coords(scrollbarWidth, this.size.y, 0), // size
			this.fontHeightInPixels,
			this.lineSpacing, // itemHeight
			DataBinding.fromContextAndGet
			(
				this, (ControlTextarea c) -> c.textAsLines()
			),
			0 // sliderPosInItems
		);

		// Helper variables.
		this._drawPos = Coords.create();
		this._drawLoc = Disposition.fromPos(this._drawPos);
		this._mouseClickPos = Coords.create();
	}

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		var text = this.text(null);

		var controlActionNames = ControlActionNames.Instances();
		if
		(
			actionNameToHandle == controlActionNames.ControlCancel
			|| actionNameToHandle == Input.Names().Backspace
		)
		{
			this.text(text.substring(0, text.length() - 1));

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
		/* // todo - No-keyboard support.
		else if
		(
			actionNameToHandle == controlActionNames.ControlIncrement
			|| actionNameToHandle == controlActionNames.ControlDecrement
		)
		{
			// This is a bit counterintuitive.
			var direction = (actionNameToHandle == controlActionNames.ControlIncrement ? -1 : 1);

			var charCodeAtCursor =
			(
				this.cursorPos < text.length ? text.charAt(this.cursorPos) : "A".charAt(0) - 1
			);

			if (charCodeAtCursor == "Z".charAt(0) && direction == 1)
			{
				charCodeAtCursor = "a".charAt(0);
			}
			else if (charCodeAtCursor == "a".charAt(0) && direction == -1)
			{
				charCodeAtCursor = "Z".charAt(0);
			}
			else
			{
				charCodeAtCursor = charCodeAtCursor + direction;
			}

			charCodeAtCursor = NumberHelper.wrapToRangeMinMax
			(
				charCodeAtCursor,
				"A".charAt(0),
				"z".charAt(0) + 1
			);

			var charAtCursor = "" + (char)charCodeAtCursor;

			this.text
			(
				text.substr(0, this.cursorPos)
					+ charAtCursor
					+ text.substr(this.cursorPos + 1)
			);
		}
		*/
		else if
		(
			actionNameToHandle.length() == 1
			|| actionNameToHandle.startsWith("_")
		) // printable character
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

			if (this.charCountMax == null || text.length() < this.charCountMax)
			{
				var textEdited =
					text.substring(0, this.cursorPos)
						+ actionNameToHandle
						+ text.substring(this.cursorPos);

				text = this.text(textEdited);

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
		this.cursorPos = this.text(null).length();
	}

	public void focusLose()
	{
		this.isHighlighted = false;
		this.cursorPos = null;
	}

	public int indexOfFirstLineVisible()
	{
		return (int)this.scrollbar.sliderPosInItems();
	}

	public int indexOfLastLineVisible()
	{
		var returnValue = (int)
		(
			this.indexOfFirstLineVisible()
			+ Math.floor(this.scrollbar.windowSizeInItems) - 1.0;
		);
		return returnValue;
	}

	public int indexOfLineSelected(Integer valueToSet)
	{
		var returnValue = valueToSet;
		if (valueToSet == null)
		{
			returnValue = this._indexOfLineSelected;
		}
		else
		{
			this._indexOfLineSelected = valueToSet;
		}
		return returnValue;
	}

	public boolean isEnabled()
	{
		return (this._isEnabled.get());
	}

	public String text(String value)
	{
		if (value != null)
		{
			this._text.set(value);
		}

		return this._text.get();
	}

	public List<String> textAsLines()
	{
		this._textAsLines = new ArrayList<String>();

		var charWidthInPixels = this.fontHeightInPixels / 2; // hack
		var charsPerLine = (int)Math.floor(this.size.x / charWidthInPixels);
		var textComplete = this.text(null);
		var textLength = textComplete.length();
		var i = 0;
		while (i < textLength)
		{
			var line = textComplete.substring(i, charsPerLine);
			this._textAsLines.add(line);
			i += charsPerLine;
		}

		return this._textAsLines;
	}

	public boolean mouseClick(Coords clickPos)
	{
		clickPos = this._mouseClickPos.overwriteWith(clickPos);

		if (clickPos.x - this.pos.x > this.size.x - this.scrollbar.handleSize.x)
		{
			if (clickPos.y - this.pos.y <= this.scrollbar.handleSize.y)
			{
				this.scrollbar.scrollUp();
			}
			else if (clickPos.y - this.pos.y >= this.scrollbar.size.y - this.scrollbar.handleSize.y)
			{
				this.scrollbar.scrollDown();
			}
			else
			{
				// todo

				/*
				var clickPosRelativeToSlideInPixels = clickPos.subtract
				(
					this.scrollbar.pos
				).subtract
				(
					new Coords(0, this.scrollbar.handleSize.y, 0)
				);
				*/
			}
		}
		else
		{
			var offsetOfLineClicked = clickPos.y - this.pos.y;
			var indexOfLineClicked =
				this.indexOfFirstLineVisible()
					+ Math.floor
					(
						offsetOfLineClicked
						/ this.lineSpacing
					);

			var lines = this.textAsLines();
			if (indexOfLineClicked < lines.size())
			{
				this.indexOfLineSelected((int)indexOfLineClicked);
			}
		}

		return true; // wasActionHandled
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
		this.lineSpacing *= scaleFactor.y;
		this.scrollbar.scalePosAndSize(scaleFactor);
	}

	// drawable

	public void draw
	(
		Universe universe, Display display, Disposition drawLoc,
		ControlStyle style
	)
	{
		drawLoc = this._drawLoc.overwriteWith(drawLoc);
		var drawPos = this._drawPos.overwriteWith(drawLoc.pos).add(this.pos);

		style = style != null ? style : this.style(universe);
		var colorFore = (this.isHighlighted ? style.colorFill : style.colorBorder);
		var colorBack = (this.isHighlighted ? style.colorBorder : style.colorFill);

		display.drawRectangle
		(
			drawPos,
			this.size,
			colorBack, // fill
			style.colorBorder, // border
			false // areColorsReversed
		);

		var itemSizeY = this.lineSpacing;
		var textMarginLeft = 2;
		var itemPosY = drawPos.y;

		var lines = this.textAsLines();

		if (lines == null || lines.size() == 0)
		{
			return;
		}

		if (this.isHighlighted)
		{
			// todo - Cursor positioning.

			var lineIndexFinal = lines.size() - 1;
			var lineFinal = lines.get(lineIndexFinal);
			lines.set(lineIndexFinal, lineFinal + "_");
		}

		var numberOfLinesVisible = Math.floor(this.size.y / itemSizeY);
		var indexStart = this.indexOfFirstLineVisible();
		var indexEnd = indexStart + numberOfLinesVisible - 1;
		if (indexEnd >= lines.size())
		{
			indexEnd = lines.size() - 1;
		}

		var drawPos2 = new Coords(drawPos.x + textMarginLeft, itemPosY, 0);

		for (var i = indexStart; i <= indexEnd; i++)
		{
			var line = lines.get(i);

			display.drawText
			(
				line,
				this.fontHeightInPixels,
				drawPos2,
				colorFore,
				colorBack,
				false, // areColorsReversed
				false, // isCentered
				this.size.x // widthMaxInPixels
			);

			drawPos2.y += itemSizeY;
		}

		this.scrollbar.draw(universe, display, drawLoc, style);
	}
}
