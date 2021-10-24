
package GameFramework.Geometry.Shapes;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class Face implements ShapeBase
{
	public Coords[] vertices;

	private Box _box;
	private List<Edge> _edges;
	private Plane _plane;

	public Face(Coords[] vertices)
	{
		this.vertices = vertices;
	}

	public Box box()
	{
		if (this._box == null)
		{
			this._box = new Box(Coords.create(), Coords.create());
		}
		this._box.ofPoints(this.vertices);
		return this._box;
	}

	public boolean containsPoint(Coords pointToCheck)
	{
		var face = this;

		var faceNormal = face.plane().normal;

		var displacementFromVertex0ToCollision = Coords.create();

		var isPosWithinAllEdgesOfFaceSoFar = true;

		var edges = face.edges();
		for (var e = 0; e < edges.length; e++)
		{
			var edgeFromFace = edges[e];
			var edgeFromFaceVertex0 = edgeFromFace.vertices[0];

			displacementFromVertex0ToCollision.overwriteWith
			(
				pointToCheck
			).subtract
			(
				edgeFromFaceVertex0
			);

			var edgeFromFaceTransverse =
				edgeFromFace.transverse(faceNormal);

			var displacementProjectedAlongEdgeTransverse =
				displacementFromVertex0ToCollision.dotProduct
				(
					edgeFromFaceTransverse
				);

			if (displacementProjectedAlongEdgeTransverse > 0)
			{
				isPosWithinAllEdgesOfFaceSoFar = false;
				break;
			}
		}

		return isPosWithinAllEdgesOfFaceSoFar;
	}

	public List<Edge> edges()
	{
		if (this._edges == null)
		{
			this._edges = new ArrayList<Edge>();

			for (var v = 0; v < this.vertices.length; v++)
			{
				var vNext = NumberHelper.wrapToRangeMinMax
				(
					v + 1, 0, this.vertices.length
				);
				var vertex = this.vertices[v];
				var vertexNext = this.vertices[vNext];

				var edge = Edge.fromVertex0And1(vertex, vertexNext);

				this._edges.add(edge);
			}
		}

		return this._edges;
	}

	public boolean equals(Face other)
	{
		return ArrayHelper.equals(this.vertices, other.vertices);
	}

	public Plane plane()
	{
		if (this._plane == null)
		{
			this._plane = new Plane(Coords.create(), 0);
		}

		this._plane.fromPoints
		(
			this.vertices[0],
			this.vertices[1],
			this.vertices[2]
		);

		return this._plane;
	}

	// Cloneable.

	public ShapeBase clone()
	{
		return new Face(ArrayHelper.clone(this.vertices));
	}

	public ShapeBase overwriteWith(ShapeBase otherAsShapeBase)
	{
		var other = (Face)otherAsShapeBase
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

	public Coords surfacePointNearPos
	(
		Coords posToCheck, Coords surfacePointOut
	)
	{
		throw new Exception("Not implemented!");
	}

	public Box toBox(Box boxOut)
	{
		return boxOut.ofPoints(this.vertices);
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		Transforms.applyTransformToCoordsMObject(transformToApply, this.vertices);
		return this;
	}
}
