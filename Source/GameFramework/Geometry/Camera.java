
package GameFramework.Geometry;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Collisions.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;
import GameFramework.Model.Places.*;

public class Camera implements EntityProperty<Camera>
{
	public Coords viewSize;
	public double focalLength;
	public Disposition loc;
	public Function<List<Entity>,List<Entity>> _entitiesInViewSort;

	public Coords viewSizeHalf;
	public Box viewCollider;
	public List<Entity> entitiesInView;

	private Plane[] _clipPlanes;
	private Coords _posSaved;

	public Camera
	(
		Coords viewSize, double focalLength, Disposition loc,
		Function<List<Entity>,List<Entity>> entitiesInViewSort
	)
	{
		this.viewSize = viewSize;
		this.focalLength = focalLength;
		this.loc = loc;
		this._entitiesInViewSort = entitiesInViewSort;

		this.viewSizeHalf = this.viewSize.clone().clearZ().half();

		var viewColliderSize = this.viewSize.clone();
		viewColliderSize.z = Double.POSITIVE_INFINITY;
		this.viewCollider = new Box
		(
			this.loc.pos, viewColliderSize
		);
		this.entitiesInView = new ArrayList<Entity>();

		this._posSaved = Coords.create();
	}

	public static Camera _default()
	{
		return new Camera
		(
			new Coords(400, 300, 1000), // viewSize
			150, // focalLength
			Disposition.fromPos(new Coords(0, 0, -150) ),
			null // entitiesInViewSort
		);
	}

	public Plane[] clipPlanes()
	{
		if (this._clipPlanes == null)
		{
			this._clipPlanes = new Plane[]
			{
				new Plane(Coords.create(), 0),
				new Plane(Coords.create(), 0),
				new Plane(Coords.create(), 0),
				new Plane(Coords.create(), 0),
			};
		}

		var cameraLoc = this.loc;
		var cameraOrientation = cameraLoc.orientation;

		var cameraPos = cameraLoc.pos;

		var centerOfViewPlane = cameraPos.clone().add
		(
			cameraOrientation.forward.clone().multiplyScalar
			(
				this.focalLength
			)
		);

		var cornerOffsetRight =	cameraOrientation.right.clone().multiplyScalar
		(
			this.viewSizeHalf.x
		);

		var cornerOffsetDown = cameraOrientation.down.clone().multiplyScalar
		(
			this.viewSizeHalf.y
		);

		var cameraViewCorners = new Coords[]
		{
			centerOfViewPlane.clone().add
			(
				cornerOffsetRight
			).add
			(
				cornerOffsetDown
			),

			centerOfViewPlane.clone().subtract
			(
				cornerOffsetRight
			).add
			(
				cornerOffsetDown
			),

			centerOfViewPlane.clone().subtract
			(
				cornerOffsetRight
			).subtract
			(
				cornerOffsetDown
			),

			centerOfViewPlane.clone().add
			(
				cornerOffsetRight
			).subtract
			(
				cornerOffsetDown
			),

		};

		var numberOfCorners = cameraViewCorners.length;

		for (var i = 0; i < numberOfCorners; i++)
		{
			var iNext = i + 1;
			if (iNext >= numberOfCorners)
			{
				iNext = 0;
			}

			var clipPlane = this._clipPlanes[i];

			var cameraViewCorner = cameraViewCorners[i];
			var cameraViewCornerNext = cameraViewCorners[iNext];

			clipPlane.fromPoints
			(
				cameraPos,
				cameraViewCorner,
				cameraViewCornerNext
			);
		}

		return this._clipPlanes;
	}

	public Coords coordsTransformViewToWorld(Coords viewCoords, boolean ignoreZ)
	{
		var cameraLoc = this.loc;

		if (ignoreZ)
		{
			viewCoords.z = this.focalLength;
		}

		var worldCoords = viewCoords.subtract(this.viewSizeHalf);

		cameraLoc.orientation.unprojectCoordsRDF
		(
			worldCoords
		);

		worldCoords.add
		(
			cameraLoc.pos
		);

		return worldCoords;
	}

	public Coords coordsTransformWorldToView(Coords worldCoords)
	{
		var cameraPos = this.loc.pos;
		var cameraOrientation = this.loc.orientation;

		var viewCoords = worldCoords.subtract(cameraPos);

		cameraOrientation.projectCoordsRDF(viewCoords);

		if (this.focalLength != 0)
		{
			var viewCoordsZ = viewCoords.z;
			if (viewCoordsZ != 0)
			{
				viewCoords.multiplyScalar(this.focalLength).divideScalar(viewCoordsZ);
				viewCoords.z = viewCoordsZ;
			}
		}

		viewCoords.add(this.viewSizeHalf);

		return viewCoords;
	}

	public void drawEntitiesInView
	(
		UniverseWorldPlaceEntities uwpe,
		Entity cameraEntity, Display display
	)
	{
		var universe = uwpe.universe;
		var place = uwpe.place;

		this.loc.pos.round(); // hack - To prevent lines between map tiles.

		this.entitiesInView = this.drawEntitiesInView_1_FindEntitiesInView
		(
			place, cameraEntity, universe.collisionHelper, this.entitiesInView
		);

		this.drawEntitiesInView_2_Draw
		(
			uwpe, display, this.entitiesInView
		);
	}

	public List<Entity> drawEntitiesInView_1_FindEntitiesInView
	(
		Place place,
		Entity cameraEntity,
		CollisionHelper collisionHelper,
		List<Entity> entitiesInView
	)
	{
		var collisionTracker = place.collisionTracker();
		if (collisionTracker == null)
		{
			entitiesInView = this.drawEntitiesInView_1_FindEntitiesInView_WithoutTracker
			(
				place, collisionHelper, entitiesInView
			);
		}
		else
		{
			entitiesInView = this.drawEntitiesInView_1_FindEntitiesInView_WithTracker
			(
				place, cameraEntity, collisionHelper, entitiesInView, collisionTracker
			);
		}

		return entitiesInView;
	}

	private List<Entity> drawEntitiesInView_1_FindEntitiesInView_WithTracker
	(
		Place place,
		Entity cameraEntity,
		CollisionHelper collisionHelper,
		List<Entity> entitiesInView,
		CollisionTracker collisionTracker
	)
	{
		var cameraCollidable = cameraEntity.collidable();
		//cameraCollidable.isDisabled = false;
		ArrayHelper.clear(cameraCollidable.entitiesAlreadyCollidedWith);
		var collisions = collisionTracker.entityCollidableAddAndFindCollisions
		(
			cameraEntity, collisionHelper, new ArrayList<Collision>()
		);
		var entitiesCollidedWith =
			collisions.stream().map(x -> x.entitiesColliding.get(1) );
		// todo - Should entitiesInView be cleared out first?
		entitiesInView.addAll
		(
			entitiesCollidedWith.filter(x -> x.drawable() != null)
		);
		//cameraCollidable.isDisabled = true;

		var drawablesAll = place.drawables();
		var drawablesUnboundable = drawablesAll.stream().filter
		(
			x -> x.boundable() == null
		).collect(Collectors.toList());
		entitiesInView.addAll(drawablesUnboundable);

		return entitiesInView;
	}

	private List<Entity> drawEntitiesInView_1_FindEntitiesInView_WithoutTracker
	(
		Place place,
		CollisionHelper collisionHelper,
		List<Entity> entitiesInView
	)
	{
		ArrayHelper.clear(entitiesInView);

		var placeEntitiesDrawable = place.drawables();

		for (var i = 0; i < placeEntitiesDrawable.size(); i++)
		{
			var entity = placeEntitiesDrawable.get(i);
			var drawable = entity.drawable();
			if (drawable.isVisible)
			{
				var entityPos = entity.locatable().loc.pos;
				this._posSaved.overwriteWith(entityPos);

				this.coordsTransformWorldToView(entityPos);

				var isEntityInView = false;
				var boundable = entity.boundable();
				if (boundable == null) // todo
				{
					isEntityInView = true;
				}
				else
				{
					var entityCollider = boundable.bounds;
					isEntityInView = collisionHelper.doCollidersCollide
					(
						entityCollider, this.viewCollider
					);
				}

				if (isEntityInView)
				{
					entitiesInView.add(entity);
				}

				entityPos.overwriteWith(this._posSaved);
			}
		}

		return entitiesInView;
	}

	private void drawEntitiesInView_2_Draw
	(
		UniverseWorldPlaceEntities uwpe,
		Display display,
		List<Entity> entitiesInView
	)
	{
		this.entitiesInViewSort(entitiesInView);

		for (var i = 0; i < entitiesInView.size(); i++)
		{
			var entity = entitiesInView.get(i);
			uwpe.entity = entity;

			var visual = entity.drawable().visual;

			var entityPos = entity.locatable().loc.pos;

			this._posSaved.overwriteWith(entityPos);

			this.coordsTransformWorldToView(entityPos);

			visual.draw(uwpe, display);

			entityPos.overwriteWith(this._posSaved);
		}
	}

	private List<Entity> entitiesInViewSort(List<Entity> entitiesToSort)
	{
		List<Entity> entitiesSorted = null;

		if (this._entitiesInViewSort == null)
		{
			entitiesSorted = entitiesToSort;
		}
		else
		{
			entitiesSorted = this._entitiesInViewSort.apply(entitiesToSort);
		}

		return entitiesSorted;
	}

	public Entity toEntity()
	{
		return new Entity(Camera.class.getName(), new EntityProperty[] { this } );
	}

	// Clonable.

	public Camera clone()
	{
		return this; // todo
	}

	public Camera overwriteWith(Camera other)
	{
		return this; // todo
	}
	
	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		// Do nothing.  Rendering is done in Place.draw().
	}
}
