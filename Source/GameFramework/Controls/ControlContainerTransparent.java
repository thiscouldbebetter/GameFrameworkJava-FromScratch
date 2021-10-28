
package GameFramework.Controls;

import java.util.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*
import GameFramework.Input.*;
import GameFramework.Model.*;
import GameFramework.Model.Actors.*;

public class ControlContainerTransparent extends ControlBase
{
	ControlContainer containerInner;

	public ControlContainerTransparent(ControlContainer containerInner)
	{
		super
		(
			containerInner.name(),
			containerInner.pos,
			containerInner.size,
			containerInner.fontHeightInPixels
		);
		this.containerInner = containerInner;
	}

	// instance methods

	public ActionToInputsMapping[] actionToInputsMappings()
	{
		return this.containerInner.actionToInputsMappings();
	}

	public ControlBase childWithFocus()
	{
		return this.containerInner.childWithFocus();
	}

	public ControlBase childWithFocusNextInDirection(int direction)
	{
		return this.containerInner.childWithFocusNextInDirection(direction);
	}

	public List<ControlBase> childrenAtPosAddToList
	(
		Coords posToCheck,
		List<ControlBase> listToAddTo,
		boolean addFirstChildOnly
	)
	{
		return this.containerInner.childrenAtPosAddToList
		(
			posToCheck, listToAddTo, addFirstChildOnly
		);
	}

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		return this.containerInner.actionHandle(actionNameToHandle, universe);
	}

	public boolean isEnabled()
	{
		return true; // todo
	}

	public boolean mouseClick(Coords mouseClickPos)
	{
		var childrenContainingPos = this.containerInner.childrenAtPosAddToList
		(
			mouseClickPos,
			ArrayHelper.clear(this.containerInner.childrenContainingPos),
			true // addFirstChildOnly
		);

		var wasClickHandled = false;
		if (childrenContainingPos.length > 0)
		{
			var child = childrenContainingPos[0];
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

	public boolean mouseMove(Coords mouseMovePos)
	{
		return this.containerInner.mouseMove(mouseMovePos);
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		return this.containerInner.scalePosAndSize(scaleFactor);
	}

	// drawable

	public void draw
	(
		Universe universe, Display display, Disposition drawLoc,
		ControlStyle style
	)
	{
		if (this.isVisible() == false)
		{
			return;
		}

		drawLoc = this.containerInner._drawLoc.overwriteWith(drawLoc);
		var drawPos = this.containerInner._drawPos.overwriteWith(drawLoc.pos).add
		(
			this.containerInner.pos
		);

		style = (style != null ? style : this.style(universe));

		display.drawRectangle
		(
			drawPos, this.containerInner.size,
			null, // display.colorBack,
			style.colorBorder, false
		);

		var children = this.containerInner.children;
		for (var i = 0; i < children.size(); i++)
		{
			var child = children.get(i);
			child.draw(universe, display, drawLoc, style);
		}
	}
}
