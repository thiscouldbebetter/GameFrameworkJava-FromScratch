
package GameFramework.Controls;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Input.*;
import GameFramework.Model.*;
import GameFramework.Model.Actors.*;
import GameFramework.Utility.*;

public abstract class ControlBase implements Namable
{
	public double fontHeightInPixels;
	public String _name;
	public ControlBase parent;
	public Coords pos;
	public Coords size;

	private boolean _isVisible;
	public String styleName;

	public boolean isHighlighted;

	public ControlBase
	(
		String name,
		Coords pos,
		Coords size,
		Double fontHeightInPixels
	)
	{
		this._name = name;
		this.pos = pos;
		this.size = size;
		this.fontHeightInPixels =
			(fontHeightInPixels != null ? fontHeightInPixels : 10);

		this._isVisible = true;
		this.styleName = null;

		this.isHighlighted = false;
	}

	public boolean actionHandle(String actionName, Universe universe) { return false; }
	public ActionToInputsMapping[] actionToInputsMappings() { return new ActionToInputsMapping[] {}; }
	public ControlBase childWithFocus() { return null; }
	public void draw(Universe u, Display d, Disposition drawLoc, ControlStyle style) {}
	public void focusGain() { this.isHighlighted = true; }
	public void focusLose() { this.isHighlighted = false; }
	public boolean isEnabled() { return true; }
	public boolean isVisible() { return this._isVisible; }
	public boolean mouseClick(Coords x) { return false; }
	public void mouseEnter() { this.isHighlighted = true; }
	public void mouseExit() { this.isHighlighted = false; }
	public boolean mouseMove(Coords x) { return false; }
	public ControlBase scalePosAndSize(Coords x) { return this; }
	public ControlStyle style(Universe universe)
	{
		var returnValue =
		(
			this.styleName == null
			? universe.controlBuilder.styleDefault()
			: universe.controlBuilder.styleByName(this.styleName)
		);
		return returnValue;
	}
	public VenueControls toVenue() { return VenueControls.fromControl(this); }
	
	// Namable.
	
	public String name()
	{
		return this._name;
	}
}
