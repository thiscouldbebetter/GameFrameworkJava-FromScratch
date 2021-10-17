
package GameFramework.Controls;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class ControlSelect extends ControlBase
{
	private Object _valueSelected;
	private Object _options;
	public DataBinding<Object,Object> bindingForOptionValues;
	public DataBinding<Object,String> bindingForOptionText;

	public int indexOfOptionSelected;

	private Coords _drawPos;
	private Coords _sizeHalf;

	public ControlSelect
	(
		String name,
		Coords pos,
		Coords size,
		Object valueSelected,
		Object options,
		DataBinding<Object,Object> bindingForOptionValues,
		DataBinding<Object,String> bindingForOptionText,
		double fontHeightInPixels
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this._valueSelected = valueSelected;
		this._options = options;
		this.bindingForOptionValues = bindingForOptionValues;
		this.bindingForOptionText = bindingForOptionText;

		this.indexOfOptionSelected = null;
		var valueSelected = this.valueSelected();
		var options = this.options();
		for (var i = 0; i < options.length; i++)
		{
			var option = options[i];
			var optionValue = this.bindingForOptionValues.contextSet
			(
				option
			).get();

			if (optionValue == valueSelected)
			{
				this.indexOfOptionSelected = i;
				break;
			}
		}

		// Helper variables.
		this._drawPos = Coords.create();
		this._sizeHalf = Coords.create();
	}

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		var controlActionNames = ControlActionNames.Instances();
		if (actionNameToHandle == controlActionNames.ControlDecrement)
		{
			this.optionSelectedNextInDirection(-1);
		}
		else if
		(
			actionNameToHandle == controlActionNames.ControlIncrement
			|| actionNameToHandle == controlActionNames.ControlConfirm
		)
		{
			this.optionSelectedNextInDirection(1);
		}
		return true; // wasActionHandled
	}

	public boolean mouseClick(Coords clickPos)
	{
		this.optionSelectedNextInDirection(1);
		return true; // wasClickHandled
	}

	public Object optionSelected()
	{
		var optionSelected =
		(
			this.indexOfOptionSelected == null
			? null
			: this.options()[this.indexOfOptionSelected]
		);
		return optionSelected;
	}

	public void optionSelectedNextInDirection(int direction)
	{
		var options = this.options();

		this.indexOfOptionSelected = NumberHelper.wrapToRangeMinMax
		(
			this.indexOfOptionSelected + direction, 0, options.length
		);

		var optionSelected = this.optionSelected();
		var valueToSelect =
		(
			optionSelected == null
			? null
			: this.bindingForOptionValues.contextSet(optionSelected).get()
		);

		this._valueSelected.set(valueToSelect);
	}

	public Object[] options()
	{
		return this._options.get();
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
	}

	public Object valueSelected()
	{
		var returnValue =
		(
			this._valueSelected == null
			? null
			: (this._valueSelected.get == null ? this._valueSelected : this._valueSelected.get() )
		);

		return returnValue;
	}

	// drawable

	public void draw(Universe universe, Display display, Disposition drawLoc)
	{
		var drawPos = this._drawPos.overwriteWith(drawLoc.pos).add(this.pos);

		var style = this.style(universe);

		display.drawRectangle
		(
			drawPos, this.size,
			style.colorFill,
			style.colorBorder,
			this.isHighlighted // areColorsReversed
		);

		drawPos.add(this._sizeHalf.overwriteWith(this.size).half());

		var optionSelected = this.optionSelected();
		var text =
		(
			optionSelected == null
			? "-"
			: this.bindingForOptionText.contextSet(optionSelected).get()
		);

		display.drawText
		(
			text,
			this.fontHeightInPixels,
			drawPos,
			style.colorBorder,
			style.colorFill,
			this.isHighlighted,
			true, // isCentered
			this.size.x // widthMaxInPixels
		);
	}
}
