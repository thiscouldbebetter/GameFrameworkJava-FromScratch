
package GameFramework.Geometry.Transforms;

import GameFramework.Geometry.*;

public class Transform_Orient implements Transform
{
	public Orientation orientation;

	public Coords _components[];

	public Transform_Orient(Orientation orientation)
	{
		this.orientation = orientation;

		this._components = mew Coords[] { Coords.create(), Coords.create(), Coords.create() };
	}

	public Transform_Orient overwriteWith(Transform other)
	{
		return this; // todo
	}

	public Transformable transform(Transformable transformable)
	{
		return transformable.transform(this);
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		var components = this._components;
		var ori = this.orientation;

		coordsToTransform.overwriteWith
		(
			components[0].overwriteWith(ori.forward).multiplyScalar(coordsToTransform.x).add
			(
				components[1].overwriteWith(ori.right).multiplyScalar(coordsToTransform.y).add
				(
					components[2].overwriteWith(ori.down).multiplyScalar(coordsToTransform.z)
				)
			)
		);

		return coordsToTransform;
	}
}

}
