
package GameFramework.Model;

public class Entity //
{
	public int id;
	public String name;
	public EntityProperty[] properties;
	public Map<String, EntityProperty> propertiesByName;

	public Entity(String name, EntityProperty[] properties)
	{
		this.id = IDHelper.Instance().idNext();
		this.name = name;
		this.properties = properties;

		this.propertiesByName = new HashMap<String, Object>();
		for (var i = 0; i < this.properties.length; i++)
		{
			var property = this.properties[i];
			var propertyName = property.constructor.class.name;
			this.propertiesByName.set(propertyName, property);
		}
	}

	public Entity finalize(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.entity = this;
		var entityProperties = this.properties;
		for (var p = 0; p < entityProperties.length; p++)
		{
			var property = entityProperties[p];
			if (property.finalize != null)
			{
				property.finalize(uwpe);
			}
		}
		return this;
	}

	public Entity initialize(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.entity = this;
		var entityProperties = this.properties;
		for (var p = 0; p < entityProperties.length; p++)
		{
			var property = entityProperties[p];
			if (property.initialize != null)
			{
				property.initialize(uwpe);
			}
		}
		return this;
	}

	public Entity propertyAdd(EntityProperty propertyToAdd)
	{
		return this.propertyAddForPlace(propertyToAdd, null);
	}

	public Entity propertyAddForPlace(EntityProperty propertyToAdd, Place place)
	{
		this.properties.push(propertyToAdd);
		this.propertiesByName.set(propertyToAdd.getClass().getName(), propertyToAdd);
		if (place != null)
		{
			if (place.entities.indexOf(this) >= 0)
			{
				var propertyName = propertyToAdd.constructor.class.name;
				var entitiesWithProperty = place.entitiesByPropertyName(propertyName);
				entitiesWithProperty.add(this);
			}
		}
		return this;
	}

	public EntityProperty propertyByName(String name)
	{
		return this.propertiesByName.get(name);
	}

	public Entity propertyRemoveForPlace
	(
		EntityProperty propertyToRemove, Place place
	)
	{
		ArrayHelper.remove(this.properties, propertyToRemove);
		this.propertiesByName.delete(propertyToRemove.constructor.class.name);
		if (place != null)
		{
			var propertyName = propertyToRemove.constructor.class.name;
			var entitiesWithProperty = place.entitiesByPropertyName(propertyName);
			ArrayHelper.remove(entitiesWithProperty, this);
		}
		return this;
	}

	public Entity updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.entity = this;

		var entityProperties = this.properties;
		for (var p = 0; p < entityProperties.length; p++)
		{
			var property = entityProperties[p];
			if (property.finalize != null)
			{
				property.finalize(uwpe);
			}
		}
		return this;
	}

	// Cloneable.

	public Entity clone()
	{
		var nameCloned = this.name; // + IDHelper.Instance().idNext();
		var propertiesCloned = [];
		for (var i = 0; i < this.properties.length; i++)
		{
			var property = this.properties[i];
			var propertyAsAny = property as Object;
			var propertyCloned =
			(
				propertyAsAny.clone == null ?
				propertyAsAny : propertyAsAny.clone()
			) as EntityProperty;
			propertiesCloned.push(propertyCloned);
		}
		var returnValue = new Entity
		(
			nameCloned, propertiesCloned
		);
		return returnValue;
	}

	// Equatable.

	public boolean equals(Entity other)
	{
		var areAllPropertiesEqual =
			ArrayHelper.areEqual(this.properties, other.properties);

		var areEntitiesEqual =
			(this.name == other.name && areAllPropertiesEqual);

		return areEntitiesEqual;
	}

	// Convenience methods for properties.

	/*
	// todo 

	public Actor actor() { return (Actor)(this.propertyByName(Actor.class.name) ); }
	public Animatable animatable() { return (Animatable2)(this.propertyByName(Animatable2.class.name) ); }
	public Audible audible() { return this.propertyByName(Audible.class.name) as Audible; }
	public Boundable boundable() { return this.propertyByName(Boundable.class.name) as Boundable; }
	public Camera camera() { return this.propertyByName(Camera.class.name) as Camera; }
	public Collidable collidable() { return this.propertyByName(Collidable.class.name) as Collidable; }
	public Constrainable constrainable() { return this.propertyByName(Constrainable.class.name) as Constrainable; }
	public Controllable controllable() { return this.propertyByName(Controllable.class.name) as Controllable; }
	public Damager damager() { return this.propertyByName(Damager.class.name) as Damager; }
	public Device device() { return this.propertyByName(Device.class.name) as Device; }
	public Drawable drawable() { return this.propertyByName(Drawable.class.name) as Drawable; }
	public Effectable effectable() { return this.propertyByName(Effectable.class.name) as Effectable; }
	public Ephemeral ephemeral() { return this.propertyByName(Ephemeral.class.name) as Ephemeral; }
	public EquipmentUser equipmentUser() { return this.propertyByName(EquipmentUser.class.name) as EquipmentUser; }
	public Equippable equippable() { return this.propertyByName(Equippable.class.name) as Equippable; }
	public enemy(): Enemy { return this.propertyByName(Enemy.class.name) as Enemy; }
	public forceField(): ForceField { return this.propertyByName(ForceField.class.name) as ForceField; }
	public item(): Item { return this.propertyByName(Item.class.name) as Item; }
	public itemContainer(): ItemContainer { return this.propertyByName(ItemContainer.class.name) as ItemContainer; }
	public itemCrafter(): ItemCrafter { return this.propertyByName(ItemCrafter.class.name) as ItemCrafter; }
	public itemDefn(): ItemDefn { return this.propertyByName(ItemDefn.class.name) as ItemDefn; }
	public itemHolder(): ItemHolder { return this.propertyByName(ItemHolder.class.name) as ItemHolder; }
	public itemStore(): ItemStore { return this.propertyByName(ItemStore.class.name) as ItemStore; }
	public journalKeeper(): JournalKeeper { return this.propertyByName(JournalKeeper.class.name) as JournalKeeper; }
	public killable(): Killable { return this.propertyByName(Killable.class.name) as Killable; }
	public loadable(): Loadable { return this.propertyByName(Loadable.class.name) as Loadable; }
	public locatable(): Locatable { return this.propertyByName(Locatable.class.name) as Locatable; }
	public movable(): Movable { return this.propertyByName(Movable.class.name) as Movable; }
	public obstacle(): Obstacle { return this.propertyByName(Obstacle.class.name) as Obstacle; }
	public phased(): Phased { return this.propertyByName(Phased.class.name) as Phased; }
	public recurrent(): Recurrent { return this.propertyByName(Recurrent.class.name) as Recurrent; }
	public perceptible(): Perceptible { return this.propertyByName(Perceptible.class.name) as Perceptible; }
	public perceptor(): Perceptor { return this.propertyByName(Perceptor.class.name) as Perceptor; }
	public playable(): Playable { return this.propertyByName(Playable.class.name) as Playable; }
	public portal(): Portal { return this.propertyByName(Portal.class.name) as Portal; }
	public projectileGenerator(): ProjectileGenerator { return this.propertyByName(ProjectileGenerator.class.name) as ProjectileGenerator; }
	public selectable(): Selectable { return this.propertyByName(Selectable.class.name) as Selectable; }
	public selector(): Selector { return this.propertyByName(Selector.class.name) as Selector; }
	public skillLearner(): SkillLearner { return this.propertyByName(SkillLearner.class.name) as SkillLearner; }
	public starvable(): Starvable { return this.propertyByName(Starvable.class.name) as Starvable; }
	public talker(): Talker { return this.propertyByName(Talker.class.name) as Talker; }
	public tirable(): Tirable { return this.propertyByName(Tirable.class.name) as Tirable; }
	public traversable(): Traversable { return this.propertyByName(Traversable.class.name) as Traversable; }
	public usable(): Usable { return this.propertyByName(Usable.class.name) as Usable; }
	*/
}
