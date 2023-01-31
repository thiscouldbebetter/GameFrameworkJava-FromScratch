package Model;

public class Place
{
	public String name;
	public Entity[] entities;

	public Place(String name, Entity[] entities)
	{
		this.name = name;
		this.entities = entities;
	}

	public void draw(Universe universe, World world)
	{
		for (var i = 0; i < this.entities.length; i++)
		{
			var entity = this.entities[i];
			var drawable = entity.drawable();
			if (drawable != null)
			{
				drawable.updateForTimerTick(universe, world, this, entity);
			}
		}
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
