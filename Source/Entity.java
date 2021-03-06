
public class Entity
{
	public String name;

	public Actor _actor;
	public Drawable _drawable;
	public Locatable _locatable;

	public EntityProperty[] properties;

	public Entity
	(
		String name,
		Actor actor,
		Drawable drawable,
		Locatable locatable
	)
	{
		this.name = name;

		this._actor = actor;
		this._drawable = drawable;
		this._locatable = locatable;

		this.properties = new EntityProperty[]
		{
			this._actor,
			this._drawable,
			this._locatable
		};
	}

	public void updateForTimerTick
	(
		Universe universe, World world, Place place
	)
	{
		for (var i = 0; i < this.properties.length; i++)
		{
			var property = this.properties[i];
			property.updateForTimerTick(universe, world, place, this);
		}
	}

	// EntityProperties.

	public Actor actor()
	{
		return this._actor;
	}

	public Drawable drawable()
	{
		return this._drawable;
	}

	public Locatable locatable()
	{
		return this._locatable;
	}
}
