
package GameFramework.Display.Visuals;

import java.util.function.*;

import GameFramework.Display.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class VisualDirectional implements Visual
{
	public Visual visualForNoDirection;
	public Visual[] visualsForDirections;
	public Function<Entity,Double> _headingInTurnsGetForEntity;

	public int numberOfDirections;

	public VisualDirectional
	(
		Visual visualForNoDirection,
		Visual[] visualsForDirections,
		Function<Entity,Double> headingInTurnsGetForEntity
	)
	{
		this.visualForNoDirection = visualForNoDirection;
		this.visualsForDirections = visualsForDirections;
		this._headingInTurnsGetForEntity = headingInTurnsGetForEntity;

		this.numberOfDirections = this.visualsForDirections.length;
	}

	public static VisualDirectional fromVisuals
	(
		Visual visualForNoDirection, Visual[] visualsForDirections
	)
	{
		return new VisualDirectional
		(
			visualForNoDirection, visualsForDirections, null
		);
	}

	public double headingInTurnsGetForEntity(Entity entity)
	{
		double returnValue = 0;

		if (this._headingInTurnsGetForEntity == null)
		{
			var loc = entity.locatable().loc;
			returnValue = loc.orientation.forward.headingInTurns();
		}
		else
		{
			returnValue = this._headingInTurnsGetForEntity.apply(entity);
		}

		return returnValue;
	}

	// Visual.

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var entity = uwpe.entity;
		var headingInTurns = this.headingInTurnsGetForEntity(entity);
		Visual visualForHeading;

		if (headingInTurns < 0)
		{
			visualForHeading = this.visualForNoDirection;
		}
		else
		{
			var direction = NumberHelper.wrapToRangeMinMax
			(
				(int)Math.round
				(
					headingInTurns * this.numberOfDirections
				),
				0, this.numberOfDirections
			);
			visualForHeading = this.visualsForDirections[direction];
		}

		visualForHeading.draw(uwpe, display);
	}

	// Clonable.

	public Visual clone()
	{
		return this; // todo
	}

	public Visual overwriteWith(Visual other)
	{
		return this; // todo
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}
