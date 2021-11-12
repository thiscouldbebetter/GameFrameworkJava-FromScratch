
package GameFramework.Geometry.Shapes;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Helpers.*;

public class Path implements Transformable<Path>
{
	public Coords[] points;

	public Path(Coords[] points)
	{
		this.points = points;
	}

	// Clonable.

	public Path clone()
	{
		return new Path
		(
			ArrayHelper.clone
			(
				Arrays.asList(this.points)
			).toArray
			(
				new Coords[] {}
			)
		);
	}

	public Path overwriteWith(Path other)
	{
		ArrayHelper.overwriteWith(this.points, other.points);
		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public Path transform(Transform transformToApply)
	{
		Transforms.applyTransformToCoordsMany(transformToApply, this.points);
		return this;
	}
}
