
package GameFramework.Geometry.Collisions;

import java.util.*;
import java.util.stream.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class Collision implements Comparable<Collision>
{
	public Coords pos;
	public Double distanceToCollision;
	public List<ShapeBase> colliders;
	public Map<String,ShapeBase> collidersByName;
	public List<Entity> entitiesColliding;

	public Coords[] normals;
	public boolean isActive;

	public Collision
	(
		Coords pos,
		Double distanceToCollision,
		List<ShapeBase> colliders,
		List<Entity> entitiesColliding
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

		this.collidersByName = new HashMap<String,ShapeBase>();
		this.normals = new Coords[] { Coords.create(), Coords.create() };

		this.isActive = false;
	}

	public static Collision create()
	{
		return new Collision
		(
			Coords.create(), null, new ArrayList<ShapeBase>(), new ArrayList<Entity>()
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
		Arrays.asList(this.normals).stream().forEach(x -> x.clear());
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

	// Comparable.

	public int compareTo(Collision other)
	{
		var difference =
			(this.distanceToCollision - other.distanceToCollision);
		var returnValue =
			(difference == 0 ? 0 : (int)( difference / Math.abs(difference) ) );
		return returnValue;
	}
}
