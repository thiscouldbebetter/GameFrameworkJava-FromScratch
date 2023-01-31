package Main;

import Display.*;
import Model.*;

public class Drawable implements EntityProperty
{
	public Visual visual;

	public Drawable(Visual visual)
	{
		this.visual = visual;
	}

	// EntityProperty.

	public static String nameStatic() { return "Drawable"; }
	
	public String name() { return Drawable.nameStatic(); }

	public void updateForTimerTick
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		this.visual.draw(universe, world, place, entity);
	}
}
