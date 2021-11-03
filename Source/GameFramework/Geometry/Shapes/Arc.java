
package GameFramework.Geometry.Shapes;
{

public class Arc implements ShapeBase<Arc>
{
	public Shell shell;
	public Wedge wedge;

	private ShapeGroupAll _collider;

	public Arc(Shell shellWedge , wedge)
	{
		this.shell = shell;
		this.wedge = wedge;

		this._collider = new ShapeGroupAll
		(
			new Shape[]
			{
				this.shell,
				this.wedge
			}
		);
	}

	public static Arc _default()
	{
		return new Arc(Shell._default(), Wedge._default() );
	}

	public static Arc fromShellAndWedge(Shell shellWedge, wedge)
	{
		return new Arc(shell, wedge);
	}

	public Coords center()
	{
		return this.shell.center();
	}

	public ShapeGroupAll collider()
	{
		return this._collider;
	}

	// cloneable

	public Arc clone()
	{
		return new Arc(this.shell.clone(), this.wedge.clone());
	}

	public boolean equals(Arc other)
	{
		var returnValue =
		(
			this.shell.equals(other.shell)
			&& this.wedge.equals(other.wedge)
		);

		return returnValue;
	}

	public Arc overwriteWith(Arc other)
	{
		this.shell.overwriteWith(other.shell);
		this.wedge.overwriteWith(other.wedge);
		return this;
	}

	// Transformable.

	public Coords coordsGroupToTranslate()
	{
		return new Coords[] { this.shell.center(), this.wedge.vertex };
	}

	// ShapeBase.

	public ShapeBase locate(Disposition loc)
	{
		var directionMin = this.wedge.directionMin;
		directionMin.overwriteWith(loc.orientation.forward);
		return ShapeHelper.Instance().applyLocationToShapeDefault(loc, this);
	}

	public Coords normalAtPos(Coords posToCheck, Coords normalOut)
	{
		return this.shell.normalAtPos(posToCheck, normalOut);
	}

	public Coords surfacePointNearPos(Coords posToCheck, Coords surfacePointOut)
	{
		return surfacePointOut.overwriteWith(posToCheck); // todo
	}

	public Box toBox(Box boxOut)
	{
		return this.shell.toBox(boxOut);
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		throw new Error("Not implemented!");
	}
}

}
