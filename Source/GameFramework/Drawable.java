package GameFramework;

import GameFramework.Display.*;

public class Drawable implements EntityProperty
{
	public Visual visual;

	public Drawable(Visual visual)
	{
		this.visual = visual;
	}

	public void updateForTimerTick
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		this.visual.draw(universe, world, place, entity);
	}
}
