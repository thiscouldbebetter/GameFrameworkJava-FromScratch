
package Controls;

import Display.*;
import Geometry.*;
import Helpers.*;
import Input.*;
import Model.*;
import Model.Actors.*;

import java.util.*;

public class ControlContainer extends ControlBase
{
	public List<ControlBase> children;
	public Map<String, ControlBase> childrenByName;
	public ActorAction[] actions;
	public Map<String, ActorAction> actionsByName;
	public ActionToInputsMapping[] _actionToInputsMappings;
	public Map<String, ActionToInputsMapping> _actionToInputsMappingsByInputName;

	public List<ControlBase> childrenContainingPos;
	public List<ControlBase> childrenContainingPosPrev;
	public Integer indexOfChildWithFocus;

	private Coords _childMax;
	public Coords _drawPos; // hack
	public Disposition _drawLoc;
	private Coords _mouseClickPos;
	private Coords _mouseMovePos;
	private Coords _posToCheck;

	public ControlContainer
	(
		String name,
		Coords pos,
		Coords size,
		ControlBase[] children,
		ActorAction[] actions,
		ActionToInputsMapping[] actionToInputsMappings
	)
	{
		super(name, pos, size, null);
		this.children = Arrays.asList(children);
		this.actions = (actions != null ? actions : new ActorAction[] {});
		this._actionToInputsMappings = (actionToInputsMappings != null ? actionToInputsMappings : new ActionToInputsMapping[] {});
		this._actionToInputsMappingsByInputName =
			ActionToInputsMapping.mappingsToMappingsByInputName
			(
				this._actionToInputsMappings
			);

		this.childrenByName = ArrayHelper.addLookupsByName(this.children);
		this.actionsByName = ArrayHelper.addLookupsByName(this.actions);

		for (var i = 0; i < this.children.size(); i++)
		{
			var child = this.children.get(i);
			child.parent = this;
		}

		this.indexOfChildWithFocus = null;
		this.childrenContainingPos = new ArrayList<ControlBase>();
		this.childrenContainingPosPrev = new ArrayList<ControlBase>();

		// Helper variables.
		this._childMax = Coords.create();
		this._drawPos = Coords.create();
		this._drawLoc = Disposition.fromPos(this._drawPos);
		this._mouseClickPos = Coords.create();
		this._mouseMovePos = Coords.create();
		this._posToCheck = Coords.create();
	}

	public static ControlContainer from4
	(
		String name,
		Coords pos,
		Coords size,
		ControlBase[] children
	)
	{
		return new ControlContainer
		(
			name, pos, size, children, null, null
		);
	}

	// instance methods

	// actions

	public boolean actionHandle(String actionNameToHandle, Universe universe)
	{
		var wasActionHandled = false;

		var childWithFocus = this.childWithFocus();

		var controlActionNames = ControlActionNames.Instances();
		if
		(
			actionNameToHandle == controlActionNames.ControlPrev
			|| actionNameToHandle == controlActionNames.ControlNext
		)
		{
			wasActionHandled = true;

			var direction = (actionNameToHandle == controlActionNames.ControlPrev ? -1 : 1);

			if (childWithFocus == null)
			{
				childWithFocus = this.childWithFocusNextInDirection(direction);
				if (childWithFocus != null)
				{
					childWithFocus.focusGain();
				}
			}
			else if (childWithFocus.childWithFocus() != null)
			{
				childWithFocus.actionHandle(actionNameToHandle, universe);
				if (childWithFocus.childWithFocus() == null)
				{
					childWithFocus = this.childWithFocusNextInDirection(direction);
					if (childWithFocus != null)
					{
						childWithFocus.focusGain();
					}
				}
			}
			else
			{
				childWithFocus.focusLose();
				childWithFocus = this.childWithFocusNextInDirection(direction);
				if (childWithFocus != null)
				{
					childWithFocus.focusGain();
				}
			}
		}
		else if (this._actionToInputsMappingsByInputName.containsKey(actionNameToHandle))
		{
			var inputName = actionNameToHandle; // Likely passed from parent as raw input.
			var mapping = this._actionToInputsMappingsByInputName.get(inputName);
			var actionName = mapping.actionName;
			var action = this.actionsByName.get(actionName);
			action.performForUniverse(universe);
			wasActionHandled = true;
		}
		else if (this.actionsByName.containsKey(actionNameToHandle))
		{
			var action = this.actionsByName.get(actionNameToHandle);
			action.performForUniverse(universe);
			wasActionHandled = true;
		}
		else if (childWithFocus != null)
		{
			wasActionHandled = childWithFocus.actionHandle(actionNameToHandle, universe);
		}

		return wasActionHandled;
	}

	public ActionToInputsMapping[] actionToInputsMappings()
	{
		return this._actionToInputsMappings;
	}

	public void childAdd(ControlBase childToAdd)
	{
		this.children.add(childToAdd);
		this.childrenByName.put(childToAdd.name(), childToAdd);
	}

	public ControlBase childByName(String childName)
	{
		return this.childrenByName.get(childName);
	}

	public ControlBase childWithFocus()
	{
		return
		(
			this.indexOfChildWithFocus == null
			? null
			: this.children.get(this.indexOfChildWithFocus)
		);
	}

	public ControlBase childWithFocusNextInDirection(int direction)
	{
		if (this.indexOfChildWithFocus == null)
		{
			var childCount = this.children.size();
			var iStart = (direction == 1 ? 0 : childCount - 1);
			var iEnd = (direction == 1 ? childCount : -1);

			for (var i = iStart; i != iEnd; i += direction)
			{
				var child = this.children.get(i);
				if
				(
					child.isEnabled()
				)
				{
					this.indexOfChildWithFocus = i;
					break;
				}
			}
		}
		else
		{
			while (true)
			{
				this.indexOfChildWithFocus += direction;

				var isChildNextInRange = NumberHelper.isInRangeMinMax
				(
					this.indexOfChildWithFocus, 0, this.children.size() - 1
				);

				if (isChildNextInRange == false)
				{
					this.indexOfChildWithFocus = null;
					break;
				}
				else
				{
					var child = this.children.get(this.indexOfChildWithFocus);
					if (child.isEnabled())
					{
						break;
					}
				}

			} // end while (true)

		} // end if

		var returnValue = this.childWithFocus();

		return returnValue;
	}

	public List<ControlBase> childrenAtPosAddToList
	(
		Coords posToCheck,
		List<ControlBase> listToAddTo,
		boolean addFirstChildOnly
	)
	{
		posToCheck = this._posToCheck.overwriteWith(posToCheck).clearZ();

		for (var i = this.children.size() - 1; i >= 0; i--)
		{
			var child = this.children.get(i);

			var doesChildContainPos = posToCheck.isInRangeMinMax
			(
				child.pos,
				this._childMax.overwriteWith(child.pos).add(child.size)
			);

			if (doesChildContainPos)
			{
				listToAddTo.add(child);
				if (addFirstChildOnly)
				{
					break;
				}
			}
		}

		return listToAddTo;
	}

	public void focusGain()
	{
		this.indexOfChildWithFocus = -1;
		var childWithFocus = this.childWithFocusNextInDirection(1);
		if (childWithFocus != null)
		{
			childWithFocus.focusGain();
		}
	}

	public void focusLose()
	{
		var childWithFocus = this.childWithFocus();
		if (childWithFocus != null)
		{
			childWithFocus.focusLose();
			this.indexOfChildWithFocus = -1;
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

		var childrenContainingPos = this.childrenAtPosAddToList
		(
			mouseClickPos,
			ArrayHelper.clear(this.childrenContainingPos),
			true // addFirstChildOnly
		);

		var wasClickHandled = false;
		if (childrenContainingPos.size() > 0)
		{
			var child = childrenContainingPos.get(0);

			var wasClickHandledByChild = child.mouseClick(mouseClickPos);
			if (wasClickHandledByChild)
			{
				wasClickHandled = true;
			}
		}

		return wasClickHandled;
	}

	public boolean mouseMove(Coords mouseMovePos)
	{
		var temp = this.childrenContainingPosPrev;
		this.childrenContainingPosPrev = this.childrenContainingPos;
		this.childrenContainingPos = temp;

		mouseMovePos = this._mouseMovePos.overwriteWith(mouseMovePos).subtract(this.pos);

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

		return false; // wasMoveHandled
	}

	public ControlBase scalePosAndSize(Coords scaleFactor)
	{
		this.pos.multiply(scaleFactor);
		this.size.multiply(scaleFactor);

		for (var i = 0; i < this.children.size(); i++)
		{
			var child = this.children.get(i);
			/*
			if (child.scalePosAndSize == null)
			{
				child.pos.multiply(scaleFactor);
				child.size.multiply(scaleFactor);
				if (child.fontHeightInPixels > 0)
				{
					child.fontHeightInPixels *= scaleFactor.y;
				}
			}
			else
			{
			*/

			child.scalePosAndSize(scaleFactor);

		}

		return this;
	}

	public void shiftChildPositions(Coords displacement)
	{
		for (var i = 0; i < this.children.size(); i++)
		{
			var child = this.children.get(i);
			child.pos.add(displacement);
		}
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
		style = (style != null ? style : this.style(universe) );

		display.drawRectangle
		(
			drawPos, this.size,
			style.colorBackground,
			style.colorBorder,
			false
		);

		var children = this.children;
		for (var i = 0; i < children.size(); i++)
		{
			var child = children.get(i);
			child.draw(universe, display, drawLoc, style);
		}
	}
}
