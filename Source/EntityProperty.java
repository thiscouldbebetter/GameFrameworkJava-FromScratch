
public interface EntityProperty
{
	String name();
	void updateForTimerTick
	(
		Universe universe, World world, Place place, Entity entity
	);
}
