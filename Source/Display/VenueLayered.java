
package Display;

import Geometry.*;
import Model.*;

public class VenueLayered implements Venue
{
	public Venue[] children;
	public Color colorToOverlayBetweenChildren;

	public VenueLayered(Venue[] children, Color colorToOverlayBetweenChildren)
	{
		this.children = children;
		this.colorToOverlayBetweenChildren = colorToOverlayBetweenChildren;
	}

	public void finalize(Universe universe) {}

	public void initialize(Universe universe)
	{
		for (var i = 0; i < this.children.length; i++)
		{
			var child = this.children[i];
			child.initialize(universe);
		}
	}

	public void updateForTimerTick(Universe universe)
	{
		this.children[this.children.length - 1].updateForTimerTick(universe);
	}

	public void draw(Universe universe)
	{
		for (var i = 0; i < this.children.length; i++)
		{
			var child = this.children[i];
			child.draw(universe);
			if (this.colorToOverlayBetweenChildren != null)
			{
				var display = universe.display;
				display.drawRectangle
				(
					Coords.Instances().Zeroes,
					display.sizeInPixels(),
					this.colorToOverlayBetweenChildren,
					null, // colorBorder?
					false // areColorsReversed
				);
			}
		}
	}
}
