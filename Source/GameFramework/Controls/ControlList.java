
package GameFramework.Controls;

import java.util.*;
import java.util.function.*;

import GameFramework.Geometry.*;

public class ControlList extends ControlBase
{
	public List<Object> items;
	public DataBinding<Object,String> bindingForItemText;
	public DataBinding<Object,Object> bindingForItemSelected;
	public DataBinding<Object,Object> bindingForItemValue;
	public DataBinding<Object,Boolean> bindingForIsEnabled;
	public Consumer<Universe> confirm;
	public double widthInItems;

	public boolean isHighlighted;
	public Coords _itemSpacing;
	public ControlBase parent;
	public ControlScrollbar scrollbar;

	private Disposition _drawLoc;
	private Coords _drawPos;
	private Object _itemSelected;
	private Coords _mouseClickPos;

	public ControlList
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object,Object[]> items,
		DataBinding<Object,String> bindingForItemText,
		double fontHeightInPixels,
		DataBinding<Object,Object> bindingForItemSelected,
		DataBinding<Object,Object> bindingForItemValue,
		DataBinding<Object,Boolean> bindingForIsEnabled,
		Consumer<Universe> confirm,
		double widthInItems
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this._items = items;
		this.bindingForItemText = bindingForItemText;
		this.bindingForItemSelected = bindingForItemSelected;
		this.bindingForItemValue = bindingForItemValue;
		this.bindingForIsEnabled =
			(bindingForIsEnabled != null ? bindingForIsEnabled : DataBinding.fromTrue());
		this.confirm = confirm;
		this.widthInItems = (widthInItems != null ? widthInItems : 1);

		var itemSpacingY = 1.2 * this.fontHeightInPixels; // hack
		this._itemSpacing = new Coords(0, itemSpacingY, 0);
		var scrollbarWidth = itemSpacingY;

		this.isHighlighted = false;

		this.scrollbar = new ControlScrollbar
		(
			new Coords(this.size.x - scrollbarWidth, 0, 0), // pos
			new Coords(scrollbarWidth, this.size.y, 0), // size
			this.fontHeightInPixels,
			itemSpacingY, // itemHeight
			this._items,
			0 // value
		);

		// Helper variables.
		this._drawPos = Coords.create();
		this._drawLoc = Disposition.fromPos(this._drawPos);
		this._mouseClickPos = Coords.create();
	}

	public static ControlList fromPosSizeAndItems
	(
		Coords pos, Coords size, DataBinding<Object, Object[]> items
	)
	{
		var returnValue = new ControlList
		(
			"", // name,
			pos,
			size,
			items,
			DataBinding.fromContext(null), // bindingForItemText,
			10, // fontHeightInPixels,
			null, // bindingForItemSelected,
			null, // bindingForItemValue,
			DataBinding.fromTrue(), // isEnabled
			null, null
		);

		return returnValue;
	}

	public static ControlList fromPosSizeItemsAndBindingForItemText
	(
		Coords pos,
		Coords size,
		DataBinding<Object,Object[]> items,
		DataBinding<Object,String> bindingForItemText
	)
	{
		var returnValue = new ControlList
		(
			"", // name,
			pos,
			size,
			items,
			bindingForItemText,
			10, // fontHeightInPixels,
			null, // bindingForItemSelected,
			null, // bindingForItemValue,
			DataBinding.fromTrue(), // isEnabled
			null, null
		);

		return returnValue;
	}

	public static ControlList from6
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object,Object[]> items,
		DataBinding<Object,String> bindingForItemText,
		double fontHeightInPixels
	{
		return new ControlList
		(
			name, pos, size, items, bindingForItemText, fontHeightInPixels,
			null, null, null, null, null
		);
	}

	public static ControlList from7
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object, Object[]> items,
		DataBinding<Object, String> bindingForItemText,
		number fontHeightInPixels,
		DataBinding<Object, Object> bindingForItemSelected,
	)
	{
		return new ControlList
		(
			name, pos, size, items, bindingForItemText, fontHeightInPixels,
			bindingForItemSelected, null, null, null, null
		);
	}

	public static ControlList from8
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object,Object[]> items,
		DataBinding<Object,String> bindingForItemText,
		number fontHeightInPixels,
		DataBinding<ObjectObject> bindingForItemSelected,
		DataBinding<ObjectObject> bindingForItemValue,
	)
	{
		return new ControlList
		(
			name, pos, size, items, bindingForItemText, fontHeightInPixels,
			bindingForItemSelected, bindingForItemValue, null, null, null
		);
	}

	static from9
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding items<Object,Object[]>,
		DataBinding bindingForItemText<Object,String>,
		number fontHeightInPixels,
		DataBinding bindingForItemSelected<Object,Object>,
		DataBinding bindingForItemValue<Object,Object>,
		DataBinding bindingForIsEnabled<Object,Boolean>
	): ControlList
	{
		return new ControlList
		(
			name, pos, size, items, bindingForItemText, fontHeightInPixels,
			bindingForItemSelected, bindingForItemValue, bindingForIsEnabled,
			null, null
		);
	}

	public static ControlList from10
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object,Object[]> items,
		DataBinding<Object,String> bindingForItemText,
		number fontHeightInPixels,
		DataBinding<Object,Object> bindingForItemSelected,
		DataBinding<Object,Object> bindingForItemValue,
		DataBinding<Object,Boolean> bindingForIsEnabled,
		Consumer<Universe> confirm
	)
	{
		return new ControlList
		(
			name, pos, size, items, bindingForItemText, fontHeightInPixels,
			bindingForItemSelected, bindingForItemValue, bindingForIsEnabled,
			confirm, null
		);
	}

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		var wasActionHandled = false;
		var controlActionNames = ControlActionNames.Instances();
		if (actionNameToHandle == controlActionNames.ControlIncrement)
		{
			this.itemSelectedNextInDirection(1);
			wasActionHandled = true;
		}
		else if (actionNameToHandle == controlActionNames.ControlDecrement)
		{
			this.itemSelectedNextInDirection(-1);
			wasActionHandled = true;
		}
		else if (actionNameToHandle == controlActionNames.ControlConfirm)
		{
			if (this.confirm != null)
			{
				this.confirm(universe);
				wasActionHandled = true;
			}
		}
		return wasActionHandled;
	}

	public int indexOfFirstItemVisible()
	{
		return this.indexOfFirstRowVisible() * this.widthInItems;
	}

	public int indexOfFirstRowVisible()
	{
		return this.scrollbar.sliderPosInItems();
	}

	public int indexOfItemSelected(int valueToSet)
	{
		var returnValue = valueToSet;
		var items = this.items();
		if (valueToSet == null)
		{
			returnValue = items.indexOf(this.itemSelected(null));
			if (returnValue == -1)
			{
				returnValue = null;
			}
		}
		else
		{
			var itemToSelect = items[valueToSet];
			this.itemSelected(itemToSelect);
		}
		return returnValue;
	}

	public int indexOfLastItemVisible()
	{
		return this.indexOfLastRowVisible() * this.widthInItems;
	}

	public int indexOfLastRowVisible()
	{
		var rowCountVisible = Math.floor(this.scrollbar.windowSizeInItems) - 1;
		var returnValue = this.indexOfFirstRowVisible() + rowCountVisible;
		return returnValue;
	}

	public boolean isEnabled()
	{
		return
		(
			this.bindingForIsEnabled == null
			? true
			: this.bindingForIsEnabled.get()
		);
	}

	public Object itemSelected(Object itemToSet)
	{
		var returnValue = itemToSet;

		if (itemToSet == null)
		{
			if (this.bindingForItemSelected == null)
			{
				returnValue = this._itemSelected;
			}
			else
			{
				returnValue =
				(
					this.bindingForItemSelected.get == null
					? this._itemSelected
					: this.bindingForItemSelected.get()
				);
			}
		}
		else
		{
			this._itemSelected = itemToSet;

			if (this.bindingForItemSelected != null)
			{
				var valueToSet;
				if (this.bindingForItemValue == null)
				{
					valueToSet = this._itemSelected;
				}
				else
				{
					valueToSet = this.bindingForItemValue.contextSet
					(
						this._itemSelected
					).get();
				}
				this.bindingForItemSelected.set(valueToSet);
			}
		}

		return returnValue;
	}

	public Object itemSelectedNextInDirection(int direction)
	{
		var items = this.items();
		var numberOfItems = items.length;

		var indexOfItemSelected = this.indexOfItemSelected(null);

		if (indexOfItemSelected == null)
		{
			if (numberOfItems > 0)
			{
				if (direction == 1)
				{
					indexOfItemSelected = 0;
				}
				else // if (direction == -1)
				{
					indexOfItemSelected = numberOfItems - 1;
				}
			}
		}
		else
		{
			indexOfItemSelected = NumberHelper.trimToRangeMinMax
			(
				indexOfItemSelected + direction, 0, numberOfItems - 1
			);
		}

		var itemToSelect =
		(
			indexOfItemSelected == null ? null : items[indexOfItemSelected]
		);
		this.itemSelected(itemToSelect);

		var indexOfFirstItemVisible = this.indexOfFirstItemVisible();
		var indexOfLastItemVisible = this.indexOfLastItemVisible();

		var indexOfItemSelected = this.indexOfItemSelected(null);
		if (indexOfItemSelected < indexOfFirstItemVisible)
		{
			this.scrollbar.scrollUp();
		}
		else if (indexOfItemSelected > indexOfLastItemVisible)
		{
			this.scrollbar.scrollDown();
		}

		var returnValue = this.itemSelected(null);
		return returnValue;
	}

	public Coords itemSpacing()
	{
		var scrollbarWidthVisible =
		(
			this.scrollbar.isVisible() ? this.scrollbar.size.x : 0
		);
		return this._itemSpacing.overwriteWithDimensions
		(
			(this.size.x - scrollbarWidthVisible) / this.widthInItems,
			this._itemSpacing.y,
			0
		);
	}

	public Object[] items()
	{
		return (this._items.get == null ? this._items : this._items.get());
	}

	public boolean mouseClick(Coords clickPos)
	{
		clickPos = this._mouseClickPos.overwriteWith(clickPos);

		var isClickPosInScrollbar =
			(clickPos.x - this.pos.x > this.size.x - this.scrollbar.handleSize.x);
		if (isClickPosInScrollbar)
		{
			if (clickPos.y - this.pos.y <= this.scrollbar.handleSize.y)
			{
				this.scrollbar.scrollUp();
			}
			else if (clickPos.y - this.pos.y >= this.scrollbar.size.y - this.scrollbar.handleSize.y)
			{
				this.scrollbar.scrollDown();
			}
		}
		else
		{
			var clickOffsetInPixels = clickPos.clone().subtract(this.pos);
			var clickOffsetInItems =
				clickOffsetInPixels.clone().divide(this.itemSpacing()).floor();
			var rowOfItemClicked =
				this.indexOfFirstRowVisible() + clickOffsetInItems.y;
			var indexOfItemClicked =
				rowOfItemClicked * this.widthInItems + clickOffsetInItems.x;

			var items = this.items();
			if (indexOfItemClicked < items.length)
			{
				var indexOfItemSelectedOld = this.indexOfItemSelected(null);
				if (indexOfItemClicked == indexOfItemSelectedOld)
				{
					if (this.confirm != null)
					{
						this.confirm(null); // todo
					}
				}
				else
				{
					this.indexOfItemSelected(indexOfItemClicked);
				}
			}
		}

		return true; // wasActionHandled
	}

	public void mouseEnter() {}

	public void mouseExit() {}

	public boolean mouseMove(Coords movePos) { return false; }

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);
		this.fontHeightInPixels *= scaleFactor.y;
		this._itemSpacing.multiply(scaleFactor);
		this.scrollbar.scalePosAndSize(scaleFactor);

		return this;
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

		var style = style || this.style(universe);
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

		var textMarginLeft = 2;

		var items = this.items();

		if (items == null)
		{
			return;
		}

		var indexStart = this.indexOfFirstItemVisible();
		var indexEnd = this.indexOfLastItemVisible();
		if (indexEnd >= items.length)
		{
			indexEnd = items.length - 1;
		}

		var itemSelected = this.itemSelected(null);

		var drawPos2 = Coords.create();

		for (var i = indexStart; i <= indexEnd; i++)
		{
			var item = items[i];

			var iOffset = i - indexStart;
			var offsetInItems = new Coords
			(
				iOffset % this.widthInItems,
				Math.floor(iOffset / this.widthInItems),
				0
			);

			drawPos2.overwriteWith
			(
				this.itemSpacing()
			).multiply
			(
				offsetInItems
			).add
			(
				drawPos
			)

			if (item == itemSelected)
			{
				display.drawRectangle
				(
					drawPos2,
					this.itemSpacing(),
					colorFore, // colorFill
					null, null
				);
			}

			var text = this.bindingForItemText.contextSet
			(
				item
			).get();

			drawPos2.addDimensions
			(
				textMarginLeft, 0, 0
			);

			display.drawText
			(
				text,
				this.fontHeightInPixels,
				drawPos2,
				colorFore,
				colorBack,
				(i == this.indexOfItemSelected(null)), // areColorsReversed
				false, // isCentered
				this.size.x // widthMaxInPixels
			);
		}

		this.scrollbar.draw(universe, display, drawLoc, style);
	}
}
