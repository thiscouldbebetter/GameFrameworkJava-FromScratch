package Model;

import Display.*;

public class Drawable implements EntityProperty
{
	public Visual visual;

	public Drawable(Visual visual)
	{
		this.visual = visual;
	}

	public static Drawable fromVisual(Visual visual)
	{
		return new Drawable(visual);
	}

	// Clonable.

	public EntityProperty cloneAsEntityProperty()
	{
		return this; // todo
	}

	// EntityProperty.

	public static String nameStatic() { return "Drawable"; }
	
	public String name() { return Drawable.nameStatic(); }

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		this.visual.draw(uwpe, uwpe.universe.display);
	}
}
