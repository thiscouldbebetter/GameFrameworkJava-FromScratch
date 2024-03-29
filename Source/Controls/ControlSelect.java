
package Controls;

import Display.*;
import Geometry.*;
import Helpers.*;
import Model.*;
import Utility.*;

public class ControlSelect<TContext,TValue,TItem> extends ControlBase
{
	private DataBinding<TContext,TValue> _valueSelected;
	private DataBinding<TContext,TItem[]> _options;
	public DataBinding<TItem,TValue> bindingForOptionValues;
	public DataBinding<TItem,String> bindingForOptionText;

	public Integer indexOfOptionSelected;

	private Coords _drawPos;
	private Coords _sizeHalf;

	public ControlSelect
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<TContext,TValue> valueSelected,
		DataBinding<TContext,TItem[]> options,
		DataBinding<TItem,TValue> bindingForOptionValues,
		DataBinding<TItem,String> bindingForOptionText,
		double fontHeightInPixels
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this._valueSelected = valueSelected;
		this._options = options;
		this.bindingForOptionValues = bindingForOptionValues;
		this.bindingForOptionText = bindingForOptionText;

		this.indexOfOptionSelected = null;
		var valueSelectedActual = this.valueSelected();
		var optionsActual = this._options.get();
		for (var i = 0; i < optionsActual.length; i++)
		{
			var option = optionsActual[i];
			var optionValue = this.bindingForOptionValues.contextSet
			(
				option
			).get();

			if (optionValue == valueSelectedActual)
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

	public TItem optionSelected()
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

	public TItem[] options()
	{
		return this._options.get();
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
	}

	public TValue valueSelected()
	{
		var returnValue =
		(
			this._valueSelected == null
			? null
			: this._valueSelected.get()
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
