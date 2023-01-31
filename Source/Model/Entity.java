package Model;

import Main.*;

public class Entity
{
	public String name;

	public EntityProperty[] properties;

	public Entity
	(
		String name,
		EntityProperty[] properties
	)
	{
		this.name = name;
		this.properties = properties;
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

	private EntityProperty propertyByName(String propertyNameToFind)
	{
		EntityProperty returnValue = null; 

		for (var i = 0; i < this.properties.length; i++)
		{
			var property = this.properties[i];
			var propertyName = property.name();
			if (propertyName == propertyNameToFind)
			{
				returnValue = property;
				break;
			}
		}

		return returnValue;
	}

	public Actor actor()
	{
		return (Actor)this.propertyByName(Actor.nameStatic());
	}

	public Drawable drawable()
	{
		return (Drawable)this.propertyByName(Drawable.nameStatic());
	}

	public Locatable locatable()
	{
		return (Locatable)this.propertyByName(Locatable.nameStatic());
	}
}
