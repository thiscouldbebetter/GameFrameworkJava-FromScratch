package Main;

import Model.*;

public class Actor implements EntityProperty
{
	public Activity activity;

	public Actor(Activity activity)
	{
		this.activity = activity;
	}

	// EntityProperty.

	public static String nameStatic() { return "Actor"; }

	public String name() { return Actor.nameStatic(); }

	public void updateForTimerTick
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		this.activity.perform(universe, world, place, entity);
	}
}
