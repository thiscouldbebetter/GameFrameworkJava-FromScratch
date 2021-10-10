
package GameFramework.Controls;

import GameFramework.Geometry.*;
import GameFramework.Model.*;

public abstract class ControlBase
{
	public double fontHeightInPixels;
	public String name;
	public ControlBase parent;
	public Coords pos;
	public Coords size;

	private boolean _isVisible;
	private String styleName;

	public boolean isHighlighted;

	public ControlBase
	(
		String name,
		Coords pos,
		Coords size,
		double fontHeightInPixels
	)
	{
		this.name = name;
		this.pos = pos;
		this.size = size;
		this.fontHeightInPixels = fontHeightInPixels;

		this._isVisible = true;
		this.styleName = null;

		this.isHighlighted = false;
	}

	boolean actionHandle(String actionName, Universe universe) { return false; }
	ActionToInputsMapping[] actionToInputsMappings() { return new ActionToInputsMapping{} {}; }
	ControlBase childWithFocus() { return null; }
	void draw(Universe u, Display d, Disposition drawLoc, ControlStyle style) {}
	void focusGain() { this.isHighlighted = true; }
	void focusLose() { this.isHighlighted = false; }
	boolean isEnabled() { return true; }
	boolean isVisible() { return this._isVisible; }
	boolean mouseClick(Coords x) { return false; }
	void mouseEnter() { this.isHighlighted = true; }
	void mouseExit() { this.isHighlighted = false; }
	boolean mouseMove(Coords x) { return false; }
	void scalePosAndSize(Coords x) {}
	style(Universe universe)
	{
		var returnValue =
		(
			this.styleName == null
			? universe.controlBuilder.styleDefault()
			: universe.controlBuilder.styleByName(this.styleName)
		);
		return returnValue;
	}
	VenueControls toVenue() { return VenueControls.fromControl(this); }
}
