
package GameFramework.Display;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Shapes.Meshes.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;
import GameFramework.Utility.*;

public class MeshTextured
{
	public Mesh geometry;
	public Material[] materials;
	public Map<String,Material> materialsByName;
	public MeshTexturedFaceTexture[] faceTextures;
	public VertexGroup[] vertexGroups;

	private Map<String,Integer[]> _faceIndicesByMaterialName;
	private FaceTextured[] _faces;

	public MeshTextured
	(
		Mesh geometry,
		Material[] materials,
		MeshTexturedFaceTexture[] faceTextures,
		VertexGroup[] vertexGroups
	)
	{
		this.geometry = geometry;
		this.materials = materials;
		this.materialsByName = ArrayHelper.addLookupsByName(this.materials);
		this.faceTextures = faceTextures;
		this.vertexGroups = vertexGroups;
	}

	public static MeshTextured fromMeshAndMaterials
	(
		Mesh geometry, Material materials[]
	)
	{
		return new MeshTextured(geometry, materials, null, null);
	}

	public FaceTextured[] faces()
	{
		if (this._faces == null)
		{
			var geometryFaces = this.geometry.faces();
			this._faces = new FaceTextured[geometryFaces.length];
			for (var i = 0; i < geometryFaces.length; i++)
			{
				var geometryFace = geometryFaces[i];
				var faceTexture = this.faceTextures[i];
				var faceMaterialName = faceTexture.materialName;
				var faceMaterial = this.materialsByName.get(faceMaterialName);
				var face = new FaceTextured(geometryFace, faceMaterial);
				this._faces[i] = face;
			}
		}

		return this._faces;
	}

	public MeshTextured faceTexturesBuild()
	{
		var materialName = this.materials[0].name();

		var numberOfFaces = this.geometry.faceBuilders.length;

		var faceTextures = new MeshTexturedFaceTexture[numberOfFaces];

		for (var f = 0; f < numberOfFaces; f++)
		{
			var faceTexture = new MeshTexturedFaceTexture
			(
				materialName,
				new Coords[]
				{
					Coords.create(),
					new Coords(1, 0, 0),
					new Coords(1, 1, 0),
					new Coords(1, 0, 0)
				}
			);
			faceTextures[f] = faceTexture;
		}

		this.faceTextures = faceTextures;

		return this;
	}

	public Map<String,Integer[]> faceIndicesByMaterialName()
	{
		if (this._faceIndicesByMaterialName == null)
		{
			this._faceIndicesByMaterialName = new HashMap<String,Integer[]>();

			for (var f = 0; f < this.faceTextures.length; f++)
			{
				var faceTexture = this.faceTextures[f];

				var faceMaterialName = faceTexture.materialName;
				var faceIndicesForMaterial =
					this._faceIndicesByMaterialName.get(faceMaterialName);
				if (faceIndicesForMaterial == null)
				{
					faceIndicesForMaterial = new Integer[] {};
					this._faceIndicesByMaterialName.put
					(
						faceMaterialName,
						faceIndicesForMaterial
					);
				}
				faceIndicesForMaterial[f] = f;
			}
		}

		return this._faceIndicesByMaterialName;
	}

	public MeshTextured transform(Transform transformToApply)
	{
		this.geometry.transform(transformToApply);

		return this;
	}

	public MeshTextured transformFaceTextures(Transform transformToApply)
	{
		for (var i = 0; i < this.faceTextures.length; i++)
		{
			var faceTexture = this.faceTextures[i];
			faceTexture.transform(transformToApply);
		}

		return this;
	}

	// cloneable

	public MeshTextured clone()
	{
		return new MeshTextured
		(
			this.geometry.clone(),
			this.materials,
			ArrayHelper.clone(this.faceTextures),
			ArrayHelper.clone(this.vertexGroups)
		);
	}

	public MeshTextured overwriteWith(MeshTextured other)
	{
		this.geometry.overwriteWith(other.geometry);
		// todo
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		throw new Error("Not implemented!");
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		throw new Error("Not implemented!");
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		throw new Error("Not implemented!");
	}

	public Box toBox(Box boxOut)
	{
		throw new Error("Not implemented!");
	}
}
