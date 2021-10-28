
package GameFramework.Geometry.Shapes.Meshes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;
import GameFramework.Utility.*;

public class FaceBuilder implements Clonable<FaceBuilder>
{
	public int[] vertexIndices;

	public FaceBuilder(int[] vertexIndices)
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
			faceVertices.add(meshVertex);
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
		return new FaceBuilder(ArrayHelper.clone(this.vertexIndices));
	}

	public FaceBuilder overwriteWith(FaceBuilder other)
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
