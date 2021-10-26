
package GameFramework.Display.Visuals;

public GameFramework.Display.*;
public GameFramework.Geometry.*;
public GameFramework.Geometry.Shapes.*;
public GameFramework.Geometry.Transforms.*;

public class VisualPolygon implements Visual
{
	public Path verticesAsPath;
	public Color colorFill;
	public Color colorBorder;
	public boolean shouldUseEntityOrientation;

	public Path verticesAsPathTransformed;
	public Transform_Locate transformLocate;

	public VisualPolygon
	(
		Path verticesAsPath, Color colorFill, Color colorBorder,
		boolean shouldUseEntityOrientation
	)
	{
		this.verticesAsPath = verticesAsPath;
		this.colorFill = colorFill;
		this.colorBorder = colorBorder;
		this.shouldUseEntityOrientation =
			shouldUseEntityOrientation;

		this.verticesAsPathTransformed = this.verticesAsPath.clone();
		this.transformLocate = new Transform_Locate(null);
	}

	public static VisualPolygon fromPathAndColorFill
	(
		Path path, Color colorFill
	)
	{
		var returnValue = new VisualPolygon
		(
			path, colorFill, null, null // shouldUseEntityOrientation
		);
		return returnValue;
	}

	public static VisualPolygon fromPathAndColors
	(
		Path verticesAsPath, Color colorFill, Color colorBorder
	)
	{
		return new VisualPolygon(verticesAsPath, colorFill, colorBorder, null);
	}

	public static VisualPolygon fromVerticesAndColorFill
	(
		Coords vertices[], Color colorFill
	)
	{
		var verticesAsPath = new Path(vertices);
		return VisualPolygon.fromPathAndColorFill(verticesAsPath, colorFill);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;

		var drawableLoc = entity.locatable().loc;

		this.transformLocate.loc.overwriteWith(drawableLoc);
		if (this.shouldUseEntityOrientation == false)
		{
			this.transformLocate.loc.orientation.default();
		}

		this.verticesAsPathTransformed.overwriteWith
		(
			this.verticesAsPath
		);

		Transforms.applyTransformToCoordsMany
		(
			this.transformLocate,
			this.verticesAsPathTransformed.points
		);

		display.drawPolygon
		(
			this.verticesAsPathTransformed.points,
			this.colorFill, this.colorBorder
		);
	}

	// Clonable.

	public Visual clone()
	{
		return new VisualPolygon
		(
			this.verticesAsPath.clone(),
			ClonableHelper.clone(this.colorFill),
			ClonableHelper.clone(this.colorBorder),
			this.shouldUseEntityOrientation
		);
	}

	public Visual overwriteWith(Visual otherAsVisual)
	{
		var otherAsVisualPolygon = (VisualPolygon)other;
		this.verticesAsPath.overwriteWith(otherAsVisualPolygon.verticesAsPath);
		if (this.colorFill != null)
		{
			this.colorFill.overwriteWith(otherAsVisualPolygon.colorFill);
		}
		if (this.colorBorder != null)
		{
			this.colorBorder.overwriteWith(otherAsVisualPolygon.colorBorder);
		}
		this.shouldUseEntityOrientation = other.shouldUseEntityOrientation;
		return this;
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		this.verticesAsPath.transform(transformToApply);
		return this;
	}
}