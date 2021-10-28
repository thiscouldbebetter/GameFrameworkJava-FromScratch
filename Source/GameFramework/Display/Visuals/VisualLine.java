
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms*;
import GameFramework.Model.*;

public class VisualLine implements Visual
{
	public Coords fromPos;
	public Coords toPos;
	public Color color;
	public double lineThickness;

	private Coords _drawPosFrom;
	private Coords _drawPosTo;
	private Transform_Locate _transformLocate;

	public VisualLine
	(
		Coords fromPos, Coords toPos, Color color, double lineThickness
	)
	{
		this.fromPos = fromPos;
		this.toPos = toPos;
		this.color = color;
		this.lineThickness = lineThickness;

		// Helper variables.

		this._drawPosFrom = Coords.create();
		this._drawPosTo = Coords.create();
		this._transformLocate = new Transform_Locate(null);
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		var loc = entity.locatable().loc;
		this._transformLocate.loc = loc;

		var drawPosFrom = this._drawPosFrom.overwriteWith
		(
			this.fromPos
		);
		this._transformLocate.transformCoords(drawPosFrom);

		var drawPosTo = this._drawPosTo.overwriteWith
		(
			this.toPos
		);
		this._transformLocate.transformCoords(drawPosTo);

		display.drawLine
		(
			drawPosFrom, drawPosTo, this.color, this.lineThickness
		);
	}

	// Clonable.

	public Visual clone()
	{
		return new VisualLine
		(
			this.fromPos.clone(), this.toPos.clone(),
			this.color.clone(), this.lineThickness
		);
	}

	public Visual overwriteWith(Visual otherAsVisual)
	{
		var other = (VisualLine)otherAsVisual;
		this.fromPos.overwriteWith(other.fromPos);
		this.toPos.overwriteWith(other.toPos);
		this.color.overwriteWith(other.color);
		this.lineThickness = other.lineThickness;
		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }

	public Transformable transform(Transform transformToApply)
	{
		transformToApply.transformCoords(this.fromPos);
		transformToApply.transformCoords(this.toPos);
		return this;
	}
}
