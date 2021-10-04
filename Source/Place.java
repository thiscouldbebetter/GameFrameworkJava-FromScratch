
public class Place
{
	public String name;
	public Entity[] entities;

	public Place(String name, Entity[] entities)
	{
		this.name = name;
		this.entities = entities;
	}

	public void updateForTimerTick(Universe universe, World world)
	{
		for (var i = 0; i < this.entities.length; i++)
		{
			var entity = this.entities[i];
			entity.updateForTimerTick(universe, world, this);
		}
	}
}
