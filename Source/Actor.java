
public class Actor implements EntityProperty
{
	public Activity activity;

	public Actor(Activity activity)
	{
		this.activity = activity;
	}

	// EntityProperty.

	public void updateForTimerTick
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		this.activity.perform(universe, world, place, entity);
	}
}
