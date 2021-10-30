
package GameFramework.Model;

import java.util.*;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Media.*;
import GameFramework.Model.Combat.*;
import GameFramework.Model.Items.*;
import GameFramework.Model.Items.Equipment.*;
import GameFramework.Model.Mortality.*;
import GameFramework.Model.Physics.*;
import GameFramework.Model.Places.*;
import GameFramework.Utility.*;

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
			var propertyName = property.getClass().getName();
			this.propertiesByName.put(propertyName, property);
		}
	}

	public Entity finalize(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.entity = this;
		var entityProperties = this.properties;
		for (var p = 0; p < entityProperties.length; p++)
		{
			var property = entityProperties[p];
			property.finalize(uwpe);
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
			property.initialize(uwpe);
		}
		return this;
	}

	public Entity propertyAdd(EntityProperty propertyToAdd)
	{
		return this.propertyAddForPlace(propertyToAdd, null);
	}

	public Entity propertyAddForPlace(EntityProperty propertyToAdd, Place place)
	{
		this.properties.add(propertyToAdd);
		this.propertiesByName.put
		(
			propertyToAdd.getClass().getName(), propertyToAdd
		);
		if (place != null)
		{
			if (place.entities.indexOf(this) >= 0)
			{
				var propertyName = propertyToAdd.getClass().getName();
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
		this.propertiesByName.delete(propertyToRemove.getClass().getName());
		if (place != null)
		{
			var propertyName = propertyToRemove.getClass().getName();
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
		var propertiesCloned = new EntityProperty[this.properties.length];
		for (var i = 0; i < this.properties.length; i++)
		{
			var property = this.properties[i];
			var propertyCloned = property.clone();
			propertiesCloned[i] = propertyCloned;
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

	public Actor actor() { return (Actor)(this.propertyByName(Actor.class.getName()) ); }
	//public Animatable animatable() { return (Animatable2)(this.propertyByName(Animatable2.class.getName()) ); }
	public Audible audible() { return (Audible)(this.propertyByName(Audible.class.getName()) ); }
	public Boundable boundable() { return (Boundable)(this.propertyByName(Boundable.class.getName()) ); }
	public Camera camera() { return (Camera)(this.propertyByName(Camera.class.getName()) ); }
	public Collidable collidable() { return (Collidable)(this.propertyByName(Collidable.class.getName()) ); }
	//public Constrainable constrainable() { return this.propertyByName(Constrainable.class.getName()) as Constrainable; }
	public Controllable controllable() { return (Controllable)(this.propertyByName(Controllable.class.getName()) ); }
	//public Damager damager() { return this.propertyByName(Damager.class.getName()) as Damager; }
	//public Device device() { return this.propertyByName(Device.class.getName()) as Device; }
	public Drawable drawable() { return (Drawable)(this.propertyByName(Drawable.class.getName()) ); }
	public Effectable effectable() { return (Effectable)this.propertyByName(Effectable.class.getName()); }
	//public Ephemeral ephemeral() { return this.propertyByName(Ephemeral.class.getName()) as Ephemeral; }
	public EquipmentUser equipmentUser() { return (EquipmentUser)this.propertyByName(EquipmentUser.class.getName()); }
	public Equippable equippable() { return (Equippable)this.propertyByName(Equippable.class.getName()); }
	//public Enemy enemy() { return this.propertyByName(Enemy.class.getName()) as Enemy; }
	//public ForceField forceField() { return this.propertyByName(ForceField.class.getName()) as ForceField; }
	public Item item() { return (Item)(this.propertyByName(Item.class.getName()) ); }
	//public ItemContainer itemContainer() { return this.propertyByName(ItemContainer.class.getName()) as ItemContainer; }
	//public ItemCrafter itemCrafter() { return this.propertyByName(ItemCrafter.class.getName()) as ItemCrafter; }
	public ItemDefn itemDefn() { return (ItemDefn)(this.propertyByName(ItemDefn.class.getName()) ); }
	public ItemHolder itemHolder() { return (ItemHolder)this.propertyByName(ItemHolder.class.getName()); }
	//public ItemStore itemStore() { return this.propertyByName(ItemStore.class.getName()) as ItemStore; }
	//public JournalKeeper journalKeeper() { return this.propertyByName(JournalKeeper.class.getName()) as JournalKeeper; }
	public Killable killable() { return (Killable)(this.propertyByName(Killable.class.getName()) ); }
	public Loadable loadable() { return (Loadable)(this.propertyByName(Loadable.class.getName()) ); }
	public Locatable locatable() { return (Locatable)(this.propertyByName(Locatable.class.getName()) ); }
	public Movable movable() { return (Movable)this.propertyByName(Movable.class.getName()); }
	//public Obstacle obstacle() { return this.propertyByName(Obstacle.class.getName()) as Obstacle; }
	//public Phased phased() { return this.propertyByName(Phased.class.getName()) as Phased; }
	//public Recurrent recurrent() { return this.propertyByName(Recurrent.class.getName()) as Recurrent; }
	//public Perceptible perceptible() { return this.propertyByName(Perceptible.class.getName()) as Perceptible; }
	//public Perceptor perceptor() { return this.propertyByName(Perceptor.class.getName()) as Perceptor; }
	public Playable playable() { return (Playable)this.propertyByName(Playable.class.getName()); }
	//public Portal portal() { return this.propertyByName(Portal.class.getName()) as Portal; }
	//public ProjectileGenerator projectileGenerator() { return this.propertyByName(ProjectileGenerator.class.getName()) as ProjectileGenerator; }
	public Selectable selectable() { return (Selectable)this.propertyByName(Selectable.class.getName()); }
	public Selector selector() { return (Selector)this.propertyByName(Selector.class.getName()); }
	//public SkillLearner skillLearner() { return this.propertyByName(SkillLearner.class.getName()) as SkillLearner; }
	public Starvable starvable() { return (Starvable)this.propertyByName(Starvable.class.getName() ); }
	//public Talker talker() { return this.propertyByName(Talker.class.getName()) as Talker; }
	public Tirable tirable() { return (Tirable)this.propertyByName(Tirable.class.getName()); }
	//public Traversable traversable() { return this.propertyByName(Traversable.class.getName()) as Traversable; }
	//public Usable usable() { return this.propertyByName(Usable.class.getName()) as Usable; }
}
