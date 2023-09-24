package Model;

import java.util.*;

import Helpers.*;
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

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.entity = this;

		for (var i = 0; i < this.properties.length; i++)
		{
			var property = this.properties[i];
			property.updateForTimerTick(uwpe);
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

	// Clonable.

	public Entity clone()
	{
		return new Entity
		(
			this.name,
			Arrays.asList
			(
				this.properties
			).stream().map
			(
				x -> x.cloneAsEntityProperty()
			).toArray(EntityProperty[]::new)
		);
	}
}
