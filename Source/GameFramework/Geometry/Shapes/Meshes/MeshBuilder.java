
package GameFramework.Geometry.Shapes.Meshes;

public class MeshBuilder
{
	public MeshTextured biped(Material material, double heightInPixels)
	{
		var heightOver2 = heightInPixels / 2;
		var heightOver3 = heightInPixels / 3;
		var heightOver4 = heightInPixels / 4;
		var heightOver6 = heightInPixels / 6;
		var heightOver8 = heightInPixels / 8;
		var heightOver12 = heightInPixels / 12;
		var heightOver18 = heightInPixels / 18;
		var heightOver24 = heightInPixels / 24;
		var heightOver36 = heightInPixels / 36;

		var meshesForEntityParts = new Mesh[]
		{
			this.box
			(
				//"Pelvis",
				material,
				new Coords(heightOver12, heightOver24, heightOver24),
				new Coords(0, 0, -heightOver2)
			),

			this.box
			(
				//"Spine.1",
				material,
				new Coords(heightOver12, heightOver24, heightOver6),
				new Coords(0, 0, 0 - heightOver2 - heightOver4)
			),

			this.box
			(
				//"Head",
				material,
				new Coords(heightOver18, heightOver18, heightOver18),
				new Coords(0, heightOver36, 0 - heightInPixels)
			),

			this.box
			(
				//"Thigh.L",
				material,
				new Coords(heightOver36, heightOver36, heightOver8),
				new Coords(heightOver18, 0, 0 - heightOver2 + heightOver12)
			),

			this.box
			(
				//"Shin.L",
				material,
				new Coords(heightOver36, heightOver36, heightOver8),
				new Coords(heightOver18, 0, 0 - heightOver6)
			),

			this.box
			(
				//"Foot.L",
				material,
				new Coords(heightOver36, heightOver12, heightOver36),
				new Coords(heightOver18, heightOver12, 0 - heightOver36)
			),

			this.box
			(
				//"Bicep.L",
				material,
				new Coords(heightOver36, heightOver36, heightOver12),
				new Coords(heightOver6, 0, 0 - heightOver2 - heightOver3)
			),

			this.box
			(
				//"Forearm.L",
				material,
				new Coords(heightOver36, heightOver36, heightOver12),
				new Coords(heightOver6, 0, 0 - heightOver2 - heightOver4 + heightOver8)
			),

			this.box
			(
				//"Thigh.R",
				material,
				new Coords(heightOver36, heightOver36, heightOver8),
				new Coords(0 - heightOver18, 0, 0 - heightOver2 + heightOver12)
			),

			this.box
			(
				//"Shin.R",
				material,
				new Coords(heightOver36, heightOver36, heightOver8),
				new Coords(0 - heightOver18, 0, 0 - heightOver6)
			),

			this.box
			(
				//"Foot.R",
				material,
				new Coords(heightOver36, heightOver12, heightOver36),
				new Coords(0 - heightOver18, heightOver12, 0 - heightOver36)
			),

			this.box
			(
				//"Bicep.R",
				material,
				new Coords(heightOver36, heightOver36, heightOver12),
				new Coords(0 - heightOver6, 0, 0 - heightOver2 - heightOver3)
			),

			this.box
			(
				//"Forearm.R",
				material,
				new Coords(heightOver36, heightOver36, heightOver12),
				new Coords(0 - heightOver6, 0, 0 - heightOver2 - heightOver4 + heightOver8)
			),
		};

		var vertexGroupNames = new String[]
		{
			"Pelvis",
			"Spine.1",
			"Head",
			"Thigh.L",
			"Shin.L",
			"Foot.L",
			"Bicep.L",
			"Forearm.L",
			"Thigh.R",
			"Shin.R",
			"Foot.R",
			"Bicep.R",
			"Forearm.R",
		};

		var returnValue = this.mergeMeshes
		(
			meshesForEntityParts,
			vertexGroupNames
		);

		returnValue.transform
		(
			new Transform_Orient
			(
				new Orientation
				(
					new Coords(0, 1, 0),
					new Coords(0, 0, 1)
				)
			)
		);

		// fix
		//this.meshVerticesMergeIfWithinDistance(returnValue, 3);

		return returnValue;
	}

	public MeshTextured box(Material material, Coords size, Coords pos)
	{
		var returnMesh = this.unitCube(material);

		returnMesh.transform
		(
			new Transform_Scale(size)
		);

		returnMesh.transform
		(
			new Transform_Translate(pos)
		);

		return returnMesh;
	}

	public MeshTextured grid
	(
		Coords sizeInCells, Coords cellSize, Material material
	)
	{
		var numberOfFaces = sizeInCells.x * sizeInCells.y;
		var numberOfVertices =
			(sizeInCells.x + 1) * (sizeInCells.y + 1);

		var vertexPositions = new Coords[numberOfVertices];
		var vertexPosInCells = Coords.create();
		var vertexPos = Coords.create();

		for (var y = 0; y <= sizeInCells.y; y++)
		{
			vertexPosInCells.y = y;

			for (var x = 0; x <= sizeInCells.x; x++)
			{
				vertexPosInCells.x = x;

				vertexPos.overwriteWith
				(
					vertexPosInCells
				).multiply
				(
					cellSize
				);

				var vertexIndex = (y * sizeInCells.x + 1) + x;
				vertexPositions[vertexIndex] = vertexPos.clone();
			}
		}

		var faces = new Face[numberOfFaces];

		for (var y = 0; y < sizeInCells.y; y++)
		{
			for (var x = 0; x < sizeInCells.x; x++)
			{
				var vertexIndex = y * (sizeInCells.x + 1) + x;
				var faceVertexIndices =
				new int[]
				{
					vertexIndex,
					vertexIndex + 1,
					vertexIndex + (sizeInCells.x + 1) + 1,
					vertexIndex + (sizeInCells.x + 1),
				};

				var face = new Mesh.FaceBuilder(faceVertexIndices);

				var faceIndex = y * sizeInCells.x + x;
				faces[faceIndex] = (face);
			}
		}

		var returnMesh = new Mesh
		(
			Coords.create(), // center
			vertexPositions,
			faces // faceBuilders
		);

		if (material != null)
		{
			var faceTextures = [];

			var textureUVs = new Coords[]
			{
				Coords.create(),
				new Coords(1, 0, 0),
				new Coords(1, 1, 0),
				new Coords(0, 1, 0),
			};

			for (var y = 0; y < sizeInCells.y; y++)
			{
				for (var x = 0; x < sizeInCells.x; x++)
				{
					var faceTexture = new MeshTexturedFaceTexture
					(
						material.name, textureUVs
					);

					var faceTextureIndex = y * sizeInCells.x + x;
					faceTextures[faceTextureIndex] = faceTexture;
				}
			}

			var returnMeshTextured = new MeshTextured
			(
				returnMesh, // geometry
				new Material[] { material },
				faceTextures,
				null
			);
		}

		return returnMeshTextured;
	}

	public MeshTextured room
	(
		Coords roomSize,
		Coords[] neighborOffsets,
		boolean[] connectedToNeighbors,
		Material materialWall,
		Material materialFloor,
		double doorwayWidthScaleFactor,
		double wallThickness
	)
	{
		doorwayWidthScaleFactor =
			(doorwayWidthScaleFactor != null ? doorwayWidthScaleFactor : 1);
		wallThickness =
			(wallThickness != null ? wallThickness : 0);

		var wallNormals = neighborOffsets;

		if (connectedToNeighbors == null)
		{
			connectedToNeighbors = new boolean[] { false, false, false, false };
		}

		var meshesForRoom = [];

		var down = new Coords(0, 0, 1);

		for (var i = 0; i < wallNormals.length; i++)
		{
			var wallNormal = wallNormals[i];

			var meshForWall;
			var wallDisplacement;

			var connectedToNeighbor = connectedToNeighbors[i];
			if (connectedToNeighbor)
			{
				meshForWall = this.room_WallWithDoorway
				(
					materialWall, doorwayWidthScaleFactor, wallThickness
				);
				wallDisplacement = wallNormal.clone().multiplyScalar(1 - wallThickness);
			}
			else
			{
				meshForWall = this.room_Wall(materialWall);
				wallDisplacement = wallNormal.clone();
			}

			var wallOrientation =
				new Orientation(wallNormal.clone(), down.clone());

			meshForWall.transform
			(
				new Transform_OrientRDF(wallOrientation)
			).transform
			(
				new Transform_Translate(wallDisplacement)
			);

			meshesForRoom.add
			(
				meshForWall
			);
		}

		var meshForFloor = this.room_Floor(materialFloor);
		meshesForRoom.add(meshForFloor);

		//var meshForCeiling = this.room_Ceiling(material);
		//meshesForRoom.push(meshForCeiling);

		for (var i = 0; i < meshesForRoom.length; i++)
		{
			var mesh = meshesForRoom[i];

			var face = mesh.geometry.faces()[0];
			var faceNormal = face.plane().normal;

			var faceOrientationDown =
			(
				faceNormal.z == 0 ? down : new Coords(1, 0, 0)
			);
			var faceOrientation = new Orientation
			(
				faceNormal, faceOrientationDown
			);

			var faceTangent = faceOrientation.right;
			var faceDown = faceOrientation.down;

			mesh.transformFaceTextures
			(
				new Transform_Scale
				(
					new Coords
					(
						faceTangent.dotProduct(roomSize),
						faceDown.dotProduct(roomSize),
						0
					).absolute().multiplyScalar
					(
						.2
					)
				)
			)
		}

		var returnMesh = this.mergeMeshes(meshesForRoom, new Array<String>());

		returnMesh.transform
		(
			new Transform_Scale(roomSize) as Transform
		).transform
		(
			new Transform_Translate(new Coords(0, 0, -roomSize.z))
		);

		return returnMesh;
	}

	public MeshTextured room_Ceiling(Material material)
	{
		var returnMesh = this.unitSquare
		(
			material
		).transform
		(
			new Transform_Scale
			(
				new Coords(1, 1, -1)
			)
		).transform
		(
			new Transform_Translate
			(
				new Coords(0, 0, -1)
			)
		).transformFaceTextures
		(
			new Transform_Scale(new Coords(1, 1, 1).multiplyScalar(.2))
		);

		return returnMesh;
	}

	public MeshTextured room_Floor(Material material)
	{
		var returnMesh = this.unitSquare
		(
			material
		).transform
		(
			new Transform_Translate
			(
				new Coords(0, 0, 1)
			)
		).transformFaceTextures
		(
			new Transform_Scale(new Coords(1, 1, 1).multiplyScalar(9))
		);

		return returnMesh;
	}

	public MeshTextured room_Wall(Material material)
	{
		var returnMesh = new Mesh
		(
			Coords.create(), // center
			// vertices
			[
				// wall
				new Coords(1, -1, 0),
				new Coords(-1, -1, 0),
				new Coords(-1, 1, 0),
				new Coords(1, 1, 0),

			],
			// faces
			[
				new Mesh.FaceBuilder([0, 1, 2, 3]),
			]
		);

		var returnMeshTextured = new MeshTextured
		(
			returnMesh,
			[ material ],
			[
				new MeshTexturedFaceTexture
				(
					material.name,
					[
						new Coords(1, 0, 0),
						Coords.create(),
						new Coords(0, 1, 0),
						new Coords(1, 1, 0),
					]
				),
			],
			null
		);

		return returnMeshTextured;
	}

	public MeshTextured room_WallWithDoorway
	(
		Material material, double doorwayWidthScaleFactor, double wallThickness
	)
	{
		var doorwayHeight = 0.5;
		var doorwayWidthHalf = doorwayHeight * doorwayWidthScaleFactor / 2;

		var wt = wallThickness;

		var returnMesh = new Mesh
		(
			Coords.create(), // center
			// vertices
			[
				// wall

				// b = bottom, t = top, l = left, r = right.
				// top
				new Coords(-doorwayWidthHalf, -doorwayHeight, 0), // bl - 0
				new Coords(doorwayWidthHalf, -doorwayHeight, 0), // br - 1
				new Coords(doorwayWidthHalf, -1, 0), // tr - 2
				new Coords(-doorwayWidthHalf, -1, 0), // tl - 3

				// left
				new Coords(-1, 1, 0), // bl - 4
				new Coords(-doorwayWidthHalf, 1, 0), // br - 5
				new Coords(-doorwayWidthHalf, -1, 0), // tr - 6
				new Coords(-1, -1, 0), // tl - 7

				// right
				new Coords(doorwayWidthHalf, 1, 0), // bl - 8
				new Coords(1, 1, 0), // br - 9
				new Coords(1, -1, 0), // tr - 10
				new Coords(doorwayWidthHalf, -1, 0), // tl - 11

				// doorframe
				new Coords(-doorwayWidthHalf, 1, wt), // bl - 12
				new Coords(doorwayWidthHalf, 1, wt), // br - 13
				new Coords(doorwayWidthHalf, -doorwayHeight, wt), // tr - 14
				new Coords(-doorwayWidthHalf, -doorwayHeight, wt), // tl - 15
			],
			// vertexIndicesForFaces
			[
				// wall
				new Mesh.FaceBuilder([ 0, 1, 2, 3]), // top
				new Mesh.FaceBuilder([ 4, 5, 6, 7 ]), // left
				new Mesh.FaceBuilder([ 8, 9, 10, 11 ]), // right

				// doorframe
				new Mesh.FaceBuilder([ 5, 12, 15, 0  ]), // left
				new Mesh.FaceBuilder([ 1, 14, 13, 8 ]), // right

				// todo - top - Hard to see currently.
			]
		);

		var doorwayWidth = doorwayWidthHalf * 2;
		var doorwayWidthReversed = 1 - doorwayWidth;
		var doorwayWidthReversedHalf = doorwayWidthReversed / 2;
		var doorwayHeightReversed = 1 - doorwayHeight;

		var transformScaleSides = new Transform_Scale
		(
			new Coords(doorwayWidthReversedHalf, .5, 0)
		) as Transform;

		var transformScaleTop = new Transform_Scale
		(
			new Coords(doorwayWidthHalf, doorwayHeightReversed, 0)
		) as Transform;

		var transformScaleSidesDoorframe = new Transform_Scale
		(
			new Coords(wallThickness, doorwayHeight, 0)
		) as Transform;

		var materialName = material.name;

		var faceTextures =
		[
			// wall
			// top
			new MeshTexturedFaceTexture
			(
				materialName,
				[
					new Coords(0, 1, 0),
					new Coords(1, 1, 0),
					new Coords(1, 0, 0),
					Coords.create(),
				]
			).transform(transformScaleTop),
			// left
			new MeshTexturedFaceTexture
			(
				materialName,
				[
					new Coords(0, 1, 0),
					new Coords(1, 1, 0),
					new Coords(1, 0, 0),
					Coords.create(),
				]
			).transform(transformScaleSides),
			// right
			new MeshTexturedFaceTexture
			(
				materialName,
				[
					new Coords(0, 1, 0),
					new Coords(1, 1, 0),
					new Coords(1, 0, 0),
					Coords.create(),
				]
			).transform(transformScaleSides),
			// doorframe
			// left
			new MeshTexturedFaceTexture
			(
				materialName,
				[
					new Coords(0, 1, 0),
					new Coords(1, 1, 0),
					new Coords(1, 0, 0),
					Coords.create(),
				]
			).transform(transformScaleSidesDoorframe),
			// right
			new MeshTexturedFaceTexture
			(
				materialName,
				[
					new Coords(0, 1, 0),
					new Coords(1, 1, 0),
					new Coords(1, 0, 0),
					Coords.create(),
				]
			).transform(transformScaleSidesDoorframe),
			// todo - top
		];

		var returnMeshTextured = new MeshTextured
		(
			returnMesh,
			[ material ],
			faceTextures,
			null
		).transformFaceTextures
		(
			new Transform_Scale( new Coords(1, 1, 1).multiplyScalar(2) )
		);

		return returnMeshTextured;
	}

	public MeshTextured unitCube(Material material)
	{
		var returnMesh = this.unitCube_Geometry();
		var returnMeshTextured =
			MeshTextured.fromMeshAndMaterials(returnMesh, [ material ]);
		return returnMeshTextured;
	}

	public Mesh unitCube_Geometry()
	{
		var returnMesh = new Mesh
		(
			Coords.create(), // center
			// vertices
			new Coords[]
			{
				// top
				new Coords(-1, -1, -1),
				new Coords(1, -1, -1),
				new Coords(1, 1, -1),
				new Coords(-1, 1, -1),

				// bottom
				new Coords(-1, -1, 1),
				new Coords(1, -1, 1),
				new Coords(1, 1, 1),
				new Coords(-1, 1, 1),
			},
			// vertexIndicesForFaces
			new Mesh.FaceBuilder[]
			{
				new Mesh.FaceBuilder(new int[] {7, 3, 0, 4}), // west
				new Mesh.FaceBuilder(new int[] {5, 1, 2, 6}), // east

				new Mesh.FaceBuilder(new int[] {4, 0, 1, 5}), // north
				new Mesh.FaceBuilder(new int[] {6, 2, 3, 7}), // south

				new Mesh.FaceBuilder(new int[] {0, 3, 2, 1}), // top
				new Mesh.FaceBuilder(new int[] {5, 6, 7, 4}), // bottom
			]
		);

		return returnMesh;
	}

	unitRing(Material material, double doubleOfVertices): MeshTextured
	{
		var Coords vertices[] = [];
		var double vertexIndicesForFace[] = [];

		for (var i = 0; i < doubleOfVertices; i++)
		{
			var vertexAngleInTurns = i / doubleOfVertices;

			var vertexPolar = new Polar(vertexAngleInTurns, 1, 0);
			var vertex = vertexPolar.toCoords(Coords.create());

			vertices.add(vertex);

			vertexIndicesForFace.splice(0, 0, i);
		}

		var returnMesh = new Mesh
		(
			Coords.create(), // center
			vertices,
			[ new Mesh.FaceBuilder(vertexIndicesForFace) ]
		);

		var returnMeshTextured =
			MeshTextured.fromMeshAndMaterials(returnMesh, [ material ]);

		return returnMeshTextured;
	}

	public MeshTextured unitSquare(Material material)
	{
		var returnMesh = new Mesh
		(
			Coords.create(), // center
			// vertices
			new Coords[]
			{
				// back
				new Coords(1, -1, 0),
				new Coords(1, 1, 0),
				new Coords(-1, 1, 0),
				new Coords(-1, -1, 0),
			},
			// vertexIndicesForFaces
			new Mesh.FaceBuilder[]
			{
				new Mesh.FaceBuilder(new int[] {3, 2, 1, 0})
				//[0, 1, 2, 3]
			}
		);

		var returnMeshTextured = new MeshTextured
		(
			returnMesh,
			new Material[] { material },
			new MeshTexturedFaceTexture[]
			{
				new MeshTexturedFaceTexture
				(
					material.name,
					new Coords[]
					{
						Coords.create(),
						new Coords(1, 0, 0),
						new Coords(1, 1, 0),
						new Coords(0, 1, 0),
					}
				)
			},
			null
		);

		return returnMeshTextured;
	}

	public FaceTextured clipFaceAgainstPlanes
	(
		FaceTextured faceToClip, Plane[] planesToClipAgainst
	)
	{
		for (var p = 0; p < planesToClipAgainst.length; p++)
		{
			faceToClip = this.splitFaceByPlaneFrontAndBack
			(
				faceToClip,
				planesToClipAgainst[p]
			)[0];

			if (faceToClip == null)
			{
				break;
			}
		}

		return faceToClip;
	}

	public MeshTextured mergeMeshes
	(
		MeshTextured[] meshesToMerge, String[] vertexGroupNames
	)
	{
		var verticesMerged = new ArrayList<Coords>();
		var faceBuildersMerged = new ArrayList<Mesh.FaceBuilder>();
		var faceTexturesMerged = new ArrayList<MeshTexturedFaceTexture>();
		var vertexGroups = new ArrayList<VertexGroup>();

		var doubleOfVerticesSoFar = 0;

		for (var m = 0; m < meshesToMerge.length; m++)
		{
			var meshToMerge = meshesToMerge[m];
			var meshToMergeGeometry = meshToMerge.geometry;
			var verticesToMerge = meshToMergeGeometry.vertices();

			verticesMerged = verticesMerged.concat
			(
				verticesToMerge
			);

			var faceBuildersToMerge = meshToMergeGeometry.faceBuilders;
			for (var f = 0; f < faceBuildersToMerge.length; f++)
			{
				var faceBuilder = faceBuildersToMerge[f];
				faceBuilder.vertexIndicesShift(doubleOfVerticesSoFar);
				faceBuildersMerged.add(faceBuilder);
			}

			var faceTextures = meshToMerge.faceTextures;
			if (faceTextures != null)
			{
				for (var f = 0; f < faceTextures.length; f++)
				{
					var faceTexture = faceTextures[f];
					var faceTextureCloned = faceTexture.clone();
					faceTexturesMerged.add(faceTextureCloned);
				}
			}

			if (vertexGroupNames != null)
			{
				var vertexIndicesInVertexGroup = [];
				for (var v = 0; v < verticesToMerge.length; v++)
				{
					vertexIndicesInVertexGroup.add(doubleOfVerticesSoFar + v);
				}

				var vertexGroup = new VertexGroup
				(
					vertexGroupNames[m],
					vertexIndicesInVertexGroup
				);

				vertexGroups.add(vertexGroup);
			}

			doubleOfVerticesSoFar += verticesToMerge.length;
		}

		var returnMesh = new Mesh
		(
			Coords.create(), // center
			verticesMerged,
			faceBuildersMerged
		);

		var materialsMerged = new ArrayList<Material>();
		var materialsMergedByName = new Map<String, Material>();

		for (var i = 0; i < meshesToMerge.length; i++)
		{
			var meshToMergeMaterials = meshesToMerge[i].materials;
			for (var m = 0; m < meshToMergeMaterials.length; m++)
			{
				var material = meshToMergeMaterials[m];
				var materialName = material.name;
				if (materialsMergedByName.get(materialName) == null)
				{
					materialsMerged.add(material);
					materialsMergedByName.put(materialName, material);
				}
			}
		}

		var returnMeshTextured = new MeshTextured
		(
			returnMesh,
			materialsMerged,
			faceTexturesMerged,
			vertexGroups
		);

		return returnMeshTextured;
	}

	public List<FaceTextured> splitFaceByPlaneFrontAndBack
	(
		FaceTextured faceToDivide, Plane planeToDivideOn
	)
	{
		var returnValues = new ArrayList<FaceTextured>();

		var verticesInFacesDivided = new ArrayList<Coords>[]
		[
			new ArrayList<Coords>(), // front
			new ArrayList<Coords>() // back
		];

		var distanceOfVertexAbovePlane = 0;

		var faceToDivideVertices = faceToDivide.geometry.vertices;
		for (var v = 0; v < faceToDivideVertices.length; v++)
		{
			var vertex = faceToDivideVertices[v];

			distanceOfVertexAbovePlane =
				planeToDivideOn.distanceToPointAlongNormal(vertex);

			if (distanceOfVertexAbovePlane != 0)
			{
				break;
			}
		}

		var facesDividedIndex = (distanceOfVertexAbovePlane > 0 ? 0 : 1);

		var verticesInFaceDivided =
			verticesInFacesDivided[facesDividedIndex];

		var doAnyEdgesCollideWithPlaneSoFar = false;

		var collisionHelper = new CollisionHelper();
		var collision = Collision.create();

		var edges = faceToDivide.geometry.edges();
		for (var e = 0; e < edges.length; e++)
		{
			var edge = edges[e];
			var vertex0 = edge.vertices[0];

			verticesInFaceDivided.add
			(
				vertex0
			);

			var distanceOfVertex0AbovePlane =
				planeToDivideOn.distanceToPointAlongNormal(vertex0);

			var distanceOfVertex1AbovePlane =
				planeToDivideOn.distanceToPointAlongNormal
				(
					edge.vertices[1]
				);

			if (distanceOfVertex0AbovePlane * distanceOfVertex1AbovePlane < 0)
			{
				var collision = collisionHelper.collisionOfEdgeAndPlane
				(
					edge, planeToDivideOn, collision
				);

				if (collision.isActive)
				{
					doAnyEdgesCollideWithPlaneSoFar = true;

					verticesInFaceDivided.add
					(
						collision.pos
					);

					facesDividedIndex = 1 - facesDividedIndex;
					verticesInFaceDivided =
						verticesInFacesDivided[facesDividedIndex];

					verticesInFaceDivided.add(collision.pos);
				}
			}
		}

		if (doAnyEdgesCollideWithPlaneSoFar)
		{
			for (var i = 0; i < verticesInFacesDivided.length; i++)
			{
				var verticesInFace = verticesInFacesDivided[i];

				if (verticesInFace.length > 2)
				{
					var faceDivided = new FaceTextured
					(
						new Face(verticesInFace), faceToDivide.material
					);

					returnValues.add(faceDivided);
				}
			}
		}
		else
		{
			returnValues[facesDividedIndex] = faceToDivide;
		}

		return returnValues;
	}
}
