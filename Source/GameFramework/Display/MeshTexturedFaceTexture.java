
package GameFramework.Display;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Utility.*;

public class MeshTexturedFaceTexture implements Clonable<MeshTexturedFaceTexture>
{
	public String materialName;
	public Coords[] textureUVs;

	public MeshTexturedFaceTexture(String materialName, Coords[] textureUVs)
	{
		this.materialName = materialName;
		this.textureUVs = textureUVs;
	}

	public MeshTexturedFaceTexture clone()
	{
		return new MeshTexturedFaceTexture
		(
			this.materialName, ArrayHelper.clone(this.textureUVs)
		);
	}

	public MeshTexturedFaceTexture overwriteWith(MeshTexturedFaceTexture other)
	{
		return this; // todo
	}

	// Transformable.

	public MeshTexturedFaceTexture transform(Transform transformToApply)
	{
		for (var i = 0; i < this.textureUVs.length; i++)
		{
			var textureUV = this.textureUVs[i];
			transformToApply.transformCoords(textureUV);
		}
		return this;
	}
}
