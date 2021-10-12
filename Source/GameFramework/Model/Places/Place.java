
package GameFramework.Model.Places;

import java.util.*;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Collisions.*;
import GameFramework.Model.*;

public class Place //
{
	public String name;
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
		this.name = name;
		this.defnName = defnName;
		this.size = size;
		this.entities = new ArrayList<Entity>();
		this.entitiesById = new HashMap<Integer,Entity>();
		this.entitiesByName = new HashMap<String,Entity>();

		this._entitiesByPropertyName = new HashMap<String,List<Entity>>();
		this.entitiesToSpawn = new ArrayList<Entity>();
		this.entitiesToSpawn.addAll(entities);
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
		for (var i = 0; i < this.entitiesToRemove.length; i++)
		{
			var entity = this.entitiesToRemove[i];
			this.entityRemove(entity);
		}
		this.entitiesToRemove.length = 0;
	}

	public void entitiesToRemoveAdd(Entity[] entitiesToRemove)
	{
		this.entitiesToRemove.addAll(entitiesToRemove);
	}

	public void entitiesToSpawnAdd(Entity entitiesToSpawn[])
	{
		this.entitiesToSpawn.addAll(entitiesToSpawn);
	}

	public void entitiesSpawn(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.place = this;
		for (var i = 0; i < this.entitiesToSpawn.length; i++)
		{
			var entity = this.entitiesToSpawn[i];
			uwpe.entity = entity;
			this.entitySpawn(uwpe);
		}

		this.entitiesToSpawn.length = 0;
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
		for (var p = 0; p < entityProperties.length; p++)
		{
			var property = entityProperties[p];
			var propertyName = property.constructor.name;
			var entitiesWithProperty =
				this.entitiesByPropertyName(propertyName);
			ArrayHelper.remove(entitiesWithProperty, entity);
		}
		ArrayHelper.remove(this.entities, entity);
		this.entitiesById.delete(entity.id);
		this.entitiesByName.delete(entity.name);
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
			var propertyName = property.constructor.name;
			var entitiesWithProperty = this.entitiesByPropertyName(propertyName);
			entitiesWithProperty.push(entity);
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
		this.entitiesToRemove.push(entityToRemove);
	}

	public void entityToSpawnAdd(Entity entityToSpawn)
	{
		this.entitiesToSpawn.push(entityToSpawn);
	}

	public void finalize(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.place = this;
		var universe = uwpe.universe;
		this.entitiesRemove();
		universe.inputHelper.inputsRemoveAll();
		for (var i = 0; i < this.entities.length; i++)
		{
			var entity = this.entities[i];
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
		this.entitiesToSpawn.length = 0;
		for (var i = 0; i < this.entities.length; i++)
		{
			var entity = this.entities[i];
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
				for (var i = 0; i < entitiesWithProperty.length; i++)
				{
					var entity = entitiesWithProperty[i];
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
			universe, universe.display.sizeInPixels, player, null, false
		);
		return returnValue;
	}

	// Entity convenience accessors.

	public Entity camera()
	{
		return this.entitiesByPropertyName(Camera.name)[0];
	}

	public CollisionTracker collisionTracker()
	{
		CollisionTracker returnValue = null;
 
		if (typeof(CollisionTracker) != "undefined")
		{
			var collisionTrackerEntity =
				this.entitiesByPropertyName(CollisionTracker.name)[0];
			var returnValueAsProperty =
			(
				collisionTrackerEntity == null
				? null
				: collisionTrackerEntity.propertyByName(CollisionTracker.name)
			);
			returnValue = (CollisionTracker)returnValueAsProperty;
		}
		return returnValue;
	}

	public List<Entity> drawables()
	{
		return this.entitiesByPropertyName(Drawable.class.name);
	}

	public List<Entity> items()
	{
		return this.entitiesByPropertyName(Item.class.name);
	}

	public List<Entity> loadables()
	{
		return this.entitiesByPropertyName(Loadable.class.name);
	}

	public List<Entity> movables()
	{
		return this.entitiesByPropertyName(Movable.class.name);
	}

	public List<Entity> player()
	{
		return this.entitiesByPropertyName(Playable.class.name)[0];
	}

	public List<Entity> usables()
	{
		return this.entitiesByPropertyName(Usable.class.name);
	}

}
