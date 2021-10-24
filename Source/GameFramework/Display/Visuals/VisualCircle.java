
package GameFramework.Display.Visuals;

import GameFramework.Display.*;

public class VisualCircle implements Visual
{
	public double radius;
	public Color colorFill;
	public Color colorBorder;
	public double borderThickness;

	public VisualCircle(double radius, Color colorFill, Color colorBorder, double borderThickness)
	{
		this.radius = radius;
		this.colorFill = colorFill;
		this.colorBorder = colorBorder;
		this.borderThickness = borderThickness;
	}

	public static VisualCircle fromRadiusAndColorFill(double radius, Color colorFill)
	{
		return new VisualCircle(radius, colorFill, null, null);
	}

	public static VisualCircle fromRadiusAndColors
	(
		double radius, Color colorFill, Color colorBorder
	)
	{
		return new VisualCircle(radius, colorFill, colorBorder, null);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		display.drawCircle
		(
			entity.locatable().loc.pos,
			this.radius,
			this.colorFill,
			this.colorBorder,
			this.borderThickness
		);
	}

	// Clonable.

	public Visual clone()
	{
		return new VisualCircle
		(
			this.radius, this.colorFill, this.colorBorder, this.borderThickness
		);
	}

	public Visual overwriteWith(Visual otherAsVisual)
	{
		var other = (VisualCircle)otherAsVisual;
		this.radius = other.radius;
		this.colorFill = other.colorFill;
		this.colorBorder = other.colorBorder;
		this.borderThickness = other.borderThickness;
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}
