
package GameFramework.Controls;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class ControlTabbed extends ControlBase
{
	public Coords tabButtonSize;
	public ControlBase[] childrenForTabs;
	public Map<String,ControlBase> childrenForTabsByName;
	public Consumer<Universe> cancel;

	private List<ControlButton> buttonsForChildren;
	private int childSelectedIndex;
	private List<ControlBase> childrenContainingPos;
	private List<ControlBase> childrenContainingPosPrev;
	private boolean isChildSelectedActive;

	private Coords _childMax;
	private Coords _drawPos;
	private Disposition _drawLoc;
	private Coords _mouseClickPos;
	private Coords _mouseMovePos;
	private Coords _posToCheck;

	public ControlTabbed
	(
		String name, Coords pos, Coords size, Coords tabButtonSize,
		ControlBase childrenForTabs[], double fontHeightInPixels,
		Consumer<Universe> cancel
	)
	{
		super(name, pos, size, fontHeightInPixels);
		this.tabButtonSize = tabButtonSize;
		this.childrenForTabs = childrenForTabs;
		this.childrenForTabsByName =
			ArrayHelper.addLookupsByName(this.childrenForTabs);
		this.cancel = cancel;

		this.childSelectedIndex = 0;
		this.childrenContainingPos = new ArrayList<ControlBase>();
		this.childrenContainingPosPrev = new ArrayList<ControlBase>();
		this.isChildSelectedActive = false;

		var marginSize = this.fontHeightInPixels;
		var tabPaneHeight = marginSize + this.tabButtonSize.y;
		var buttonsForChildren = new ArrayList<ControlButton>();

		Runnable buttonForTabClick = () -> // click
		{
			buttonsForChildren.stream().forEach
			(
				x -> x.isHighlighted = false
			);
			/*
			var buttonIndex = buttonsForChildren.indexOf(b); // hack
			this.childSelectedIndex = buttonIndex;
			this.isChildSelectedActive = true;
			b.isHighlighted = true;
			*/
		};

		for (var i = 0; i < this.childrenForTabs.length; i++)
		{
			var child = this.childrenForTabs[i];

			child.pos.y += tabPaneHeight;

			var childName = child.name();

			var buttonPos = Coords.fromXY
			(
				marginSize + this.tabButtonSize.x * i, marginSize
			);

			var button = ControlButton.from8
			(
				"button" + childName,
				buttonPos,
				this.tabButtonSize.clone(),
				childName, // text
				this.fontHeightInPixels,
				true, // hasBorder
				DataBinding.fromTrue(), // isEnabled
				buttonForTabClick
			);
			button.context = button; // hack
			buttonsForChildren.add(button);
		}

		if (this.cancel != null)
		{
			// this.childrenForTabs.add(null);
			var button = ControlButton.from8
			(
				"buttonCancel",
				Coords.fromXY
				(
					this.size.x - marginSize - this.tabButtonSize.x,
					marginSize
				), // pos
				this.tabButtonSize.clone(),
				"Done", // text
				this.fontHeightInPixels,
				true, // hasBorder
				DataBinding.fromTrue(), // isEnabled
				() -> this.cancel.accept(null) // click
			);
			buttonsForChildren.add(button);
		}

		this.buttonsForChildren = buttonsForChildren;

		this.buttonsForChildren.get(0).isHighlighted = true;

		// Temporary variables.
		this._childMax = Coords.create();
		this._drawPos = Coords.create();
		this._drawLoc = Disposition.fromPos(this._drawPos);
		this._mouseClickPos = Coords.create();
		this._mouseMovePos = Coords.create();
		this._posToCheck = Coords.create();
	}

	// instance methods

	// actions

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		var wasActionHandled = false;

		var childSelected = this.childSelected();

		var controlActionNames = ControlActionNames.Instances();

		if (this.isChildSelectedActive)
		{
			if (actionNameToHandle == controlActionNames.ControlCancel)
			{
				this.isChildSelectedActive = false;
				childSelected.focusLose();
				wasActionHandled = true;
			}
			else
			{
				wasActionHandled = childSelected.actionHandle(actionNameToHandle, universe);
			}
		}
		else
		{
			wasActionHandled = true;

			if (actionNameToHandle == controlActionNames.ControlConfirm)
			{
				if (childSelected == null)
				{
					this.cancel.accept(universe);
				}
				else
				{
					this.isChildSelectedActive = true;
					childSelected.focusGain();
				}
			}
			else if
			(
				actionNameToHandle == controlActionNames.ControlPrev
				|| actionNameToHandle == controlActionNames.ControlNext
			)
			{
				var direction = (actionNameToHandle == controlActionNames.ControlPrev ? -1 : 1);
				if (childSelected != null)
				{
					childSelected.focusLose();
				}
				childSelected = this.childSelectNextInDirection(direction);
			}
			else if (actionNameToHandle == controlActionNames.ControlCancel)
			{
				if (this.cancel != null)
				{
					this.cancel.accept(universe);
				}
			}
		}

		return wasActionHandled;
	}

	public ControlBase childSelected()
	{
		var returnValue =
		(
			this.childSelectedIndex < 0
			? null
			: this.childrenForTabs[this.childSelectedIndex]
		);
		return returnValue;
	}

	public ControlBase childSelectNextInDirection(int direction)
	{
		while (true)
		{
			this.childSelectedIndex += direction;

			var isChildNextInRange = NumberHelper.isInRangeMinMax
			(
				this.childSelectedIndex, 0, this.childrenForTabs.length - 1
			);

			if (isChildNextInRange == false)
			{
				this.childSelectedIndex = (int)NumberHelper.wrapToRangeMax
				(
					this.childSelectedIndex,
					this.childrenForTabs.length
				);
			}

			this.buttonsForChildren.stream().forEach(x -> x.isHighlighted = false);
			var buttonForChild =
				this.buttonsForChildren.get(this.childSelectedIndex);
			buttonForChild.isHighlighted = true;

			var child = this.childrenForTabs[this.childSelectedIndex];
			if (child == null)
			{
				break;
			}
			else if
			(
				child.isEnabled()
			)
			{
				break;
			}

		} // end while (true)

		var returnValue = this.childSelected();

		return returnValue;
	}

	public ControlBase childWithFocus()
	{
		return this.childSelected();
	}

	public List<ControlBase> childrenAtPosAddToList
	(
		Coords posToCheck,
		List<ControlBase> listToAddTo,
		boolean addFirstChildOnly
	)
	{
		posToCheck = this._posToCheck.overwriteWith(posToCheck).clearZ();

		var childrenActive = new ArrayList<ControlBase>();
		childrenActive.addAll(this.buttonsForChildren);
		var childSelectedAsContainer = (ControlContainer)this.childSelected();
		if (childSelectedAsContainer != null)
		{
			childrenActive.add(childSelectedAsContainer);
		}

		for (var i = childrenActive.size() - 1; i >= 0; i--)
		{
			var child = childrenActive.get(i);
			if (child != null)
			{
				var childMax =
					this._childMax.overwriteWith(child.pos).add(child.size);
				var doesChildContainPos =
					posToCheck.isInRangeMinMax(child.pos, childMax);

				if (doesChildContainPos)
				{
					listToAddTo.add(child);
					if (addFirstChildOnly)
					{
						break;
					}
				}
			}
		}

		return listToAddTo;
	}

	public void focusGain()
	{
		this.childSelectedIndex = -1;
		var childSelected = this.childSelectNextInDirection(1);
		if (childSelected != null)
		{
			childSelected.focusGain();
		}
	}

	public void focusLose()
	{
		var childSelected = this.childSelected();
		if (childSelected != null)
		{
			childSelected.focusLose();
			this.childSelectedIndex = -1;
		}
	}

	public boolean mouseClick(Coords mouseClickPos)
	{
		mouseClickPos = this._mouseClickPos.overwriteWith
		(
			mouseClickPos
		).subtract
		(
			this.pos
		);

		var wasClickHandled = false;

		var childrenContainingPos = this.childrenAtPosAddToList
		(
			mouseClickPos,
			ArrayHelper.clear(this.childrenContainingPos),
			true // addFirstChildOnly
		);
		var child = childrenContainingPos.get(0);
		if (child != null)
		{
			var wasClickHandledByChild = child.mouseClick(mouseClickPos);
			if (wasClickHandledByChild)
			{
				wasClickHandled = true;
			}
		}

		return wasClickHandled;
	}

	public void mouseEnter() {}
	public void mouseExit() {}

	public boolean mouseMove(Coords mouseMovePos)
	{
		mouseMovePos = this._mouseMovePos.overwriteWith
		(
			mouseMovePos
		).subtract
		(
			this.pos
		);

		var wasMoveHandled = false;

		var temp = this.childrenContainingPosPrev;
		this.childrenContainingPosPrev = this.childrenContainingPos;
		this.childrenContainingPos = temp;

		mouseMovePos =
			this._mouseMovePos.overwriteWith(mouseMovePos).subtract(this.pos);

		var childrenContainingPos = this.childrenAtPosAddToList
		(
			mouseMovePos,
			ArrayHelper.clear(this.childrenContainingPos),
			true // addFirstChildOnly
		);

		for (var i = 0; i < childrenContainingPos.size(); i++)
		{
			var child = childrenContainingPos.get(i);

			child.mouseMove(mouseMovePos);

			if (this.childrenContainingPosPrev.indexOf(child) == -1)
			{
				child.mouseEnter();
			}
		}

		for (var i = 0; i < this.childrenContainingPosPrev.size(); i++)
		{
			var child = this.childrenContainingPosPrev.get(i);
			if (childrenContainingPos.indexOf(child) == -1)
			{
				child.mouseExit();
			}
		}

		var child = this.childSelected();
		if (child != null)
		{
			var wasMoveHandledByChild = child.mouseMove(mouseMovePos);
			if (wasMoveHandledByChild)
			{
				wasMoveHandled = true;
			}
		}

		return wasMoveHandled;
	}

	public ControlTabbed scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);

		for (var i = 0; i < this.childrenForTabs.length; i++)
		{
			var child = this.childrenForTabs[i];
			child.scalePosAndSize(scaleFactor);
		}

		return this;
	}

	// drawable

	public void draw
	(
		Universe universe, Display display,
		Disposition drawLoc, ControlStyle style
	)
	{
		drawLoc = this._drawLoc.overwriteWith(drawLoc);
		var drawPos = this._drawPos.overwriteWith(drawLoc.pos).add(this.pos);
		style = (style != null ? style : this.style(universe));

		display.drawRectangle
		(
			drawPos, this.size,
			style.colorBackground,
			style.colorBorder,
			null
		);

		var buttons = this.buttonsForChildren;
		for (var i = 0; i < buttons.size(); i++)
		{
			var button = buttons.get(i);
			if (i == this.childSelectedIndex)
			{
				button.isHighlighted = true;
			}
			button.draw(universe, display, drawLoc, style);
		}

		var child = this.childSelected();
		if (child != null)
		{
			child.draw(universe, display, drawLoc, style);
		}
	}
}
