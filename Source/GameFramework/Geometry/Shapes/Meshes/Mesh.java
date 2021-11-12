
package GameFramework.Geometry.Shapes.Meshes;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class Mesh implements ShapeBase<Mesh>
{
	public Coords center;
	public Coords[] vertexOffsets;
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
		var numberOfFaceVertices = faceVertices.length;
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

			var vertexIndicesTopOrBottom = new ArrayList<Integer>();

			for (var v = 0; v < numberOfFaceVertices; v++)
			{
				var vertexIndex = meshVertices.size();

				if (z == 0)
				{
					var vertexIndexNext = NumberHelper.wrapToRangeMinMax
					(
						vertexIndex + 1,
						0, numberOfFaceVertices
					);

					var faceBuilderSide = new FaceBuilder
					(
						new int[]
						{
							vertexIndex,
							vertexIndexNext,
							vertexIndexNext + numberOfFaceVertices,
							vertexIndex + numberOfFaceVertices
						}
					);
					faceBuilders.add(faceBuilderSide);

					vertexIndicesTopOrBottom.add(vertexIndex);
				}
				else
				{
					vertexIndicesTopOrBottom.add(0, new Integer(vertexIndex));
				}

				var vertex = faceVertices[v].clone().add
				(
					offsetForExtrusion
				);

				meshVertices.add(vertex);
			}

			var vertexIndicesTopOrBottomAsArray =
				new int[vertexIndicesTopOrBottom.size()];

			// It's oddly difficult to convert an Integer[] to an int[].
			for (var vi = 0; vi < vertexIndicesTopOrBottom.size(); vi++)
			{
				vertexIndicesTopOrBottomAsArray[vi] =
					vertexIndicesTopOrBottom.get(vi);
			}

			var faceBuilderTopOrBottom = new FaceBuilder
			(
				vertexIndicesTopOrBottomAsArray
			);
			faceBuilders.add(faceBuilderTopOrBottom);
		}

		var returnValue = new Mesh
		(
			center,
			meshVertices.toArray(new Coords[] {}), // vertexOffsets
			faceBuilders.toArray(new FaceBuilder[] {} )
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

	public Mesh transform(Transform transformToApply)
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
			ArrayHelper.clone
			(
				Arrays.asList(this.vertexOffsets)
			).toArray(new Coords[] {}),
			ArrayHelper.clone
			(
				this.faceBuilders
			).toArray(new FaceBuilder[] {})
		);
	}

	public Mesh overwriteWith(Mesh other)
	{
		this.center.overwriteWith(other.center);
		ArrayHelper.overwriteWith(this.vertexOffsets, other.vertexOffsets);
		ArrayHelper.overwriteWith(this.faceBuilders, other.faceBuilders);
		return this;
	}

	/*
	// Clonable<ShapeBase>.

	public ShapeBase clone()
	{
		return this.clone();
	}

	public ShapeBase overwriteWith(ShapeBase other)
	{
		var otherAsMesh = (Mesh)other;
		this.overwriteWith(otherAsMesh);
		return (ShapeBase)this;
	}
	*/

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
}


