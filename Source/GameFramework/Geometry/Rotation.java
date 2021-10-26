
package GameFramework.Geometry;

import GameFramework.Helpers.*;
import GameFramework.Utility.*;

public class Rotation
{
	public Coords axis;
	public Reference angleInTurnsRef;

	public Rotation(Coords axis, Reference angleInTurnsRef)
	{
		this.axis = axis;
		this.angleInTurnsRef = angleInTurnsRef;
	}

	public double angleInTurns()
	{
		return this.angleInTurnsRef.get();
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		// hack - Assume axis is (0, 0, 1).
		var polar = new Polar(0, 0, 0).fromCoords(coordsToTransform);

		polar.azimuthInTurns = NumberHelper.wrapToRangeMinMax
		(
			polar.azimuthInTurns + this.angleInTurns(), 0, 1
		);

		return polar.toCoords(coordsToTransform);
	}

	public Orientation transformOrientation(Orientation orientation)
	{
		orientation.forwardSet(this.transformCoords(orientation.forward));
		return orientation;
	}
}
