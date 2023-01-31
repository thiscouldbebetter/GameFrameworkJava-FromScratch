package Display.Visuals;

import Display.*;
import Geometry.*;
import Model.*;

public class VisualRectangle implements Visual
{
	public String colorName;
	public Coords size;

	public VisualRectangle(String colorName, Coords size)
	{
		this.colorName = colorName;
		this.size = size;
	}

	public void draw
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		var pos = entity.locatable().loc.pos;

		var display = universe.display;
		display.drawRectangle(pos, this.size);
	}
}
