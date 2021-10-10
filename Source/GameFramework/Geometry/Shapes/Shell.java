
package GameFramework.Geometry.Shapes;
{

public class Shell implements ShapeBase
{
	public Sphere sphereOuter;
	public double radiusInner;

	private Sphere sphereInner;

	private ShapeGroupAll _collider;

	public Shell(Sphere sphereOuter, double radiusInner)
	{
		this.sphereOuter = sphereOuter;
		this.radiusInner = radiusInner;

		this.sphereInner = new Sphere(this.sphereOuter.center, this.radiusInner);
		this._collider = new ShapeGroupAll
		(
			new ShapeBase[]
			{
				this.sphereOuter,
				new ShapeInverse(new ShapeContainer(this.sphereInner))
			}
		);
	}

	public static Shell _default()
	{
		var sphereOuter = Sphere._default();
		return new Shell(sphereOuter, sphereOuter.radius / 2);
	}

	public Coords center()
	{
		return this.sphereOuter.center;
	}

	public ShapeGroupAll collider()
	{
		return this._collider;
	}

	// cloneable

	public Shell clone()
	{
		return new Shell(this.sphereOuter.clone(), this.radiusInner);
	}

	public boolean equals(Shell other)
	{
		var returnValue =
		(
			this.sphereOuter.equals(other.sphereOuter)
			&& this.radiusInner == other.radiusInner
		);

		return returnValue;
	}

	public Shell overwriteWith(Shell other)
	{
		this.sphereOuter.overwriteWith(other.sphereOuter);
		this.radiusInner = other.radiusInner;
		return this;
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		return ShapeHelper.Instance().applyLocationToShapeDefault(loc, this);
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		var displacementFromCenter =
			normalOut.overwriteWith(posToCheck).subtract(this.center());
		var distanceFromCenter = displacementFromCenter.magnitude();
		var distanceFromSphereOuter =
			Math.abs(distanceFromCenter - this.sphereOuter.radius);
		var distanceFromSphereInner =
			Math.abs(distanceFromCenter - this.sphereInner.radius);
		// Note that normalOut == displacementFromCenter.
		if (distanceFromSphereInner < distanceFromSphereOuter)
		{
			normalOut.invert();
		}
		normalOut.normalize();
		return normalOut;
	}

	public Coords surfacePointNearPos
	(
		Coords posToCheck, Coords surfacePointOut
	)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut)
	{
		return this.sphereOuter.toBox(boxOut);
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		throw new Exception("Not implemented!");
	}
}
