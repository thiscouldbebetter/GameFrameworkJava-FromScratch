
package GameFramework.Geometry.Shapes.Meshes;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;

public class Mesh implements ShapeBase
{
	public Coords center;
	public Coords[] vertexOffsets[];
	public FaceBuilder[] faceBuilders;

	private Box _box;
	private Face[] _faces;
	private Coords[] _vertices;

	public Mesh
	(
		Coords center,
		Coords[] vertexOffsets,
		FaceBuilder[] faceBuilders
	)
	{
		this.center = center;
		this.vertexOffsets = vertexOffsets;
		this.faceBuilders = faceBuilders;
	}

	// static methods

	public static Mesh boxOfSize(Coords center, Coords size)
	{
		var box = new Box(center, size);
		var returnValue = Mesh.fromBox(box);
		return returnValue;
	}

	public static Mesh cubeUnit(Coords center)
	{
		if (center == null)
		{
			center = Coords.create();
		}
		var size = new Coords(2, 2, 2);
		var returnValue = Mesh.boxOfSize(center, size);
		return returnValue;
	}

	public static Mesh fromBox(Box box)
	{
		var sizeHalf = box.sizeHalf();
		var min = new Coords(-sizeHalf.x, -sizeHalf.y, -sizeHalf.z);
		var max = new Coords(sizeHalf.x, sizeHalf.y, sizeHalf.z);

		var vertexOffsets = new Coords[]
		{
			// top
			new Coords(min.x, min.y, min.z),
			new Coords(max.x, min.y, min.z),
			new Coords(max.x, max.y, min.z),
			new Coords(min.x, max.y, min.z),

			// bottom
			new Coords(min.x, min.y, max.z),
			new Coords(max.x, min.y, max.z),
			new Coords(max.x, max.y, max.z),
			new Coords(min.x, max.y, max.z),
		};

		var faceBuilders = new FaceBuilder[]
		{
			new FaceBuilder(new int[] {0, 1, 5, 4}), // north
			new FaceBuilder(new int[] {1, 2, 6, 5}), // east

			new FaceBuilder(new int[] {2, 3, 7, 6}), // south
			new FaceBuilder(new int[] {3, 0, 4, 7}), // west

			new FaceBuilder(new int[] {0, 3, 2, 1}), // top
			new FaceBuilder(new int[] {4, 5, 6, 7}), // bottom
		};

		var returnValue = new Mesh
		(
			box.center, vertexOffsets, faceBuilders
		);

		return returnValue;
	}

	public static Mesh fromFace(Coords center, Face faceToExtrude, double thickness)
	{
		var faceVertices = faceToExtrude.vertices;
		var doubleOfFaceVertices = faceVertices.length;
		var thicknessHalf = thickness / 2;

		var meshVertices = new ArrayList<Coords>();
		var faceBuilders = new ArrayList<FaceBuilder>();

		for (var z = 0; z < 2; z++)
		{
			var offsetForExtrusion = new Coords
			(
				0, 0, (z == 0 ? -1 : 1)
			).multiplyScalar
			(
				thicknessHalf
			);

			var vertexIndicesTopOrBottom = new List<Integer>();

			for (var v = 0; v < doubleOfFaceVertices; v++)
			{
				var vertexIndex = meshVertices.length;

				if (z == 0)
				{
					var vertexIndexNext = doubleHelper.wrapToRangeMinMax
					(
						vertexIndex + 1,
						0, doubleOfFaceVertices
					);

					var faceBuilderSide = new FaceBuilder
					(
						new int[]
						{
							vertexIndex,
							vertexIndexNext,
							vertexIndexNext + doubleOfFaceVertices,
							vertexIndex + doubleOfFaceVertices
						}
					);
					faceBuilders.push(faceBuilderSide);

					vertexIndicesTopOrBottom.push(vertexIndex);
				}
				else
				{
					vertexIndicesTopOrBottom.splice(0, 0, vertexIndex);
				}

				var vertex = faceVertices[v].clone().add
				(
					offsetForExtrusion
				);

				meshVertices.push(vertex);
			}

			var faceBuilderTopOrBottom = new FaceBuilder
			(
				vertexIndicesTopOrBottom
			);
			faceBuilders.push(faceBuilderTopOrBottom);
		}

		var returnValue = new Mesh
		(
			center,
			meshVertices, // vertexOffsets
			faceBuilders
		);

		return returnValue;
	}

	// instance methods

	public Box box()
	{
		if (this._box == null)
		{
			this._box = new Box(Coords.create(), Coords.create());
		}
		this._box.ofPoints(this.vertices());
		return this._box;
	}

	public Edge[] edges()
	{
		return null; // todo
	}

	public Face[] faces()
	{
		var vertices = this.vertices();

		if (this._faces == null)
		{
			this._faces = new Face[this.faceBuilders.length];

			for (var f = 0; f < this.faceBuilders.length; f++)
			{
				var faceBuilder = this.faceBuilders[f];
				var face = faceBuilder.toFace(vertices);
				this._faces[f] = face;
			}
		}

		return this._faces;
	}

	public Coords[] vertices()
	{
		if (this._vertices == null)
		{
			this._vertices = new Coords[this.vertexOffsets.length];
			for (var v = 0; v < this.vertexOffsets.length; v++)
			{
				this._vertices[v] = Coords.create();
			}
		}

		for (var v = 0; v < this._vertices.length; v++)
		{
			var vertex = this._vertices[v];
			var vertexOffset = this.vertexOffsets[v];
			vertex.overwriteWith(this.center).add(vertexOffset);
		}

		return this._vertices;
	}

	// transformable

	public Transformable transform(Transform transformToApply)
	{
		for (var v = 0; v < this.vertexOffsets.length; v++)
		{
			var vertexOffset = this.vertexOffsets[v];
			transformToApply.transformCoords(vertexOffset);
		}

		this.vertices(); // hack - Recalculate.

		return this;
	}

	// clonable

	public Mesh clone()
	{
		return new Mesh
		(
			this.center.clone(),
			ArrayHelper.clone(this.vertexOffsets),
			ArrayHelper.clone(this.faceBuilders)
		);
	}

	public Mesh overwriteWith(Mesh other)
	{
		this.center.overwriteWith(other.center);
		ArrayHelper.overwriteWith(this.vertexOffsets, other.vertexOffsets);
		ArrayHelper.overwriteWith(this.faceBuilders, other.faceBuilders);
		return this;
	}

	// transformable

	public Coords[] coordsGroupToTranslate()
	{
		return new Coords[] { this.center };
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		return ShapeHelper.Instance().applyLocationToShapeDefault(loc, this);
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		return this.box().normalAtPos(posToCheck, normalOut);
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut)
	{
		return boxOut.ofPoints(this.vertices());
	}

	// Inner classes.

	class FaceBuilder
	{
		public double[] vertexIndices;

		public FaceBuilder(double[] vertexIndices)
		{
			this.vertexIndices = vertexIndices;
		}

		public Face toFace(Coords[] meshVertices)
		{
			var faceVertices = new Coords[this.vertexIndices.length];
			for (var vi = 0; vi < this.vertexIndices.length; vi++)
			{
				var vertexIndex = this.vertexIndices[vi];
				var meshVertex = meshVertices[vertexIndex];
				faceVertices.push(meshVertex);
			}
			var returnValue = new Face(faceVertices);
			return returnValue;
		}

		public void vertexIndicesShift(double offset)
		{
			for (var i = 0; i < this.vertexIndices.length; i++)
			{
				var vertexIndex = this.vertexIndices[i];
				vertexIndex += offset;
				this.vertexIndices[i] = vertexIndex;
			}
		}

		// clonable

		public FaceBuilder clone()
		{
			return new FaceBuilder(this.vertexIndices.slice());
		}

		public FaceBuilder overwriteWith(Mesh other_FaceBuilder)
		{
			ArrayHelper.overwriteWith(this.vertexIndices, other.vertexIndices);
			return this;
		}

		// Transformable.

		public Transformable transform(Transform transformToApply)
		{
			throw new Error("Not implemented!");
		}
	}
}


