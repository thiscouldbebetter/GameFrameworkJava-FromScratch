
package GameFramework.Display;

import GameFramework.Display.Visuals.*;
import GameFramework.Model.*;

public class Drawable implements Clonable<Drawable>, EntityProperty
{
	public Visual visual;
	public boolean isVisible;

	public Drawable(Visual visual, boolean isVisible)
	{
		this.visual = visual;
		this.isVisible = isVisible;
		if (this.isVisible == null)
		{
			this.isVisible = true;
		}
	}

	public static Drawable fromVisual(Visual visual)
	{
		return new Drawable(visual, null);
	}

	public static Drawable  fromVisualAndIsVisible(Visual visual, boolean isVisible)
	{
		return new Drawable(visual, isVisible);
	}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isVisible)
		{
			this.visual.draw(uwpe, uwpe.universe.display);
		}
	}

	// cloneable

	public Drawable clone()
	{
		return new Drawable(this.visual, this.isVisible);
	}

	public Drawable overwriteWith(Drawable other)
	{
		this.visual.overwriteWith(other.visual);
		this.isVisible = other.isVisible;
		return this;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

}
