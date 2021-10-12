
package GameFramework.Controls;

import java.util.*;
import java.util.stream.*;

import GameFramework.Geometry.*;
import GameFramework.Model.*;

public class ControlTabbed extends ControlBase
{
	public Coords tabButtonSize;
	public ControlBase[] childrenForTabs;
	public Map<String,ControlBase> childrenForTabsByName;
	public Consumer<Universe> cancel;

	private ControlButton[] buttonsForChildren;
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
		ControlBase childrenForTabs[], number fontHeightInPixels,
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
		this.childrenContainingPos = new Array<ControlBase>();
		this.childrenContainingPosPrev = new Array<ControlBase>();
		this.isChildSelectedActive = false;

		var marginSize = this.fontHeightInPixels;
		var tabPaneHeight = marginSize + this.tabButtonSize.y;
		var buttonsForChildren = new Array<ControlButton>();

		var buttonForTabClick = (ControlButton b) -> // click
		{
			buttonsForChildren.stream().forEach(x -> x.isHighlighted = false);
			var buttonIndex = buttonsForChildren.indexOf(b); // hack
			this.childSelectedIndex = buttonIndex;
			this.isChildSelectedActive = true;
			b.isHighlighted = true;
		}

		for (var i = 0; i < this.childrenForTabs.length; i++)
		{
			var child = this.childrenForTabs[i];

			child.pos.y += tabPaneHeight;

			var childName = child.name;

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
				true, // isEnabled
				buttonForTabClick
			);
			button.context = button; // hack
			buttonsForChildren.add(button);
		}

		if (this.cancel != null)
		{
			this.childrenForTabs.add(null);
			var button = ControlButton.from8
			(
				"buttonCancel",
				Coords.fromXY(this.size.x - marginSize - this.tabButtonSize.x, marginSize), // pos
				this.tabButtonSize.clone(),
				"Done", // text
				this.fontHeightInPixels,
				true, // hasBorder
				true, // isEnabled
				this.cancel // click
			);
			buttonsForChildren.add(button);
		}

		this.buttonsForChildren = buttonsForChildren;

		this.buttonsForChildren[0].isHighlighted = true;

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

	actionHandle(String actionNameToHandle, Universe universe): boolean
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
			else if (childSelected.actionHandle != null)
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
					this.cancel(universe);
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
					this.cancel(universe);
				}
			}
		}

		return wasActionHandled;
	}

	public ControlBase childSelected()
	{
		var returnValue =
		(
			this.childSelectedIndex == null
			? null
			: this.childrenForTabs[this.childSelectedIndex]
		);
		return returnValue;
	}

	public ControlBase childSelectNextInDirection(number direction)
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
				this.childSelectedIndex = NumberHelper.wrapToRangeMax
				(
					this.childSelectedIndex,
					this.childrenForTabs.length
				);
			}

			this.buttonsForChildren.stream().forEach(x -> x.isHighlighted = false);
			var buttonForChild = this.buttonsForChildren[this.childSelectedIndex];
			buttonForChild.isHighlighted = true;

			var child = this.childrenForTabs[this.childSelectedIndex];
			if (child == null)
			{
				break;
			}
			else if
			(
				child.focusGain != null && child.isEnabled()
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
		var childSelectedAsContainer = this.childSelected() as ControlContainer;
		if (childSelectedAsContainer != null)
		{
			childrenActive.push(childSelectedAsContainer);
		}

		for (var i = childrenActive.length - 1; i >= 0; i--)
		{
			var child = childrenActive[i];
			if (child != null)
			{
				var childMax =
					this._childMax.overwriteWith(child.pos).add(child.size);
				var doesChildContainPos =
					posToCheck.isInRangeMinMax(child.pos, childMax);

				if (doesChildContainPos)
				{
					listToAddTo.push(child);
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
		this.childSelectedIndex = null;
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
			this.childSelectedIndex = null;
		}
	}

	public boolean mouseClick(Coords mouseClickPos)
	{
		var mouseClickPos = this._mouseClickPos.overwriteWith
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
		var child = childrenContainingPos[0];
		if (child != null)
		{
			if (child.mouseClick != null)
			{
				var wasClickHandledByChild = child.mouseClick(mouseClickPos);
				if (wasClickHandledByChild)
				{
					wasClickHandled = true;
				}
			}
		}

		return wasClickHandled;
	}

	public void mouseEnter()
	public void mouseExit()

	public boolean mouseMove(Coords mouseMovePos)
	{
		var mouseMovePos = this._mouseMovePos.overwriteWith
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

		for (var i = 0; i < childrenContainingPos.length; i++)
		{
			var child = childrenContainingPos[i];

			if (child.mouseMove != null)
			{
				child.mouseMove(mouseMovePos);
			}
			if (this.childrenContainingPosPrev.indexOf(child) == -1)
			{
				if (child.mouseEnter != null)
				{
					child.mouseEnter();
				}
			}
		}

		for (var i = 0; i < this.childrenContainingPosPrev.length; i++)
		{
			var child = this.childrenContainingPosPrev[i];
			if (childrenContainingPos.indexOf(child) == -1)
			{
				if (child.mouseExit != null)
				{
					child.mouseExit();
				}
			}
		}

		var child = this.childSelected();
		if (child != null)
		{
			if (child.mouseMove != null)
			{
				var wasMoveHandledByChild = child.mouseMove(mouseMovePos);
				if (wasMoveHandledByChild)
				{
					wasMoveHandled = true;
				}
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
			if (child.scalePosAndSize == null)
			{
				child.pos.multiply(scaleFactor);
				child.size.multiply(scaleFactor);
				if (child.fontHeightInPixels != null)
				{
					child.fontHeightInPixels *= scaleFactor.y;
				}
			}
			else
			{
				child.scalePosAndSize(scaleFactor);
			}
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
		var style = this.style(universe);

		display.drawRectangle
		(
			drawPos, this.size,
			style.colorBackground,
			style.colorBorder,
			null
		);

		var buttons = this.buttonsForChildren;
		for (var i = 0; i < buttons.length; i++)
		{
			var button = buttons[i];
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
