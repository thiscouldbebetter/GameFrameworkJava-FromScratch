
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualPath implements Visual<VisualPath>
{
	public Path verticesAsPath;
	public Color color;
	public double lineThickness;
	public boolean isClosed;

	public Path verticesAsPathTransformed;
	public Transform_Translate transformTranslate;

	public VisualPath
	(
		Path verticesAsPath, Color color, double lineThickness, boolean isClosed
	)
	{
		this.verticesAsPath = verticesAsPath;
		this.color = color;
		this.lineThickness = lineThickness;
		this.isClosed = isClosed;

		this.verticesAsPathTransformed = this.verticesAsPath.clone();
		this.transformTranslate = new Transform_Translate(Coords.create());
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		var drawablePos = entity.locatable().loc.pos;
		this.transformTranslate.displacement.overwriteWith(drawablePos);

		this.verticesAsPathTransformed.overwriteWith
		(
			this.verticesAsPath
		);

		Transforms.applyTransformToCoordsMany
		(
			this.transformTranslate,
			this.verticesAsPathTransformed.points
		);

		display.drawPath
		(
			this.verticesAsPathTransformed.points,
			this.color,
			this.lineThickness,
			this.isClosed
		);
	}

	// Clonable.

	public VisualPath clone()
	{
		return this; // todo
	}

	public VisualPath overwriteWith(VisualPath other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }
	
	public VisualPath transform(Transform transformToApply)
	{
		return this; // todo
	}
}
