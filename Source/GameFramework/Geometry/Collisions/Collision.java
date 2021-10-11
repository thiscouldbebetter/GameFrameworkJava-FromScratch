
package GameFramework.Geometry.Collisions;

import java.util.*;

import GameFramework.Geometry.*;

public class Collision //
{
	public Coords pos;
	public double distanceToCollision;
	public List<ShapeBase> colliders;
	public Map<String,ShapeBase> collidersByName;
	public Entity entitiesColliding[];

	public Coords normals[];
	public boolean isActive;

	public Collision;
	(
		Coords pos,
		double distanceToCollision,
		ShapeBase[] colliders,
		Entity[] entitiesColliding
	)
	{
		this.pos = (pos != null ? pos : Coords.create());
		this.distanceToCollision = distanceToCollision;
		this.colliders =
		(
			colliders != null ? colliders : new ArrayList<ShapeBase>()
		);
		this.entitiesColliding =
		(
			entitiesColliding != null
			? entitiesColliding
			: new ArrayList<Entity>()
		);

		this.collidersByName = new Map<String,ShapeBase>();
		this.normals = new Coords[] { Coords.create(), Coords.create() };

		this.isActive = false;
	}

	public static Collision create()
	{
		return new Collision
		(
			Coords.create(), 0, new ArrayList<ShapeBase>(), new ArrayList<Entity>()
		);
	}

	public static Collision fromPosAndDistance(Coords pos, double distance)
	{
		return new Collision(pos, distance, null, null);
	}

	public Collision clear()
	{
		this.isActive = false;
		ArrayHelper.clear(this.entitiesColliding);
		ArrayHelper.clear(this.colliders);
		this.normals.forEach(x -> x.clear());
		this.collidersByName.clear();
		this.distanceToCollision = null;
		return this;
	}

	public boolean equals(Collision other)
	{
		var returnValue =
		(
			this.isActive == other.isActive
			&&
			(
				this.isActive == false
				||
				(
					this.pos.equals(other.pos)
					&& this.distanceToCollision == other.distanceToCollision
					&& ArrayHelper.equals(this.colliders, other.colliders)
				)
			)
		);

		return returnValue;
	}

	public Collision clone()
	{
		var returnValue = new Collision
		(
			this.pos.clone(),
			this.distanceToCollision,
			this.colliders,
			this.entitiesColliding
		);

		// hack
		returnValue.collidersByName = this.collidersByName;
		returnValue.normals = ArrayHelper.clone(this.normals);
		returnValue.isActive = this.isActive;

		return returnValue;
	}
}
