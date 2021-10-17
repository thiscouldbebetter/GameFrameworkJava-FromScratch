
package GameFramework.Display;

import GameFramework.Utility.*;

public class VertexGroup implements Clonable<VertexGroup>
{
	public String name;
	public int[] vertexIndices;

	public VertexGroup(String name, int[] vertexIndices)
	{
		this.name = name;
		this.vertexIndices = vertexIndices;
	}

	// cloneable

	public VertexGroup clone()
	{
		return new VertexGroup(this.name, ArrayHelper.clone(this.vertexIndices));
	}

	public VertexGroup overwriteWith(VertexGroup other)
	{
		return this; // todo
	}
}
