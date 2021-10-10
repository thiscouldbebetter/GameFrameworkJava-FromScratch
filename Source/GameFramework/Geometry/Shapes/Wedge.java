
package GameFramework.Geometry.Shapes;
{

public class Wedge implements ShapeBase
{
	public Coords vertex;
	public Coords directionMin;
	public double angleSpannedInTurns;

	private Polar rayDirectionMinAsPolar;
	private Polar rayDirectionMaxAsPolar;
	private Coords rayDirectionMin;
	private Coords rayDirectionMax;
	private Coords downFromVertex;
	private Coords directionMinFromVertex;
	private Coords directionMaxFromVertex;
	private Plane planeForAngleMin;
	private Plane planeForAngleMax;
	private Hemispace hemispaces[];
	private ShapeGroupAll shapeGroupAll;
	private ShapeGroupAny shapeGroupAny;

	private Object _collider;

	public Wedge
	(
		Coords vertexCoords, double directionMin, double angleSpannedInTurns
	)
	{
		this.vertex = vertex;
		this.directionMin = directionMin;
		this.angleSpannedInTurns = angleSpannedInTurns;

		// Helper variable.
		this.rayDirectionMinAsPolar = new Polar(0, 1, 0);
	}

	public static Wedge _default()
	{
		return new Wedge
		(
			Coords.create(), // vertex
			new Coords(1, 0, 0), // directionMin
			.5 // angleSpannedInTurns
		);
	}

	public RangeExtent angleAsRangeExtent()
	{
		var angleStartInTurns = this.directionMin.headingInTurns();
		return new RangeExtent
		(
			angleStartInTurns,
			angleStartInTurns + this.angleSpannedInTurns
		);
	}

	public double angleInTurnsMax()
	{
		var returnValue = NumberHelper.wrapToRangeMinMax
		(
			this.angleInTurnsMin() + this.angleSpannedInTurns,
			0, 1
		);

		return returnValue;
	}

	public double angleInTurnsMin()
	{
		return this.rayDirectionMinAsPolar.fromCoords
		(
			this.directionMin
		).azimuthInTurns;
	}

	public Object collider()
	{
		if (this._collider == null)
		{
			this.rayDirectionMinAsPolar = new Polar(0, 1, 0);
			this.rayDirectionMaxAsPolar = new Polar(0, 1, 0);
			this.rayDirectionMin = Coords.create();
			this.rayDirectionMax = Coords.create();
			this.downFromVertex = Coords.create();
			this.directionMinFromVertex = Coords.create();
			this.directionMaxFromVertex = Coords.create();
			this.planeForAngleMin = new Plane(Coords.create(), 0);
			this.planeForAngleMax = new Plane(Coords.create(), 0);
			this.hemispaces = new Hemispace[]
			{
				new Hemispace(this.planeForAngleMin),
				new Hemispace(this.planeForAngleMax)
			};
			this.shapeGroupAll = new ShapeGroupAll(this.hemispaces);
			this.shapeGroupAny = new ShapeGroupAny(this.hemispaces);
		}

		var angleInTurnsMin = this.angleInTurnsMin();
		var angleInTurnsMax = this.angleInTurnsMax();

		this.rayDirectionMinAsPolar.azimuthInTurns = angleInTurnsMin;
		this.rayDirectionMinAsPolar.toCoords(this.rayDirectionMin);
		this.rayDirectionMaxAsPolar.azimuthInTurns = angleInTurnsMax;
		this.rayDirectionMaxAsPolar.toCoords(this.rayDirectionMax);

		var down = Coords.Instances().ZeroZeroOne;

		this.downFromVertex.overwriteWith
		(
			this.vertex
		).add
		(
			down
		);

		this.directionMinFromVertex.overwriteWith
		(
			this.vertex
		).add
		(
			this.rayDirectionMin
		);

		this.directionMaxFromVertex.overwriteWith
		(
			this.vertex
		).add
		(
			this.rayDirectionMax
		);

		this.planeForAngleMin.fromPoints
		(
			// Order matters!
			this.vertex, 
			this.directionMinFromVertex,
			this.downFromVertex
		);

		this.planeForAngleMax.fromPoints
		(
			this.vertex, 
			this.downFromVertex,
			this.directionMaxFromVertex
		);

		if (this.angleSpannedInTurns < .5)
		{
			this._collider = this.shapeGroupAll;
		}
		else
		{
			this._collider = this.shapeGroupAny;
		}

		return this._collider;
	}

	// Clonable.

	public Wedge clone()
	{
		return new Wedge
		(
			this.vertex.clone(), this.directionMin.clone(), this.angleSpannedInTurns
		);
	}

	public boolean equals(Wedge other)
	{
		var returnValue =
		(
			this.vertex.equals(other.vertex)
			&& this.directionMin.equals(other.directionMin)
			&& this.angleSpannedInTurns == other.angleSpannedInTurns
		);

		return returnValue;
	}

	public Wedge overwriteWith(Wedge other)
	{
		this.vertex.overwriteWith(other.vertex);
		this.directionMin.overwriteWith(other.directionMin);
		this.angleSpannedInTurns = other.angleSpannedInTurns;
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		this.vertex.overwriteWith(loc.pos);
		return this;
	}

	public Coords normalAtPos(Coords posToCheckCoords , normalOut)
	{
		throw new Exception("Not implemented!");
	}

	public Coords surfacePointNearPos(Coords posToCheckCoords , surfacePointOut)
	{
		throw new Exception("Not implemented!");
	}

	public Box toBox(Box boxOut) { throw new Exception("Not implemented!"); }

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}

}
