
package GameFramework.Geometry.Collisions;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Shapes.Maps.*;
import GameFramework.Geometry.Shapes.Meshes.*;
import GameFramework.Model.*;

public class CollisionHelper
{
	public boolean throwErrorIfCollidersCannotBeCollided;
	public Map<String,Map<String,Object>> colliderTypeNamesToDoCollideLookup;
	public Map<String,Map<String,Object>> colliderTypeNamesToDoesContainLookup;
	public Map<String,Map<String,Object>> colliderTypeNamesToCollisionFindLookup;

	private Box _box;
	private Box _box2;
	private Collision _collision;
	private Edge _edge;
	private Polar _polar;
	private Coords _pos;
	private RangeExtent _range;
	private RangeExtent _range2;
	private Coords _size;
	private Coords _vel;
	private Coords _vel2;

	public CollisionHelper()
	{
		this.throwErrorIfCollidersCannotBeCollided = true;

		this.colliderTypeNamesToDoCollideLookup = this.doCollideLookupBuild();
		this.colliderTypeNamesToDoesContainLookup = this.doesContainLookupBuild();
		this.colliderTypeNamesToCollisionFindLookup = this.collisionFindLookupBuild();

		// Helper variables.
		this._box = Box.create();
		this._box2 = Box.create();
		this._collision = Collision.create();
		this._displacement = Coords.create();
		this._edge = Edge.create();
		this._polar = Polar.create();
		this._pos = Coords.create();
		this._range = RangeExtent.create();
		this._range2 = RangeExtent.create();
		this._size = Coords.create();
		this._vel = Coords.create();
		this._vel2 = Coords.create();
	}

	// constructor helpers

	public Map<String,Map<String,Object>> collisionFindLookupBuild()
	{
		var lookupOfLookups = new Map<String,Map<String,Object>>();
		Map<String, Object> lookup;

		var boxName = Box.class.getName();
		var boxRotatedName = BoxRotated.class.getName();
		var mapLocatedName = MapLocated.class.getName();
		var meshName = Mesh.class.getName();
		var shapeGroupAllName = ShapeGroupAll.class.getName();
		var shapeInverseName = ShapeInverse.class.getName();
		var sphereName = Sphere.class.getName();

		if (boxName != null)
		{
			lookup = new HashMap<String, Object>()
			{{
				put(boxName, this.collisionOfBoxAndBox);
				put(boxRotatedName, this.collisionOfBoxAndBoxRotated);
				put(mapLocatedName, this.collisionOfBoxAndMapLocated);
				put(meshName, this.collisionOfBoxAndMesh);
				put(shapeGroupAllName, this.collisionOfShapeAndShapeGroupAll);
				put(shapeInverseName, this.collisionOfShapeAndShapeInverse);
				put(sphereName, this.collisionOfBoxAndSphere);
			}};
			lookupOfLookups.put(boxName, lookup);
		}
		
		if (boxRotatedName != null)
		{
			lookup = new HashMap<String, Object>()
			{{
				put(boxName, this.collisionOfBoxRotatedAndBox);
				put(boxRotatedName, this.collisionOfBoxRotatedAndBoxRotated);
			}};
			lookupOfLookups.put(boxRotatedName, lookup);
		}

		if (mapLocatedName != null)
		{
			lookup = new HashMap<String, Object>()
			{{
				put(boxName, this.collisionOfMapLocatedAndBox);
				put(boxRotatedName, this.collisionOfMapLocatedAndBoxRotated);
				put(mapLocatedName, this.collisionOfMapLocatedAndMapLocated);
				put(shapeGroupAllName, this.collisionOfShapeAndShapeGroupAll);
				put(sphereName, this.collisionOfMapLocatedAndSphere);
			}};
			lookupOfLookups.put(mapLocatedName, lookup);
		}

		if (meshName != null)
		{
			lookup = new HashMap<String, Object>()
			{{
				put(boxName, this.collisionOfMeshAndBox);
				put(shapeGroupAllName, this.collisionOfShapeAndShapeGroupAll);
				put(shapeInverseName, this.collisionOfShapeAndShapeInverse);
				put(sphereName, this.collisionOfMeshAndSphere);
			}};
			lookupOfLookups.put(meshName, lookup);
		}

		if (shapeGroupAllName != null)
		{
			lookup = new HashMap<String, Object>()
			{{
				put(boxName, this.collisionOfShapeGroupAllAndShape);
				put(meshName, this.collisionOfShapeGroupAllAndShape);
				put(sphereName, this.collisionOfShapeGroupAllAndShape);
			}};
			lookupOfLookups.put(shapeGroupAllName, lookup);
		}

		if (shapeInverseName != null)
		{
			lookup = new HashMap<String, Object>()
			{{
				put(boxName, this.collisionOfShapeInverseAndShape);
				put(meshName, this.collisionOfShapeInverseAndShape);
				put(sphereName, this.collisionOfShapeInverseAndShape);
			}};
			lookupOfLookups.put(shapeInverseName, lookup);
		}

		if (sphereName != null)
		{
			lookup = new HashMap<String, Object>()
			{{
				put(boxName, this.collisionOfSphereAndBox);
				put(boxRotatedName, this.collisionOfSphereAndBoxRotated);
				put(mapLocatedName, this.collisionOfSphereAndMapLocated);
				put(meshName, this.collisionOfSphereAndMesh);
				put(shapeGroupAllName, this.collisionOfShapeAndShapeGroupAll);
				put(shapeInverseName, this.collisionOfShapeAndShapeInverse);
				put(sphereName, this.collisionOfSpheres);
			}};
			lookupOfLookups.put(sphereName, lookup);
		}

		return lookupOfLookups;
	}

	public Map<String,Map<String,Object>> doCollideLookupBuild()
	{
		var lookupOfLookups = new Map<String,Map<String,Object>>();

		var andText = "And";
		var collideText = "Collide";
		var doText = "do";

		var functionNames = Object.getOwnPropertyNames(Object.getPrototypeOf(this));
		var functionNamesDoCollide = functionNames.filter(
			x -> x.startsWith(doText) && x.endsWith(collideText) && x.indexOf(andText) >= 0
		);

		for (var i = 0; i < functionNamesDoCollide.length; i++)
		{
			var functionName = functionNamesDoCollide[i];

			var colliderTypeNamesAsString = functionName.substr
			(
				doText.length,
				functionName.length - doText.length - collideText.length
			);

			var colliderTypeNames = colliderTypeNamesAsString.split(andText);
			var colliderTypeName0 = colliderTypeNames[0];
			var colliderTypeName1 = colliderTypeNames[1];

			var lookup = lookupOfLookups.get(colliderTypeName0);
			if (lookup == null)
			{
				lookup = new Map<String,Object>();
				lookupOfLookups.put(colliderTypeName0, lookup);
			}
			var thisAsAny = (Object)this;
			var doCollideFunction = thisAsAny[functionName];
			lookup.put(colliderTypeName1, doCollideFunction);
		}

		return lookupOfLookups;
	}

	public Map<String,Map<String,Object>> doesContainLookupBuild()
	{
		var lookupOfLookups = new HashMap<String,Map<String,Object>>();

		var containText = "Contain";
		var doesText = "does";

		var functionNames = Object.getOwnPropertyNames(Object.getPrototypeOf(this));
		var functionNamesDoesContain = functionNames.filter(
			x -> x.startsWith(doesText) && x.indexOf(containText) >= 0
		);

		for (var i = 0; i < functionNamesDoesContain.length; i++)
		{
			var functionName = functionNamesDoesContain[i];

			var colliderTypeNamesAsString = functionName.substr
			(
				doesText.length
			);

			var colliderTypeNames = colliderTypeNamesAsString.split(containText);
			var colliderTypeName0 = colliderTypeNames[0];
			var colliderTypeName1 = colliderTypeNames[1];

			var lookup = lookupOfLookups.get(colliderTypeName0);
			if (lookup == null)
			{
				lookup = new Map<String, Object>();
				lookupOfLookups.put(colliderTypeName0, lookup);
			}
			var thisAsAny = (Object)this;
			var doesContainFunction = thisAsAny[functionName];
			lookup.put(colliderTypeName1, doesContainFunction);
		}

		return lookupOfLookups;
	}

	// instance methods

	public Collision collisionActiveClosest(List<Collision> collisionsToCheck)
	{
		var returnValue = collisionsToCheck.filter
		(
			x -> x.isActive
		).sort
		(
			(Collision x, Collision y) -> x.distanceToCollision - y.distanceToCollision
		)[0];

		return returnValue;
	}

	public Collision collisionOfEntities
	(
		Entity entityColliding, Entity entityCollidedWith, Collision collisionOut
	)
	{
		var collider0 = entityColliding.collidable().collider;
		var collider1 = entityCollidedWith.collidable().collider;

		collisionOut = this.collisionOfColliders(collider0, collider1, collisionOut);

		var entitiesColliding = collisionOut.entitiesColliding;
		entitiesColliding.add(entityColliding);
		entitiesColliding.add(entityCollidedWith);

		return collisionOut;
	}

	public Collision collisionOfColliders
	(
		Object collider0, Object collider1, Collision collisionOut
	)
	{
		collisionOut.clear();

		// Prevents having to add some composite shapes, for example, Shell.
		while (collider0.collider != null)
		{
			collider0 = collider0.collider();
		}

		while (collider1.collider != null)
		{
			collider1 = collider1.collider();
		}

		var collider0TypeName = collider0.constructor.class.getName();
		var collider1TypeName = collider1.constructor.class.getName();

		var collideLookup =
			this.colliderTypeNamesToCollisionFindLookup.get(collider0TypeName);
		if (collideLookup == null)
		{
			if (this.throwErrorIfCollidersCannotBeCollided)
			{
				throw new Exception("Error!  Colliders of types cannot be collided: " + collider0TypeName + "," + collider1TypeName);
			}
		}
		else
		{
			var collisionMethod = collideLookup.get(collider1TypeName);
			if (collisionMethod == null)
			{
				if (this.throwErrorIfCollidersCannotBeCollided)
				{
					throw new Exception("Error!  Colliders of types cannot be collided: " + collider0TypeName + "," + collider1TypeName);
				}
			}
			else
			{
				collisionMethod.call
				(
					this, collider0, collider1, collisionOut, true // shouldCalculatePos
				);
			}
		}

		return collisionOut;
	}

	public List<Collision> collisionsOfEntitiesCollidableInSets
	(
		Entity entitiesCollidable0[], Entity entitiesCollidable1[]
	)
	{
		var returnValues = new ArrayList<Collision>();

		for (var i = 0; i < entitiesCollidable0.length; i++)
		{
			var entity0 = entitiesCollidable0[i];

			for (var j = 0; j < entitiesCollidable1.length; j++)
			{
				var entity1 = entitiesCollidable1[j];

				var doCollide = this.doEntitiesCollide(entity0, entity1);

				if (doCollide)
				{
					var collision = Collision.create();
					collision.entitiesColliding.add(entity0);
					collision.entitiesColliding.add(entity1);
					returnValues.add(collision);
				}
			}
		}

		return returnValues;
	}

	public boolean doEntitiesCollide(Entity entity0, Entity entity1)
	{
		var doCollidersCollide = false;

		var collidable0 = entity0.collidable();
		var collidable1 = entity1.collidable();

		var collider0 = collidable0.collider;
		var collider1 = collidable1.collider;

		doCollidersCollide = this.doCollidersCollide(collider0, collider1);

		return doCollidersCollide;
	}

	public boolean doCollidersCollide(Object collider0, Object collider1)
	{
		var returnValue = false;

		while (collider0.collider != null)
		{
			collider0 = collider0.collider();
		}

		while (collider1.collider != null)
		{
			collider1 = collider1.collider();
		}

		var collider0TypeName = collider0.getType().name;
		var collider1TypeName = collider1.getType().name;

		var doCollideLookup =
			this.colliderTypeNamesToDoCollideLookup.get(collider0TypeName);
		if (doCollideLookup == null)
		{
			if (this.throwErrorIfCollidersCannotBeCollided)
			{
				throw new Exception("Colliders Error of types cannot be collided: " + collider0TypeName + ", " + collider1TypeName);
			}
		}
		else
		{
			var collisionMethod = doCollideLookup.get(collider1TypeName);
			if (collisionMethod == null)
			{
				if (this.throwErrorIfCollidersCannotBeCollided)
				{
					throw new Exception("Colliders Error of types cannot be collided: " + collider0TypeName + ", " + collider1TypeName);
				}
			}
			else
			{
				returnValue = collisionMethod.call
				(
					this, collider0, collider1
				);
			}
		}

		return returnValue;
	}

	public boolean doesColliderContainOther(Object collider0, Object collider1)
	{
		var returnValue = false;

		while (collider0.collider != null)
		{
			collider0 = collider0.collider();
		}

		while (collider1.collider != null)
		{
			collider1 = collider1.collider();
		}

		var collider0TypeName = collider0.constructor.class.getName();
		var collider1TypeName = collider1.constructor.class.getName();

		var doesContainLookup =
			this.colliderTypeNamesToDoesContainLookup.get(collider0TypeName);
		if (doesContainLookup == null)
		{
			if (this.throwErrorIfCollidersCannotBeCollided)
			{
				throw new Exception("Colliders Error of types cannot be collided: " + collider0TypeName + ", " + collider1TypeName);
			}
		}
		else
		{
			var doesColliderContainOther = doesContainLookup.get(collider1TypeName);
			if (doesColliderContainOther == null)
			{
				if (this.throwErrorIfCollidersCannotBeCollided)
				{
					throw new Exception("Colliders Error of types cannot be collided: " + collider0TypeName + ", " + collider1TypeName);
				}
			}
			else
			{
				returnValue = doesColliderContainOther.call
				(
					this, collider0, collider1
				);
			}
		}

		return returnValue;
	}

	// shapes

	// collideEntitiesXAndY

	public void collideEntitiesBackUp(Entity entity0, Entity entity1)
	{
		var collidable0 = entity0.collidable();
		var collidable1 = entity1.collidable();

		var entity0Loc = entity0.locatable().loc;
		var entity1Loc = entity1.locatable().loc;

		var pos0 = entity0Loc.pos;
		var pos1 = entity1Loc.pos;

		var vel0 = entity0Loc.vel;
		var vel1 = entity1Loc.vel;

		var speed0 = vel0.magnitude();
		var speed1 = vel1.magnitude();
		var speedMax = Math.max(speed0, speed1);

		var vel0InvertedNormalized =
			this._vel.overwriteWith(vel0).invert().normalize();
		var vel1InvertedNormalized =
			this._vel2.overwriteWith(vel1).invert().normalize();

		var distanceBackedUpSoFar = 0;

		while
		(
			this.doEntitiesCollide(entity0, entity1)
			&& distanceBackedUpSoFar < speedMax
		)
		{
			distanceBackedUpSoFar++;

			pos0.add(vel0InvertedNormalized);
			pos1.add(vel1InvertedNormalized);

			collidable0.colliderLocateForEntity(entity0);
			collidable1.colliderLocateForEntity(entity1);
		}
	}

	public void collideEntitiesBlock(Entity entity0, Entity entity1)
	{
		// todo - Needs separation as well.
		this.collideEntitiesBlockOrBounce(entity0, entity1, 0); // coefficientOfRestitution
	}

	public void collideEntitiesBounce(Entity entity0, Entity entity1)
	{
		this.collideEntitiesBlockOrBounce(entity0, entity1, 1); // coefficientOfRestitution
	}

	public void collideEntitiesBlockOrBounce
	(
		Entity entity0, Entity entity1, double coefficientOfRestitution
	)
	{
		var collisionPos = this.collisionOfEntities
		(
			entity0, entity1, this._collision
		).pos;

		var collidable0 = entity0.collidable();
		var collidable1 = entity1.collidable();

		var collider0 = collidable0.collider;
		var collider1 = collidable1.collider;

		var normal0 = collider0.normalAtPos
		(
			collisionPos, Coords.create() // normalOut
		);
		var normal1 = collider1.normalAtPos
		(
			collisionPos, Coords.create() // normalOut
		);

		var entity0Loc = entity0.locatable().loc;
		var entity1Loc = entity1.locatable().loc;

		var vel0 = entity0Loc.vel;
		var vel1 = entity1Loc.vel;

		var vel0DotNormal1 = vel0.dotProduct(normal1);
		var vel1DotNormal0 = vel1.dotProduct(normal0);

		var multiplierOfRestitution = 1 + coefficientOfRestitution;

		if (vel0DotNormal1 < 0)
		{
			var vel0Bounce = normal1.multiplyScalar
			(
				0 - vel0DotNormal1
			).multiplyScalar
			(
				multiplierOfRestitution
			);

			vel0.add(vel0Bounce);
			entity0Loc.orientation.forwardSet
			(
				this._vel.overwriteWith(vel0).normalize()
			);
		}

		if (vel1DotNormal0 < 0)
		{
			var vel1Bounce = normal0.multiplyScalar
			(
				0 - vel1DotNormal0
			).multiplyScalar
			(
				multiplierOfRestitution
			);
			vel1.add(vel1Bounce);
			entity1Loc.orientation.forwardSet
			(
				this._vel.overwriteWith(vel1).normalize()
			);
		}
	}

	public void collideEntitiesSeparate(Entity entity0, Entity entity1)
	{
		var entity0Loc = entity0.locatable().loc;
		var entity0Pos = entity0Loc.pos;
		var collidable1 = entity1.collidable();
		var collider1 = collidable1.collider;

		var collider1Normal = collider1.normalAtPos
		(
			entity0Pos, Coords.create() // normalOut
		);

		var distanceMovedSoFar = 0;
		var distanceToMoveMax = 10;

		while (this.doEntitiesCollide(entity0, entity1) && distanceMovedSoFar < distanceToMoveMax)
		{
			distanceMovedSoFar++;
			entity0Pos.add(collider1Normal);

			var collidable0 = entity0.collidable();
			collidable0.colliderLocateForEntity(entity0);
		}
	}

	// collisionOfXAndY

	public Collision collisionOfBoxAndBox
	(
		Box box1, Box box2, Collision collision
	)
	{
		if (collision == null)
		{
			collision = Collision.create();
		}

		var boxOfIntersection = box1.intersectWith(box2);
		if (boxOfIntersection != null)
		{
			collision.isActive = true;
			collision.pos.overwriteWith(boxOfIntersection.center);
		}

		return collision;
	}

	public Collision collisionOfBoxAndBoxRotated
	(
		Box box, BoxRotated boxRotated,
		Collision collision, boolean shouldCalculatePos
	)
	{
		// hack
		return this.collisionOfBoxAndSphere
		(
			box, boxRotated.sphereSwept(), collision, shouldCalculatePos
		);
	}

	public Collision collisionOfBoxAndMapLocated
	(
		Box box, MapLocated mapLocated, Collision collision
	)
	{
		var doBoundsCollide =
			this.doBoxAndBoxCollide(mapLocated.box, box);

		if (doBoundsCollide == false)
		{
			return collision;
		}

		var map = mapLocated.map;
		var cell = map.cellCreate();
		var cellPosAbsolute = Coords.create();
		var cellPosInCells = Coords.create();
		var mapSizeInCells = map.sizeInCells;
		var mapCellSize = map.cellSize;
		var mapSizeHalf = map.sizeHalf;
		var mapPos = mapLocated.loc.pos;
		var cellAsBox = new Box( Coords.create(), map.cellSize );

		for (var y = 0; y < mapSizeInCells.y; y++)
		{
			cellPosInCells.y = y;
			cellPosAbsolute.y = (y * mapCellSize.y) + mapPos.y - mapSizeHalf.y;

			for (var x = 0; x < mapSizeInCells.x; x++)
			{
				cellPosInCells.x = x;
				cellPosAbsolute.x = (x * mapCellSize.x) + mapPos.x - mapSizeHalf.x;

				cell = map.cellAtPosInCells(cellPosInCells);

				if (cell.isBlocking)
				{
					cellAsBox.center.overwriteWith(cellPosAbsolute);
					var doCellAndBoxCollide =
						this.doBoxAndBoxCollide(cellAsBox, box);
					if (doCellAndBoxCollide)
					{
						collision.isActive = true;
						collision.pos.overwriteWith(cellAsBox.center);
						break;
					}
				}
			}
		}

		return collision;
	}

	public Collision collisionOfBoxAndMesh
	(
		Box box, Mesh mesh, Collision collision
	)
	{
		if (collision == null)
		{
			collision = Collision.create();
		}

		// hack
		var meshBoundsAsBox = mesh.box();

		var boxOfIntersection = box.intersectWith(meshBoundsAsBox);
		if (boxOfIntersection != null)
		{
			collision.isActive = true;
			collision.pos.overwriteWith(boxOfIntersection.center);
		}

		return collision;
	}

	public Collision collisionOfBoxAndSphere
	(
		Box box, Sphere sphere, Collision collision, boolean shouldCalculatePos
	)
	{
		var doCollide = false;

		var displacementBetweenCenters = this._displacement.overwriteWith
		(
			sphere.center
		).subtract
		(
			box.center
		);

		var displacementBetweenCentersAbsolute =
			displacementBetweenCenters.absolute();

		var boxSizeHalf = box.sizeHalf();
		var sphereRadius = sphere.radius;

		var doExtentsCollide =
		(
			displacementBetweenCentersAbsolute.x <= boxSizeHalf.x + sphereRadius
			&& displacementBetweenCentersAbsolute.y <= boxSizeHalf.y + sphereRadius
			&& displacementBetweenCentersAbsolute.z <= boxSizeHalf.z + sphereRadius
		);

		if (doExtentsCollide)
		{
			var isSphereNotAtCorner =
			(
				displacementBetweenCentersAbsolute.x < boxSizeHalf.x
				|| displacementBetweenCentersAbsolute.y < boxSizeHalf.y
				|| displacementBetweenCentersAbsolute.z < boxSizeHalf.z
			);

			if (isSphereNotAtCorner)
			{
				doCollide = true;
			}
			else
			{
				var distanceBetweenCenters =
					displacementBetweenCentersAbsolute.magnitude();
				var boxDiagonal = boxSizeHalf.magnitude();

				var doesSphereContainCornerOfBox =
				(
					distanceBetweenCenters < (boxDiagonal + sphereRadius)
				);

				doCollide = doesSphereContainCornerOfBox;
			}
		}

		if (collision == null)
		{
			collision = Collision.create();
		}

		collision.isActive = doCollide;

		if (doCollide && shouldCalculatePos)
		{
			// todo - Fix this.
			var boxCircumscribedAroundSphere = new Box
			(
				sphere.center,
				new Coords(1, 1, 1).multiplyScalar(sphere.radius * 2)
			);
			collision = this.collisionOfBoxAndBox
			(
				box, boxCircumscribedAroundSphere, collision
			);
		}

		return collision;
	}

	public Collision collisionOfBoxRotatedAndBox
	(
		BoxRotated boxRotated, Box box,
		Collision collision, boolean shouldCalculatePos
	)
	{
		return this.collisionOfBoxAndBoxRotated
		(
			box, boxRotated, collision, shouldCalculatePos
		);
	}

	public Collision collisionOfBoxRotatedAndBoxRotated
	(
		BoxRotated boxRotated0, BoxRotated boxRotated1,
		Collision collision, boolean shouldCalculatePos
	)
	{
		return this.collisionOfBoxAndBox
		(
			boxRotated0.box, boxRotated1.box, collision
		); // todo
	}

	public Collision collisionOfBoxRotatedAndMapLocated
	(
		BoxRotated boxRotated, MapLocated mapLocated,
		Collision collision, boolean shouldCalculatePos
	)
	{
		return collision; // todo
	}

	public Collision collisionOfBoxRotatedAndSphere
	(
		BoxRotated boxRotated,
		Sphere sphere,
		Collision collision,
		boolean shouldCalculatePos
	)
	{
		if (collision == null)
		{
			collision = Collision.create();
		}

		var doCollide = this.doBoxRotatedAndSphereCollide
		(
			boxRotated, sphere
		);

		if (doCollide)
		{
			var collisionPos = collision.pos;
			var rectangleCenter = boxRotated.box.center;
			var displacementBetweenCenters = collisionPos.overwriteWith
			(
				sphere.center
			).subtract
			(
				rectangleCenter
			);

			var distanceBetweenCenters = displacementBetweenCenters.magnitude();
			var distanceFromRectangleCenterToSphere =
				distanceBetweenCenters - sphere.radius;
			var displacementToSphere = displacementBetweenCenters.divideScalar
			(
				distanceBetweenCenters
			).multiplyScalar
			(
				distanceFromRectangleCenterToSphere
			);

			collisionPos = displacementToSphere.add(rectangleCenter);

			var normals = collision.normals;
			boxRotated.normalAtPos(collision.pos, normals[0]);
			normals[1].overwriteWith(normals[0]).invert();

			var colliders = collision.colliders;
			colliders[0] = boxRotated;
			colliders[1] = sphere;

			return collision;
		}

		return collision;
	}

	public Collision collisionOfEdgeAndEdge
	(
		Edge edge0, Edge edge1, Collision collision
	)
	{
		// 2D

		if (collision == null)
		{
			collision = Collision.create();
		}
		collision.clear();

		var edge0Bounds = edge0.toBox(this._box);
		var edge1Bounds = edge1.toBox(this._box2);

		var doBoundsOverlap = edge0Bounds.overlapsWithXY(edge1Bounds);

		if (doBoundsOverlap)
		{
			var edge0ProjectedOntoEdge1 = this._edge.overwriteWith(
				edge0
			).projectOntoOther(edge1);

			var edgeProjectedVertices = edge0ProjectedOntoEdge1.vertices;
			var edgeProjectedVertex0 = edgeProjectedVertices[0];
			var edgeProjectedVertex1 = edgeProjectedVertices[1];

			var doesEdgeCrossLineOfOther =
				(edgeProjectedVertex0.y > 0 && edgeProjectedVertex1.y <= 0)
				|| (edgeProjectedVertex0.y <= 0 && edgeProjectedVertex1.y > 0);

			if (doesEdgeCrossLineOfOther)
			{
				var edgeProjectedDirection = edge0ProjectedOntoEdge1.direction();

				var distanceAlongEdge0ToLineOfEdge1 =
					0
					- edgeProjectedVertex0.y
					/ edgeProjectedDirection.y;

				var distanceAlongEdge1ToEdge0 =
					edgeProjectedVertex0.x + edgeProjectedDirection.x * distanceAlongEdge0ToLineOfEdge1;

				var doesEdgeCrossOtherWithinItsExtent =
				(
					distanceAlongEdge1ToEdge0 >= 0
					&& distanceAlongEdge1ToEdge0 <= edge1.length()
				);

				if (doesEdgeCrossOtherWithinItsExtent)
				{
					collision.isActive = true;
					collision.distanceToCollision = distanceAlongEdge0ToLineOfEdge1;
					collision.pos.overwriteWith
					(
						edge0.direction()
					).multiplyScalar
					(
						distanceAlongEdge0ToLineOfEdge1
					).add
					(
						edge0.vertices[0]
					);
					collision.colliders.add(edge1);
					collision.collidersByName.put(Edge.class.getName(), edge1);
				}

			} // end if (doesEdgeCrossLineOfOther)

		} // end if (doBoundsOverlap)

		return collision;
	}

	public Collision collisionOfEdgeAndFace
	(
		Edge edge, Face face, Collision collision
	)
	{
		var facePlane = face.plane();

		collision = this.collisionOfEdgeAndPlane
		(
			edge, facePlane, collision
		);

		if (collision.isActive)
		{
			var isWithinFace = face.containsPoint
			(
				collision.pos
			);

			collision.isActive = isWithinFace;

			if (isWithinFace)
			{
				collision.colliders.add(face);
				collision.collidersByName.put(Face.class.getName(), face);
			}
		}

		return collision;
	}

	public List<Collision> collisionsOfEdgeAndMesh
	(
		Edge edge, Mesh mesh, List<Collision> collisions, boolean stopAfterFirst
	)
	{
		if (collisions == null)
		{
			collisions = new ArrayList<Collision>();
		}

		var meshFaces = mesh.faces();
		for (var i = 0; i < meshFaces.length; i++)
		{
			var meshFace = meshFaces[i];
			var collision = this.collisionOfEdgeAndFace
			(
				edge, meshFace, Collision.create()
			);
			if (collision.isActive)
			{
				collision.colliders.add(mesh);
				collision.collidersByName.put(Mesh.class.getName(), mesh);
				collisions.add(collision);
				if (stopAfterFirst)
				{
					break;
				}
			}
		}

		return collisions;
	}

	public Collision collisionOfEdgeAndPlane
	(
		Edge edge, Plane plane, Collision collision
	)
	{
		if (collision == null)
		{
			collision = Collision.create();
		}

		var returnValue = collision;

		var edgeVertex0 = edge.vertices[0];
		var edgeDirection = edge.direction();

		var planeNormal = plane.normal;

		var edgeDirectionDotPlaneNormal = edgeDirection.dotProduct(planeNormal);
		var doesEdgeGoTowardPlane = (edgeDirectionDotPlaneNormal < 0);

		if (doesEdgeGoTowardPlane)
		{
			var distanceToCollision =
				(
					plane.distanceFromOrigin
					- planeNormal.dotProduct(edgeVertex0)
				)
				/ planeNormal.dotProduct(edgeDirection);

			var edgeLength = edge.length();

			if (distanceToCollision >= 0 && distanceToCollision <= edgeLength)
			{
				collision.isActive = true;

				collision.pos.overwriteWith
				(
					edgeDirection
				).multiplyScalar
				(
					distanceToCollision
				).add
				(
					edge.vertices[0]
				);

				collision.distanceToCollision = distanceToCollision;

				var colliders = returnValue.colliders;
				var collidersByName = returnValue.collidersByName;

				colliders.length = 0;
				collidersByName.clear();

				colliders.add(edge);
				collidersByName.put(Edge.class.getName(), edge);
				colliders.add(plane);
				collidersByName.put(Plane.class.getName(), plane);
			}
		}

		return returnValue;
	}

	public Collision collisionOfHemispaceAndBox
	(
		Hemispace hemispace, Box box, Collision collision
	)
	{
		if (collision == null)
		{
			collision = Collision.create();
		}

		var plane = hemispace.plane;
		var boxVertices = box.vertices();
		for (var i = 0; i < boxVertices.length; i++)
		{
			var vertex = boxVertices[i];
			var distanceOfVertexFromOriginAlongNormal =
				vertex.dotProduct(plane.normal);
			var distanceOfVertexAbovePlane =
				distanceOfVertexFromOriginAlongNormal
				- plane.distanceFromOrigin;
			if (distanceOfVertexAbovePlane < 0)
			{
				collision.isActive = true;
				plane.pointClosestToOrigin(collision.pos);
				collision.colliders.length = 0;
				collision.colliders.add(hemispace);
				break;
			}
		}

		return collision;
	}

	public Collision collisionOfHemispaceAndSphere
	(
		Hemispace hemispace, Sphere sphere, Collision collision
	)
	{
		if (collision == null)
		{
			collision = Collision.create();
		}

		var plane = hemispace.plane;
		var distanceOfSphereCenterFromOriginAlongNormal =
			sphere.center.dotProduct(plane.normal);
		var distanceOfSphereCenterAbovePlane =
			distanceOfSphereCenterFromOriginAlongNormal
			- plane.distanceFromOrigin;
		if (distanceOfSphereCenterAbovePlane < sphere.radius)
		{
			collision.isActive = true;
			plane.pointClosestToOrigin(collision.pos);
			collision.colliders.length = 0;
			collision.colliders.add(hemispace);
		}
		return collision;
	}

	public Collision collisionOfMapLocatedAndBox
	(
		MapLocated mapLocated, Box box, Collision collision
	)
	{
		return this.collisionOfBoxAndMapLocated(box, mapLocated, collision);
	}

	public Collision collisionOfMapLocatedAndBoxRotated
	(
		MapLocated mapLocated, BoxRotated boxRotated,
		Collision collision, boolean shouldCalculateCollisionPos
	)
	{
		return this.collisionOfBoxRotatedAndMapLocated
		(
			boxRotated, mapLocated, collision, shouldCalculateCollisionPos
		);
	}

	public Collision collisionOfMapLocatedAndMapLocated
	(
		MapLocated mapLocated0, MapLocated mapLocated1, Collision collision
	)
	{
		return collision; // todo
	}

	public Collision collisionOfMapLocatedAndSphere
	(
		MapLocated mapLocated, Sphere sphere, Collision collision
	)
	{
		var doBoundsCollide =
			this.doBoxAndSphereCollide(mapLocated.box, sphere);

		if (doBoundsCollide == false)
		{
			return collision;
		}

		var map = mapLocated.map;
		var cell = map.cellCreate();
		var cellPosAbsolute = Coords.create();
		var cellPosInCells = Coords.create();
		var mapSizeInCells = map.sizeInCells;
		var mapCellSize = map.cellSize;
		var mapSizeHalf = map.sizeHalf;
		var mapPos = mapLocated.loc.pos;
		var cellAsBox = new Box( Coords.create(), map.cellSize );

		for (var y = 0; y < mapSizeInCells.y; y++)
		{
			cellPosInCells.y = y;
			cellPosAbsolute.y = (y * mapCellSize.y) + mapPos.y - mapSizeHalf.y;

			for (var x = 0; x < mapSizeInCells.x; x++)
			{
				cellPosInCells.x = x;
				cellPosAbsolute.x = (x * mapCellSize.x) + mapPos.x - mapSizeHalf.x;

				cell = map.cellAtPosInCells(cellPosInCells);

				if (cell.isBlocking)
				{
					cellAsBox.center.overwriteWith(cellPosAbsolute);
					var doCellAndSphereCollide =
						this.doBoxAndSphereCollide(cellAsBox, sphere);
					if (doCellAndSphereCollide)
					{
						collision.isActive = true;
						collision.pos.overwriteWith(cellAsBox.center);
						break;
					}
				}
			}
		}

		return collision;
	}

	public Collision collisionOfMeshAndBox(Mesh mesh, Box box, Collision collision)
	{
		return this.collisionOfBoxAndMesh(box, mesh, collision);
	}

	public Collision collisionOfMeshAndSphere
	(
		Mesh mesh, Sphere sphere, Collision collision
	)
	{
		// hack
		var meshBoundsAsBox = mesh.box();
		return this.collisionOfBoxAndSphere
		(
			meshBoundsAsBox, sphere, collision, true
		); // shouldCalculatePos
	}

	public Collision collisionOfShapeAndShapeGroupAll
	(
		ShapeBase shape, ShapeGroupAll shapeGroupAll, Collision collisionOut
	)
	{
		return this.collisionOfColliders(shape, shapeGroupAll.shapes[0], collisionOut);
	}

	public Collision collisionOfShapeAndShapeInverse
	(
		ShapeBase shape, ShapeInverse shapeInverse, Collision collisionOut
	)
	{
		return collisionOut; // todo
	}

	public Collision collisionOfShapeGroupAllAndShape
	(
		ShapeGroupAll shapeGroupAll, Object shape, Collision collisionOut
	)
	{
		return this.collisionOfShapeAndShapeGroupAll(shape, shapeGroupAll, collisionOut);
	}

	public Collision collisionOfShapeInverseAndShape
	(
		ShapeInverse shapeInverse, Object shape, Collision collisionOut
	)
	{
		return this.collisionOfShapeAndShapeInverse(shape, shapeInverse, collisionOut);
	}

	public Collision collisionOfSphereAndBox
	(
		Sphere sphere, Box box, Collision collision, boolean shouldCalculatePos
	)
	{
		return this.collisionOfBoxAndSphere(box, sphere, collision, shouldCalculatePos);
	}

	public Collision collisionOfSphereAndBoxRotated
	(
		Sphere sphere, BoxRotated boxRotated,
		Collision collision, boolean shouldCalculatePos
	)
	{
		return this.collisionOfBoxRotatedAndSphere(boxRotated, sphere, collision, shouldCalculatePos);
	}

	public Collision collisionOfSphereAndMapLocated
	(
		Sphere sphere, MapLocated mapLocated, Collision collision
	)
	{
		return this.collisionOfMapLocatedAndSphere(mapLocated, sphere, collision);
	}

	public Collision collisionOfSphereAndMesh
	(
		Sphere sphere, Mesh mesh, Collision collision
	)
	{
		return this.collisionOfMeshAndSphere(mesh, sphere, collision);
	}

	public Collision collisionOfSpheres
	(
		Sphere sphere0, Sphere sphere1, Collision collision
	)
	{
		var sphere0Center = sphere0.center;
		var sphere1Center = sphere1.center;

		var sphere0Radius = sphere0.radius;
		var sphere1Radius = sphere1.radius;

		var displacementFromSphere0CenterTo1 = this._displacement.overwriteWith
		(
			sphere1Center
		).subtract
		(
			sphere0Center
		);

		var distanceBetweenCenters =
			displacementFromSphere0CenterTo1.magnitude();

		if (distanceBetweenCenters == 0)
		{
			collision.pos.overwriteWith(sphere0Center);
		}
		else
		{
			var distanceToRadicalCenter =
			(
				distanceBetweenCenters * distanceBetweenCenters
				+ sphere0Radius * sphere0Radius
				- sphere1Radius * sphere1Radius
			)
			/ (2 * distanceBetweenCenters);

			var directionFromSphere0CenterTo1 =
				displacementFromSphere0CenterTo1.divideScalar(distanceBetweenCenters);
			var displacementFromSphereCenter0ToRadicalCenter =
				directionFromSphere0CenterTo1.multiplyScalar(distanceToRadicalCenter);

			collision.pos.overwriteWith
			(
				displacementFromSphereCenter0ToRadicalCenter
			).add
			(
				sphere0Center
			);
		}

		return collision;
	}

	// doXAndYCollide

	public boolean doBoxAndBoxCollide(Box box0, Box box1)
	{
		var returnValue = box0.overlapsWith(box1);
		return returnValue;
	}

	public boolean doBoxAndBoxRotatedCollide(Box box, BoxRotated boxRotated)
	{
		// todo
		var boxRotatedAsSphere = boxRotated.sphereSwept();
		return this.doBoxAndSphereCollide(box, boxRotatedAsSphere);
	}

	public boolean doBoxAndCylinderCollide(Box box, Cylinder cylinder)
	{
		var returnValue = false;

		var displacementBetweenCenters = this._displacement.overwriteWith
		(
			box.center
		).subtract
		(
			cylinder.center
		);

		if (displacementBetweenCenters.z < box.sizeHalf().z + cylinder.lengthHalf)
		{
			displacementBetweenCenters.clearZ();

			var direction = displacementBetweenCenters.normalize();

			var pointOnCylinderClosestToBoxCenter = direction.multiplyScalar
			(
				cylinder.radius
			).add
			(
				cylinder.center
			);

			pointOnCylinderClosestToBoxCenter.z = box.center.z;

			var isPointOnCylinderWithinBox = box.containsPoint
			(
				pointOnCylinderClosestToBoxCenter
			);

			returnValue = isPointOnCylinderWithinBox;
		}

		return returnValue;
	}

	public boolean doBoxAndHemispaceCollide(Box box, Hemispace hemispace)
	{
		var returnValue = false;

		var vertices = Mesh.fromBox(box).vertices();
		for (var i = 0; i < vertices.length; i++)
		{
			var vertex = vertices[i];
			if (hemispace.containsPoint(vertex))
			{
				returnValue = true;
				break;
			}
		}
		return returnValue;
	}

	public boolean doBoxAndMapLocatedCollide(Box box, MapLocated mapLocated)
	{
		// todo
		return this.doBoxAndBoxCollide(box, mapLocated.box);
	}

	public boolean doBoxAndMeshCollide(Box box, Mesh mesh)
	{
		// todo
		return this.doBoxAndBoxCollide(box, mesh.box() );
	}

	public boolean doBoxAndShapeInverseCollide
	(
		Box box, ShapeInverse shapeInverse
	)
	{
		return this.doShapeInverseAndShapeCollide(shapeInverse, box);
	}

	public boolean doBoxAndShapeGroupAllCollide
	(
		Box box, ShapeGroupAll shapeGroupAll
	)
	{
		return this.doShapeGroupAllAndShapeCollide(shapeGroupAll, box);
	}

	public boolean doBoxAndSphereCollide(Box box, Sphere sphere)
	{
		return this.collisionOfBoxAndSphere
		(
			box, sphere, this._collision, false
		).isActive;
	}

	public boolean doBoxRotatedAndBoxCollide(BoxRotated boxRotated, Box box)
	{
		return this.doBoxAndBoxRotatedCollide(box, boxRotated);
	}

	public boolean doBoxRotatedAndBoxRotatedCollide
	(
		BoxRotated boxRotated0, BoxRotated boxRotated1
	)
	{
		return false; // todo
	}

	public boolean doBoxRotatedAndMapLocatedCollide
	(
		BoxRotated boxRotated, MapLocated mapLocated
	)
	{
		// todo
		return this.doBoxAndBoxCollide(boxRotated.box, mapLocated.box);
	}

	public boolean doBoxRotatedAndSphereCollide
	(
		BoxRotated boxRotated, Sphere sphere
	)
	{
		var box = boxRotated.box;
		var center = box.center;
		var sphereCenter = sphere.center;
		var sphereCenterToRestore = this._pos.overwriteWith(sphereCenter);
		sphereCenter.subtract(center);
		var polar = this._polar;
		polar.azimuthInTurns = boxRotated.angleInTurns;
		polar.radius = 1;
		var rectangleAxisX = polar.toCoords(Coords.create());
		polar.azimuthInTurns += .25;
		var rectangleAxisY = polar.toCoords(Coords.create());
		var x = sphereCenter.dotProduct(rectangleAxisX);
		var y = sphereCenter.dotProduct(rectangleAxisY);
		sphereCenter.x = x;
		sphereCenter.y = y;
		sphereCenter.add(box.center);
		var returnValue = this.doBoxAndSphereCollide(box, sphere);
		sphereCenter.overwriteWith(sphereCenterToRestore);
		return returnValue;
	}

	public boolean doCylinderAndCylinderCollide
	(
		Cylinder cylinder0, Cylinder cylinder1
	)
	{
		var returnValue = false;

		var displacement = this._displacement.overwriteWith
		(
			cylinder1.center
		).subtract
		(
			cylinder0.center
		).clearZ();

		var distance = displacement.magnitude();
		var sumOfRadii = cylinder0.radius + cylinder1.radius;
		var doRadiiOverlap = (distance < sumOfRadii);
		if (doRadiiOverlap)
		{
			var doLengthsOverlap = false; // todo
			/*
			(
				(
					cylinder0.zMin > cylinder1.zMin
					&& cylinder0.zMin < cylinder1.zMax
				)
				||
				(
					cylinder0.zMax > cylinder1.zMin
					&& cylinder0.zMax < cylinder1.zMax
				)
				||
				(
					cylinder1.zMin > cylinder0.zMin
					&& cylinder1.zMin < cylinder0.zMax
				)
				||
				(
					cylinder1.zMax > cylinder0.zMin
					&& cylinder1.zMax < cylinder0.zMax
				)
			);
			*/

			if (doLengthsOverlap)
			{
				returnValue = true;
			}
		}

		return returnValue;
	}

	public boolean doEdgeAndFaceCollide(Edge edge, Face face, Collision collision)
	{
		return (this.collisionOfEdgeAndFace(edge, face, collision).isActive);
	}

	public boolean doEdgeAndHemispaceCollide(Edge edge, Hemispace hemispace)
	{
		var vertices = edge.vertices;
		var returnValue =
		(
			hemispace.containsPoint(vertices[0])
			|| hemispace.containsPoint(vertices[1])
		);
		return returnValue;
	}

	public boolean doEdgeAndMeshCollide(Edge edge, Mesh mesh)
	{
		var returnValue = false;

		var edgeDirection = edge.direction();
		var meshFaces = mesh.faces();
		for (var f = 0; f < meshFaces.length; f++)
		{
			var face = meshFaces[f];
			var facePlane = face.plane();
			var faceNormal = facePlane.normal;
			var faceDotEdge = faceNormal.dotProduct(edgeDirection);

			if (faceDotEdge < 0)
			{
				returnValue =
					this.doEdgeAndFaceCollide(edge, face, this._collision);
				if (returnValue == true)
				{
					break;
				}
			}
		}

		return returnValue;
	}

	public boolean doEdgeAndPlaneCollide(Edge edge, Plane plane)
	{
		return
		(
			this.collisionOfEdgeAndPlane
			(
				edge, plane, this._collision.clear()
			) != null
		);
	}

	public boolean doHemispaceAndBoxCollide(Hemispace hemispace, Box box)
	{
		var collision = this.collisionOfHemispaceAndBox
		(
			hemispace, box, this._collision.clear()
		);

		return collision.isActive;
	}

	public boolean doHemispaceAndSphereCollide
	(
		Hemispace hemispace, Sphere sphere
	)
	{
		var collision = this.collisionOfHemispaceAndSphere(hemispace, sphere, this._collision.clear());

		return collision.isActive;
	}

	public boolean doMeshAndBoxCollide(Mesh mesh, Box box)
	{
		return this.doBoxAndMeshCollide(box, mesh);
	}

	public boolean doMeshAndMeshCollide(Mesh mesh0, Mesh mesh1)
	{
		var returnValue = true;

		// hack - Meshes are assumed to be convex.

		var meshVertices = new Coords[][]
		{
			mesh0.vertices(), mesh1.vertices()
		};

		var meshFaces = new Face[][]
		{
			mesh0.faces(), mesh1.faces()
		};

		for (var m = 0; m < 2; m++)
		{
			var meshThisFaces = meshFaces[m];
			var meshThisVertices = meshVertices[m];
			var meshOtherVertices = meshVertices[1 - m];

			for (var f = 0; f < meshThisFaces.length; f++)
			{
				var face = meshThisFaces[f];
				var faceNormal = face.plane().normal;

				var vertexThis = meshThisVertices[0];
				var vertexThisProjectedMin =
					vertexThis.dotProduct(faceNormal);
				var vertexThisProjectedMax = vertexThisProjectedMin;
				for (var v = 1; v < meshThisVertices.length; v++)
				{
					vertexThis = meshThisVertices[v];
					var vertexThisProjected = vertexThis.dotProduct(faceNormal);
					if (vertexThisProjected < vertexThisProjectedMin)
					{
						vertexThisProjectedMin = vertexThisProjected;
					}

					if (vertexThisProjected > vertexThisProjectedMax)
					{
						vertexThisProjectedMax = vertexThisProjected;
					}
				}

				var vertexOther = meshOtherVertices[0];
				var vertexOtherProjectedMin =
					vertexOther.dotProduct(faceNormal);
				var vertexOtherProjectedMax = vertexOtherProjectedMin;
				for (var v = 1; v < meshOtherVertices.length; v++)
				{
					vertexOther = meshOtherVertices[v];
					var vertexOtherProjected = vertexOther.dotProduct(faceNormal);
					if (vertexOtherProjected < vertexOtherProjectedMin)
					{
						vertexOtherProjectedMin = vertexOtherProjected;
					}

					if (vertexOtherProjected > vertexOtherProjectedMax)
					{
						vertexOtherProjectedMax = vertexOtherProjected;
					}
				}

				var doProjectionsOverlap =
				(
					vertexThisProjectedMax > vertexOtherProjectedMin
					&& vertexOtherProjectedMax > vertexThisProjectedMin
				);

				if (doProjectionsOverlap == false)
				{
					returnValue = false;
					break;
				}
			}
		}

		return returnValue;
	}

	public boolean doMeshAndShapeInverseCollide
	(
		Mesh mesh, ShapeInverse inverse
	)
	{
		return this.doShapeInverseAndShapeCollide(inverse, mesh);
	}

	public boolean doMapLocatedAndBoxCollide(MapLocated mapLocated, Box box)
	{
		return this.doBoxAndMapLocatedCollide(box, mapLocated);
	}

	public boolean doMapLocatedAndBoxRotatedCollide
	(
		MapLocated mapLocated, BoxRotated boxRotated
	)
	{
		return this.doBoxRotatedAndMapLocatedCollide(boxRotated, mapLocated);
	}

	public boolean doMapLocatedAndMapLocatedCollide
	(
		MapLocated mapLocated0, MapLocated mapLocated1
	)
	{
		var returnValue = false;

		var doBoundsCollide =
			this.doBoxAndBoxCollide(mapLocated0.box, mapLocated1.box);

		if (doBoundsCollide == false)
		{
			return false;
		}

		var map0 = mapLocated0.map;
		var map1 = mapLocated1.map;

		var cell0 = map0.cellCreate();
		var cell1 = map1.cellCreate();

		var cell0PosAbsolute = Coords.create();

		var cell0PosInCells = Coords.create();
		var cell1PosInCells = Coords.create();

		var cell1PosInCellsMin = Coords.create();
		var cell1PosInCellsMax = Coords.create();

		var map0SizeInCells = map0.sizeInCells;

		var map1SizeInCellsMinusOnes = map1.sizeInCellsMinusOnes;

		var map0CellSize = map0.cellSize;
		var map1CellSize = map1.cellSize;

		var map0Pos = mapLocated0.loc.pos;
		var map1Pos = mapLocated1.loc.pos;

		for (var y0 = 0; y0 < map0SizeInCells.y; y0++)
		{
			cell0PosInCells.y = y0;
			cell0PosAbsolute.y = map0Pos.y + (y0 * map0CellSize.y);

			for (var x0 = 0; x0 < map0SizeInCells.x; x0++)
			{
				cell0PosInCells.x = x0;
				cell0PosAbsolute.x = map0Pos.x + (x0 * map0CellSize.x);

				cell0 = map0.cellAtPosInCells(cell0PosInCells);

				if (cell0.isBlocking)
				{
					cell1PosInCellsMin.overwriteWith
					(
						cell0PosAbsolute
					).subtract
					(
						map1Pos
					).divide
					(
						map1CellSize
					).floor();

					cell1PosInCellsMax.overwriteWith
					(
						cell0PosAbsolute
					).subtract
					(
						map1Pos
					).add
					(
						map0CellSize
					).divide
					(
						map1CellSize
					).floor();

					for (var y1 = cell1PosInCellsMin.y; y1 <= cell1PosInCellsMax.y; y1++)
					{
						cell1PosInCells.y = y1;

						for (var x1 = cell1PosInCellsMin.x; x1 < cell1PosInCellsMax.x; x1++)
						{
							cell1PosInCells.x = x1;

							var isCell1PosInBox = cell1PosInCells.isInRangeMinMax
							(
								Coords.Instances().Zeroes, map1SizeInCellsMinusOnes
							);
							if (isCell1PosInBox)
							{
								cell1 = map1.cellAtPosInCells(cell1PosInCells);

								if (cell1.isBlocking)
								{
									returnValue = true;

									y1 = cell1PosInCellsMax.y;
									x0 = map0SizeInCells.x;
									y0 = map0SizeInCells.y;
									break;
								}
							}
						}
					}
				}
			}
		}

		return returnValue;
	}

	public boolean doMapLocatedAndSphereCollide
	(
		MapLocated mapLocated, Sphere sphere
	)
	{
		var returnValue = false;

		var doBoundsCollide =
			this.doBoxAndSphereCollide(mapLocated.box, sphere);

		if (doBoundsCollide == false)
		{
			return false;
		}

		var map = mapLocated.map;
		var cell = map.cellCreate();
		var cellPosAbsolute = Coords.create();
		var cellPosInCells = Coords.create();
		var mapSizeInCells = map.sizeInCells;
		var mapCellSize = map.cellSize;
		var mapSizeHalf = map.sizeHalf;
		var mapPos = mapLocated.loc.pos;
		var cellAsBox = new Box( Coords.create(), map.cellSize );

		for (var y = 0; y < mapSizeInCells.y; y++)
		{
			cellPosInCells.y = y;
			cellPosAbsolute.y = (y * mapCellSize.y) + mapPos.y - mapSizeHalf.y;

			for (var x = 0; x < mapSizeInCells.x; x++)
			{
				cellPosInCells.x = x;
				cellPosAbsolute.x = (x * mapCellSize.x) + mapPos.x - mapSizeHalf.x;

				cell = map.cellAtPosInCells(cellPosInCells);

				if (cell.isBlocking)
				{
					cellAsBox.center.overwriteWith(cellPosAbsolute);
					var doCellAndSphereCollide =
						this.doBoxAndSphereCollide(cellAsBox, sphere);
					if (doCellAndSphereCollide)
					{
						returnValue = true;
						break;
					}
				}
			}
		}

		return returnValue;
	}

	public boolean doMeshAndSphereCollide(Mesh mesh, Sphere sphere)
	{
		var returnValue = true;

		// hack - Mesh is assumed to be convex.

		var meshFaces = mesh.faces();
		var hemispace = new Hemispace(null);

		for (var f = 0; f < meshFaces.length; f++)
		{
			var face = meshFaces[f];
			hemispace.plane = face.plane();
			var doHemispaceAndSphereCollide = this.doHemispaceAndSphereCollide
			(
				hemispace,
				sphere
			);
			if (doHemispaceAndSphereCollide == false)
			{
				returnValue = false;
				break;
			}
		}

		return returnValue;
	}

	public boolean doSphereAndBoxCollide(Sphere sphere, Box box)
	{
		return this.doBoxAndSphereCollide(box, sphere);
	}

	public boolean doSphereAndMapLocatedCollide
	(
		Sphere sphere, MapLocated mapLocated
	)
	{
		return this.doMapLocatedAndSphereCollide(mapLocated, sphere);
	}

	public boolean doSphereAndMeshCollide(Sphere sphere, Mesh mesh)
	{
		return this.doMeshAndSphereCollide(mesh, sphere);
	}

	public boolean doSphereAndBoxRotatedCollide
	(
		Sphere sphere, BoxRotated boxRotated
	)
	{
		return this.doBoxRotatedAndSphereCollide(boxRotated, sphere);
	}

	public boolean doSphereAndShapeContainerCollide
	(
		Sphere sphere, ShapeContainer shapeContainer
	)
	{
		return this.doShapeContainerAndShapeCollide(shapeContainer, sphere);
	}

	public boolean doSphereAndShapeGroupAllCollide
	(
		Sphere sphere, ShapeGroupAll shapeGroupAll
	)
	{
		return this.doShapeGroupAllAndShapeCollide(shapeGroupAll, sphere);
	}

	public boolean doSphereAndShapeGroupAnyCollide
	(
		Sphere sphere, ShapeGroupAny shapeGroupAny
	)
	{
		return this.doShapeGroupAnyAndShapeCollide(shapeGroupAny, sphere);
	}

	public boolean doSphereAndShapeInverseCollide
	(
		Sphere sphere, ShapeInverse shapeInverse
	)
	{
		return this.doShapeInverseAndShapeCollide(shapeInverse, sphere);
	}

	public boolean doSphereAndSphereCollide
	(
		Sphere sphere0, Sphere sphere1
	)
	{
		var displacement = this._displacement.overwriteWith
		(
			sphere1.center
		).subtract
		(
			sphere0.center
		);
		var distance = displacement.magnitude();
		var sumOfRadii = sphere0.radius + sphere1.radius;
		var returnValue = (distance < sumOfRadii);

		return returnValue;
	}

	// boolean combinations

	public boolean doShapeGroupAllAndBoxCollide
	(
		ShapeGroupAll groupAll, ShapeBase shapeOther
	)
	{
		return this.doShapeGroupAllAndShapeCollide(groupAll, shapeOther);
	}

	public boolean doShapeGroupAllAndShapeCollide
	(
		ShapeGroupAll groupAll, ShapeBase shapeOther
	)
	{
		var returnValue = true;

		var shapesThis = groupAll.shapes;
		for (var i = 0; i < shapesThis.length; i++)
		{
			var shapeThis = shapesThis[i];
			var doShapesCollide = this.doCollidersCollide(shapeThis, shapeOther);
			if (doShapesCollide == false)
			{
				returnValue = false;
				break;
			}
		}

		return returnValue;
	}

	public boolean doShapeGroupAllAndMeshCollide
	(
		ShapeGroupAll groupAll, Mesh mesh
	)
	{
		return this.doShapeGroupAllAndShapeCollide(groupAll, mesh);
	}

	public boolean doShapeGroupAnyAndBoxCollide
	(
		ShapeGroupAny groupAny, Box box
	)
	{
		return this.doShapeGroupAnyAndShapeCollide(groupAny, box);
	}

	public boolean doShapeGroupAnyAndShapeCollide
	(
		ShapeGroupAny groupAny, ShapeBase shapeOther
	)
	{
		var returnValue = false;

		var shapesThis = groupAny.shapes;
		for (var i = 0; i < shapesThis.length; i++)
		{
			var shapeThis = shapesThis[i];
			var doShapesCollide = this.doCollidersCollide(shapeThis, shapeOther);
			if (doShapesCollide)
			{
				returnValue = true;
				break;
			}
		}

		return returnValue;
	}

	public boolean doShapeContainerAndShapeCollide
	(
		ShapeContainer container, ShapeBase shapeOther
	)
	{
		return this.doesColliderContainOther(container.shape, shapeOther);
	}

	public boolean doShapeContainerAndBoxCollide
	(
		ShapeContainer container, Box box
	)
	{
		return this.doShapeContainerAndShapeCollide(container, box);
	}

	public boolean doShapeInverseAndMeshCollide
	(
		ShapeInverse inverse, Mesh mesh
	)
	{
		return this.doShapeInverseAndShapeCollide(inverse, mesh);
	}

	public boolean doShapeInverseAndShapeCollide
	(
		ShapeInverse inverse, ShapeBase shapeOther
	)
	{
		return (this.doCollidersCollide(inverse.shape, shapeOther) == false);
	}

	public boolean doShapeGroupAllAndSphereCollide
	(
		ShapeGroupAll group, ShapeBase shape
	)
	{
		return this.doShapeGroupAllAndShapeCollide(group, shape);
	}

	public boolean doBoxAndShapeGroupAnyCollide(Box box, ShapeGroupAny group)
	{
		return this.doShapeGroupAnyAndShapeCollide(group, box);
	}

	public boolean doShapeContainerAndSphereCollide
	(
		ShapeContainer container, Sphere sphere
	)
	{
		return this.doShapeContainerAndShapeCollide(container, sphere);
	}

	public boolean doShapeGroupAnyAndSphereCollide
	(
		ShapeGroupAny group, Sphere sphere
	)
	{
		return this.doShapeGroupAnyAndShapeCollide(group, sphere);
	}

	public boolean doShapeInverseAndBoxCollide
	(
		ShapeInverse inverse, Box box
	)
	{
		return this.doShapeInverseAndShapeCollide(inverse, box);
	}

	public boolean doShapeInverseAndSphereCollide
	(
		ShapeInverse inverse, Sphere sphere
	)
	{
		return this.doShapeInverseAndShapeCollide(inverse, sphere);
	}

	// contains

	public boolean doesBoxContainBox(Box box0, Box box1)
	{
		return box0.containsOther(box1);
	}

	public boolean doesBoxContainHemispace(Box box, Hemispace hemispace)
	{
		return false;
	}

	public boolean doesBoxContainSphere(Box box, Sphere sphere)
	{
		var boxForSphere = new Box
		(
			sphere.center, new Coords(1, 1, 1).multiplyScalar(sphere.radius * 2)
		);

		var returnValue = box.containsOther(boxForSphere);

		return returnValue;
	}

	public boolean doesHemispaceContainBox(Hemispace hemispace, Box box)
	{
		var returnValue = true;

		var vertices = Mesh.fromBox(box).vertices();
		for (var i = 0; i < vertices.length; i++)
		{
			var vertex = vertices[i];
			if (hemispace.containsPoint(vertex) == false)
			{
				returnValue = false;
				break;
			}
		}
		return returnValue;
	}

	public boolean doesHemispaceContainSphere
	(
		Hemispace hemispace, Sphere sphere
	)
	{
		var plane = hemispace.plane;
		var distanceOfSphereCenterAbovePlane =
			sphere.center.dotProduct(plane.normal)
			- plane.distanceFromOrigin;
		var returnValue = (distanceOfSphereCenterAbovePlane >= sphere.radius);
		return returnValue;
	}

	public boolean doesSphereContainBox(Sphere sphere, Box box)
	{
		var sphereCircumscribingBox = new Sphere(box.center, box.max().magnitude());
		var returnValue = sphere.containsOther(sphereCircumscribingBox);
		return returnValue;
	}

	public boolean doesSphereContainHemispace
	(
		Sphere sphere, Hemispace hemispace
	)
	{
		return false;
	}

	public boolean doesSphereContainSphere(Sphere sphere0, Sphere sphere1)
	{
		return sphere0.containsOther(sphere1);
	}
}

