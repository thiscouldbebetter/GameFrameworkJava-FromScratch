
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualCrosshairs implements Visual<VisualCrosshairs>
{
	public int numberOfLines;
	public double radiusOuter;
	public double radiusInner;
	public Color color;
	public double lineThickness;

	public VisualCrosshairs
	(
		Integer numberOfLines,
		Double radiusOuter,
		Double radiusInner,
		Color color,
		Double lineThickness
	)
	{
		this.numberOfLines = (numberOfLines != null ? numberOfLines : 4);
		this.radiusOuter = (radiusOuter != null ? radiusOuter : 10);
		this.radiusInner = (radiusInner != null ? radiusInner : (this.radiusOuter / 2));
		this.color = (color != null ? color : Color.Instances().White);
		this.lineThickness = (lineThickness != null ? lineThickness : 1);
	}

	public static VisualCrosshairs fromRadiiOuterAndInner
	(
		double radiusOuter, double radiusInner
	)
	{
		return new VisualCrosshairs(null, radiusOuter, radiusInner, null, null);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		display.drawCrosshairs
		(
			entity.locatable().loc.pos,
			this.numberOfLines,
			this.radiusOuter,
			this.radiusInner,
			this.color,
			this.lineThickness
		);
	}

	// Clonable.

	public VisualCrosshairs clone()
	{
		return new VisualCrosshairs
		(
			this.numberOfLines, this.radiusOuter, this.radiusInner,
			this.color, this.lineThickness
		);
	}

	public VisualCrosshairs overwriteWith(VisualCrosshairs other)
	{
		this.numberOfLines = other.numberOfLines;
		this.radiusOuter = other.radiusOuter;
		this.radiusInner = other.radiusInner;
		this.color = other.color;
		this.lineThickness = other.lineThickness;
		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public VisualCrosshairs transform(Transform transformToApply)
	{
		return this; // todo
	}
}
