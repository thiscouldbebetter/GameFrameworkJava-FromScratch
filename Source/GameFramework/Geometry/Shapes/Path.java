
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class Path
{
	public Coords[] points;

	public Path(Coords[] points)
	{
		this.points = points;
	}

	// Clonable.

	public Path clone()
	{
		return new Path(ArrayHelper.clone(this.points) );
	}

	public Path overwriteWith(Path other)
	{
		ArrayHelper.overwriteWith(this.points, other.points);
		return this;
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		Transforms.applyTransformToCoordsMObject(transformToApply, this.points);
		return this;
	}
}
