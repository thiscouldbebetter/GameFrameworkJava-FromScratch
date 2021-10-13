
package GameFramework.Geometry.Shapes;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Utility.*;

public class Box implements ShapeBase
{
	public Coords center;
	public Coords size;

	private Coords _min;
	private Coords _max;
	private RangeExtent _range;
	private Coords _sizeHalf;
	private Coords _vertices[];

	public Box(Coords center, Coords size)
	{
		this.center = (center != null ? center : Coords.create());
		this.size = (size != null ? size : Coords.create());

		this._sizeHalf = Coords.create();
		this._min = Coords.create();
		this._max = Coords.create();

		this._range = new RangeExtent(0, 0);
	}

	public static Box create()
	{
		return Box.fromCenterAndSize(Coords.create(), Coords.create());
	}

	public static Box _default()
	{
		return Box.fromCenterAndSize(Coords.zeroes(), Coords.ones() );
	}

	public static Box fromCenterAndSize(Coords center, Coords size)
	{
		// This takes the same arguments as the constructor.
		return new Box(center, size);
	}

	public static Box fromMinAndMax(Coords min, Coords max)
	{
		var center = min.clone().add(max).half();
		var size = max.clone().subtract(min);
		return new Box(center, size);
	}

	public static Box fromMinAndSize(Coords min, Coords size)
	{
		var center = size.clone().half().add(min);
		return new Box(center, size);
	}

	public static Box fromSize(Coords size)
	{
		return new Box(Coords.create(), size);
	}

	public static Box fromSizeAndCenter(Coords size, Coords center)
	{
		// Same arguments as the constructor, but different order.
		return new Box(center, size);
	}

	// Static methods.

	public static boolean doBoxesInSetsOverlap(Box[] boxSet0, Box[] boxSet1)
	{
		var doAnyBoxOverlapSoFar = false;

		for (var i = 0; i < boxSet0.length; i++)
		{
			var boxFromSet0 = boxSet0[i];

			for (var j = 0; j < boxSet1.length; j++)
			{
				var boxFromSet1 = boxSet1[j];

				doAnyBoxOverlapSoFar = boxFromSet0.overlapsWith
				(
					boxFromSet1
				);

				if (doAnyBoxOverlapSoFar)
				{
					break;
				}
			}
		}

		return doAnyBoxOverlapSoFar;
	}

	// Instance methods.

	public boolean containsOther(Box other)
	{
		return ( this.containsPoint(other.min()) && this.containsPoint(other.max()) );
	}

	public boolean containsPoint(Coords pointToCheck)
	{
		return pointToCheck.isInRangeMinMax(this.min(), this.max());
	}

	public Box setMinAndMax(Coords min, Coords max)
	{
		this.center.overwriteWith(min).add(max).half();
		this.size.overwriteWith(max).subtract(min);
		return this;
	}

	public Box intersectWith(Box other)
	{
		var thisMinDimensions = this.min().dimensions();
		var thisMaxDimensions = this.max().dimensions();
		var otherMinDimensions = other.min().dimensions();
		var otherMaxDimensions = other.max().dimensions();

		var rangesForDimensions = new RangeExtent[]
		{
			new RangeExtent(0, 0), new RangeExtent(0, 0), new RangeExtent(0, 0)
		};
		var rangeOther = new RangeExtent(0, 0);

		var doAllDimensionsOverlapSoFar = true;

		for (var d = 0; d < rangesForDimensions.length; d++)
		{
			var rangeThis = rangesForDimensions[d];
			rangeThis.overwriteWithMinAndMax
			(
				thisMinDimensions[d], thisMaxDimensions[d]
			);
			rangeOther.overwriteWithMinAndMax
			(
				otherMinDimensions[d], otherMaxDimensions[d]
			);
			var doesDimensionOverlap = rangeThis.overlapsWith(rangeOther);
			if (doesDimensionOverlap)
			{
				rangeThis.intersectWith(rangeOther);
			}
			else
			{
				doAllDimensionsOverlapSoFar = false;
				break;
			}
		}

		var returnValue = null;

		if (doAllDimensionsOverlapSoFar)
		{
			var center = Coords.create();
			var size = Coords.create();
			for (var d = 0; d < rangesForDimensions.length; d++)
			{
				var rangeForDimension = rangesForDimensions[d];
				center.dimensionSet(d, rangeForDimension.midpoint());
				size.dimensionSet(d, rangeForDimension.size());
			}

			returnValue = new Box(center, size);
		}

		return returnValue;
	}

	public Box locate(Disposition loc)
	{
		this.center.overwriteWith(loc.pos);
		return this;
	}

	public Coords max()
	{
		return this._max.overwriteWith(this.center).add(this.sizeHalf());
	}

	public Coords min()
	{
		return this._min.overwriteWith(this.center).subtract(this.sizeHalf());
	}

	public Box ofPoints(Coords points[])
	{
		var point0 = points[0];
		var minSoFar = this._min.overwriteWith(point0);
		var maxSoFar = this._max.overwriteWith(point0);

		for (var i = 1; i < points.length; i++)
		{
			var point = points[i];

			if (point.x < minSoFar.x)
			{
				minSoFar.x = point.x;
			}
			else if (point.x > maxSoFar.x)
			{
				maxSoFar.x = point.x;
			}

			if (point.y < minSoFar.y)
			{
				minSoFar.y = point.y;
			}
			else if (point.y > maxSoFar.y)
			{
				maxSoFar.y = point.y;
			}

			if (point.z < minSoFar.z)
			{
				minSoFar.z = point.z;
			}
			else if (point.z > maxSoFar.z)
			{
				maxSoFar.z = point.z;
			}
		}

		this.center.overwriteWith(minSoFar).add(maxSoFar).half();
		this.size.overwriteWith(maxSoFar).subtract(minSoFar);

		return this;
	}

	public boolean overlapsWith(Box other)
	{
		var returnValue =
		(
			this.overlapsWithOtherInDimension(other, 0)
			&& this.overlapsWithOtherInDimension(other, 1)
			&& this.overlapsWithOtherInDimension(other, 2)
		);
		return returnValue;
	}

	public boolean overlapsWithXY(Box other)
	{
		var returnValue =
		(
			this.overlapsWithOtherInDimension(other, 0)
			&& this.overlapsWithOtherInDimension(other, 1)
		);
		return returnValue;
	}

	public boolean overlapsWithOtherInDimension(Box other, int dimensionIndex)
	{
		var rangeThis = this.rangeForDimension(dimensionIndex, this._range);
		var rangeOther = other.rangeForDimension(dimensionIndex, other._range);
		var returnValue = rangeThis.overlapsWith(rangeOther);
		return returnValue;
	}

	public Coords posRandom(Randomizer randomizer)
	{
		var min = this.min();
		var offset = Coords._default().randomize
		(
			randomizer
		).multiply
		(
			this.size
		);
		var pos = offset.add(min);
		return pos;
	}

	public Box randomize(Randomizer randomizer)
	{
		this.center.randomize(randomizer);
		this.size.randomize(randomizer);

		return this;
	}

	public RangeExtent rangeForDimension
	(
		int dimensionIndex, RangeExtent rangeOut
	)
	{
		rangeOut.min = this.min().dimensionGet(dimensionIndex);
		rangeOut.max = this.max().dimensionGet(dimensionIndex);
		return rangeOut;
	}

	public Coords sizeHalf()
	{
		return this._sizeHalf.overwriteWith(this.size).half();
	}

	public Box sizeOverwriteWith(Coords sizeOther)
	{
		this.size.overwriteWith(sizeOther);
		return this;
	}

	public boolean touches(Box other)
	{
		var returnValue =
		(
			this.touchesOtherInDimension(other, 0)
			&& this.touchesOtherInDimension(other, 1)
			&& this.touchesOtherInDimension(other, 2)
		);
		return returnValue;
	}

	public boolean touchesXY(Box other)
	{
		var returnValue =
		(
			this.touchesOtherInDimension(other, 0)
			&& this.touchesOtherInDimension(other, 1)
		);
		return returnValue;
	}

	public boolean touchesOtherInDimension(Box other, int dimensionIndex)
	{
		var rangeThis = this.rangeForDimension(dimensionIndex, this._range);
		var rangeOther = other.rangeForDimension(dimensionIndex, other._range);
		var returnValue = rangeThis.touches(rangeOther);
		return returnValue;
	}

	public Coords trimCoords(Coords coordsToTrim)
	{
		return coordsToTrim.trimToRangeMinMax(this.min(), this.max());
	}

	public Coords[] vertices()
	{
		if (this._vertices == null)
		{
			this._vertices = new Coords[] {};
			// todo
		}
		return this._vertices;
	}

	// Clonable.

	public Box clone()
	{
		return new Box(this.center.clone(), this.size.clone());
	}

	public boolean equals(Box other)
	{
		var returnValue =
		(
			this.center.equals(other.center)
			&& this.size.equals(other.size)
		);

		return returnValue;
	}

	public Box overwriteWith(Box other)
	{
		this.center.overwriteWith(other.center);
		this.size.overwriteWith(other.size);
		return this;
	}

	// String

	public String toString()
	{
		return this.min().toString() + ":" + this.max().toString();
	}

	// ShapeBase.

	public double dimensionForSurfaceClosestToPoint
	(
		Coords posToCheck, Coords displacementOverSizeHalf
	)
	{
		var greatestAbsoluteDisplacementDimensionSoFar = -1;
		var dimensionIndex = null;

		for (var d = 0; d < 3; d++) // dimension
		{
			var displacementDimensionOverSizeHalf
				= displacementOverSizeHalf.dimensionGet(d);
			var displacementDimensionOverSizeHalfAbsolute
				= Math.abs(displacementDimensionOverSizeHalf);

			if
			(
				displacementDimensionOverSizeHalfAbsolute
				> greatestAbsoluteDisplacementDimensionSoFar
			)
			{
				greatestAbsoluteDisplacementDimensionSoFar =
					displacementDimensionOverSizeHalfAbsolute;
				dimensionIndex = d;
			}
		}

		return dimensionIndex;
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		var displacementOverSizeHalf = normalOut.overwriteWith
		(
			posToCheck
		).subtract
		(
			this.center
		).divide
		(
			this.sizeHalf()
		);

		var dimensionIndex =
			this.dimensionForSurfaceClosestToPoint(posToCheck, displacementOverSizeHalf);

		var displacementDimensionOverSizeHalf
			= displacementOverSizeHalf.dimensionGet(dimensionIndex);

		var multiplier = (displacementDimensionOverSizeHalf > 0 ? 1 : -1);

		normalOut.clear().dimensionSet(dimensionIndex, 1).multiplyScalar(multiplier);

		return normalOut;
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut)
	{
		return boxOut.overwriteWith(this);
	}

	// transformable

	public Coords[] coordsGroupToTranslate()
	{
		return new Coords[] { this.center };
	}

	public Transformable transform(Transform transformToApply)
	{
		Transforms.applyTransformToCoordsMObject
		(
			transformToApply, this.coordsGroupToTranslate()
		);
		return this;
	}
}
