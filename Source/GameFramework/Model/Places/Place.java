
package GameFramework.Model.Places;

import java.util.*;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Collisions.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;
import GameFramework.Model.Items.*;
import GameFramework.Model.Physics.*;
import GameFramework.Model.Usables.*;
import GameFramework.Utility.*;

public class Place implements Namable
{
	public String _name;
	public String defnName;
	public Coords size;
	public List<Entity> entities;
	public Map<Integer,Entity> entitiesById;
	public Map<String,Entity> entitiesByName;

	public Map<String,List<Entity>> _entitiesByPropertyName;
	public List<Entity> entitiesToSpawn;
	public List<Entity> entitiesToRemove;
	public boolean isLoaded;

	public Place
	(
		String name, String defnName, Coords size, Entity entities[]
	)
	{
		this._name = name;
		this.defnName = defnName;
		this.size = size;
		this.entities = new ArrayList<Entity>();
		this.entitiesById = new HashMap<Integer,Entity>();
		this.entitiesByName = new HashMap<String,Entity>();

		this._entitiesByPropertyName = new HashMap<String,List<Entity>>();
		this.entitiesToSpawn = new ArrayList<Entity>();
		this.entitiesToSpawn.addAll(Arrays.asList(entities));
		this.entitiesToRemove = new ArrayList<Entity>();
		this.isLoaded = false;
	}

	public static Place _default()
	{
		return new Place
		(
			"Default",
			"Default", // defnName,
			Coords.fromXY(1, 1).multiplyScalar(1000), // size
			null // entities
		);
	}

	public PlaceDefn defn(World world)
	{
		return world.defn.placeDefnByName(this.defnName);
	}

	public void draw(Universe universe, World world, Display display)
	{
		var uwpe = UniverseWorldPlaceEntities.fromUniverseWorldAndPlace
		(
			universe, world, this
		);
		var colorBlack = Color.byName("Black");
		display.drawBackground(colorBlack, colorBlack);

		var cameraEntity = this.camera();
		if (cameraEntity == null)
		{
			var drawables = this.drawables();
			drawables.forEach
			(
				(Entity x) ->
				{
					x.drawable().updateForTimerTick(uwpe.entitySet(x) );
				}
			);
		}
		else
		{
			var camera = cameraEntity.camera();
			camera.drawEntitiesInView(uwpe, cameraEntity, display);
		}
	}

	public List<Entity> entitiesByPropertyName(String propertyName)
	{
		var returnValues = this._entitiesByPropertyName.get(propertyName);
		if (returnValues == null)
		{
			returnValues = new ArrayList<Entity>();
			this._entitiesByPropertyName.put(propertyName, returnValues);
		}

		return returnValues;
	}

	public void entitiesRemove()
	{
		for (var i = 0; i < this.entitiesToRemove.size(); i++)
		{
			var entity = this.entitiesToRemove.get(i);
			this.entityRemove(entity);
		}
		this.entitiesToRemove.clear();
	}

	public void entitiesToRemoveAdd(Entity[] entitiesToRemove)
	{
		this.entitiesToRemove.addAll(Arrays.asList(entitiesToRemove));
	}

	public void entitiesToSpawnAdd(Entity entitiesToSpawn[])
	{
		this.entitiesToSpawn.addAll(Arrays.asList(entitiesToSpawn));
	}

	public void entitiesSpawn(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.place = this;
		for (var i = 0; i < this.entitiesToSpawn.size(); i++)
		{
			var entity = this.entitiesToSpawn.get(i);
			uwpe.entity = entity;
			this.entitySpawn(uwpe);
		}

		this.entitiesToSpawn.clear();
	}

	public Entity entityById(int entityId)
	{
		return this.entitiesById.get(entityId);
	}

	public Entity entityByName(String entityName)
	{
		return this.entitiesByName.get(entityName);
	}

	public void entityRemove(Entity entity)
	{
		var entityProperties = entity.properties;
		for (var p = 0; p < entityProperties.size(); p++)
		{
			var property = entityProperties.get(p);
			var propertyName = property.getClass().getName();
			var entitiesWithProperty =
				this.entitiesByPropertyName(propertyName);
			ArrayHelper.remove(entitiesWithProperty, entity);
		}
		ArrayHelper.remove(this.entities, entity);
		this.entitiesById.remove(entity.id);
		this.entitiesByName.remove(entity.name);
	}

	public void entitySpawn(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.place = this;

		var entity = uwpe.entity;

		if (entity.name == null)
		{
			entity.name = "Entity";
		}

		if (this.entitiesByName.containsKey(entity.name))
		{
			entity.name += entity.id;
		}

		this.entities.add(entity);
		this.entitiesById.put(entity.id, entity);
		this.entitiesByName.put(entity.name, entity);

		var entityProperties = entity.properties;
		for (var i = 0; i < entityProperties.length; i++)
		{
			var property = entityProperties[i];
			var propertyName = property.getClass().getName();
			var entitiesWithProperty = this.entitiesByPropertyName(propertyName);
			entitiesWithProperty.add(entity);
		}

		entity.initialize(uwpe);
	}

	public void entitySpawn2(Universe universe, World world, Entity entity)
	{
		this.entitySpawn
		(
			new UniverseWorldPlaceEntities(universe, world, this, entity, null)
		);
	}

	public void entityToRemoveAdd(Entity entityToRemove)
	{
		this.entitiesToRemove.add(entityToRemove);
	}

	public void entityToSpawnAdd(Entity entityToSpawn)
	{
		this.entitiesToSpawn.add(entityToSpawn);
	}

	public void finalize(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.place = this;
		var universe = uwpe.universe;
		this.entitiesRemove();
		universe.inputHelper.inputsRemoveAll();
		for (var i = 0; i < this.entities.size(); i++)
		{
			var entity = this.entities.get(i);
			entity.finalize(uwpe);
		}
	}

	public void initialize(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.place = this;
		var world = uwpe.world;
		var defn = this.defn(world);
		defn.placeInitialize(uwpe);
		this.entitiesSpawn(uwpe);
		this.entitiesToSpawn.clear();
		for (var i = 0; i < this.entities.size(); i++)
		{
			var entity = this.entities.get(i);
			entity.initialize(uwpe);
		}
	}

	public void load(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isLoaded == false)
		{
			var loadables = this.loadables();
			uwpe.place = this;
			loadables.forEach(x -> x.loadable().load(uwpe.entitySet(x) ) );
			this.isLoaded = true;
		}
	}

	public void unload(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isLoaded)
		{
			var loadables = this.loadables();
			uwpe.place = this;
			loadables.forEach(x -> x.loadable().unload(uwpe.entitySet(x) ) );
			this.isLoaded = false;
		}
	}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var world = uwpe.world;

		this.entitiesRemove();

		this.entitiesSpawn(uwpe);

		uwpe.place = this;

		var placeDefn = this.defn(world);
		var propertyNamesToProcess = placeDefn.propertyNamesToProcess;
		for (var p = 0; p < propertyNamesToProcess.length; p++)
		{
			var propertyName = propertyNamesToProcess[p];
			var entitiesWithProperty = this.entitiesByPropertyName(propertyName);
			if (entitiesWithProperty != null)
			{
				for (var i = 0; i < entitiesWithProperty.size(); i++)
				{
					var entity = entitiesWithProperty.get(i);
					var entityProperty = entity.propertiesByName.get(propertyName);
					uwpe.entity = entity;
					entityProperty.updateForTimerTick(uwpe);
				}
			}
		}
	}

	// Controls.

	public ControlBase toControl(Universe universe, World world)
	{
		var player = this.player();
		var playerControllable = player.controllable();
		var returnValue = playerControllable.toControl
		(
			new UniverseWorldPlaceEntities
			(
				universe, world, this, player, null
			)
		);
		return returnValue;
	}

	// Entity convenience accessors.

	public Entity camera()
	{
		return this.entitiesByPropertyName(Camera.class.getName()).get(0);
	}

	public CollisionTracker collisionTracker()
	{
		CollisionTracker returnValue = null;
 
		var collisionTrackerEntity =
			this.entitiesByPropertyName(CollisionTracker.class.getName()).get(0);
		var returnValueAsProperty =
		(
			collisionTrackerEntity == null
			? null
			: collisionTrackerEntity.propertyByName(CollisionTracker.class.getName())
		);
		returnValue = (CollisionTracker)returnValueAsProperty;

		return returnValue;
	}

	public List<Entity> drawables()
	{
		return this.entitiesByPropertyName(Drawable.class.getName());
	}

	public List<Entity> items()
	{
		return this.entitiesByPropertyName(Item.class.getName());
	}

	public List<Entity> loadables()
	{
		return this.entitiesByPropertyName(Loadable.class.getName());
	}

	public List<Entity> movables()
	{
		return this.entitiesByPropertyName(Movable.class.getName());
	}

	public Entity player()
	{
		return this.entitiesByPropertyName(Playable.class.getName()).get(0);
	}

	public List<Entity> usables()
	{
		return this.entitiesByPropertyName(Usable.class.getName());
	}

	// Namable.

	public String name()
	{
		return this._name;
	}
}
