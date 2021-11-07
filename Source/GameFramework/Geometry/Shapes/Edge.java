
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class Edge implements ShapeBase<Edge>
{
	public Coords[] vertices;

	public Box _box;
	public Coords _direction;
	public Coords _displacement;
	public Coords _transverse;

	public Edge(Coords[] vertices)
	{
		this.vertices =
		(
			vertices != null
			? vertices
			: new Coords[] { Coords.create(), Coords.create() }
		);

		this._direction = Coords.create();
		this._displacement = Coords.create();
		this._transverse = Coords.create();
	}

	public static Edge create()
	{
		return new Edge(null);
	}

	public static Edge fromVertex0And1(Coords vertex0, Coords vertex1)
	{
		return new Edge( new Coords[] { vertex0, vertex1 } );
	}

	public Coords direction()
	{
		return this._direction.overwriteWith(this.displacement()).normalize();
	}

	public boolean equals(Edge other)
	{
		return ArrayHelper.equals(this.vertices, other.vertices);
	}

	public Coords displacement()
	{
		return this._displacement.overwriteWith
		(
			this.vertices[1]
		).subtract
		(
			this.vertices[0]
		);
	}

	public double length()
	{
		return this.displacement().magnitude();
	}

	public Edge projectOntoOther(Edge other)
	{
		var otherVertices = other.vertices;
		var otherVertex0 = otherVertices[0];
		var otherDirection = other.direction();
		var otherTransverse = other.transverse(Coords.Instances().ZeroZeroOne);

		for (var i = 0; i < this.vertices.length; i++)
		{
			var vertex = this.vertices[i];
			vertex.subtract(otherVertex0);
			vertex.overwriteWithDimensions
			(
				vertex.dotProduct(otherDirection),
				vertex.dotProduct(otherTransverse),
				0
			);
		}

		return this;
	}

	public Coords transverse(Coords faceNormal)
	{
		return this._transverse.overwriteWith
		(
			this.direction()
		).crossProduct
		(
			faceNormal
		);
	}

	// String

	public String toString()
	{
		return this.vertices.toString();
	}

	// Cloneable.

	public Edge clone()
	{
		return new Edge(ArrayHelper.clone(this.vertices));
	}

	public Edge overwriteWith(Edge other)
	{
		ArrayHelper.overwriteWith(this.vertices, other.vertices);
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		throw new Exception("Not implemented!");
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		throw new Exception("Not implemented!");
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		throw new Exception("Not implemented!");
	}

	public Box toBox(Box boxOut)
	{
		return boxOut.ofPoints(this.vertices);
	}

	// Transformable.

	public Edge transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}
}
