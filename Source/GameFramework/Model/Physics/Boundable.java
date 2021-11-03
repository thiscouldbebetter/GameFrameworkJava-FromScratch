
package GameFramework.Model.Physics;

import GameFramework.Geometry.Shapes.*;
import GameFramework.Model.*;

public class Boundable implements EntityProperty<Boundable>
{
	public ShapeBase bounds;

	public Boundable(ShapeBase bounds)
	{
		this.bounds = bounds;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}

	public void initialize(UniverseWorldPlaceEntities uwpe)
	{
		this.updateForTimerTick(uwpe);
	}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var e = uwpe.entity;
		this.bounds.locate(e.locatable().loc);
	}

	// Clonable.

	public Boundable clone()
	{
		return new Boundable(this.bounds.clone());
	}

	public EntityProperty overwriteWith(EntityProperty otherAsProperty)
	{
		var other = (Boundable)otherAsProperty;
		this.bounds.overwriteWith(other.bounds);
		return this;
	}
}
