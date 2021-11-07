
package GameFramework.Model.Effects;

import java.util.*;
import java.util.stream.*;

import GameFramework.Display.Visuals.*;
import GameFramework.Model.*;

public class Effectable implements EntityProperty<Effectable>
{
	public List<Effect> effects;

	public Effectable(List<Effect> effects)
	{
		this.effects = (effects != null ? effects : new ArrayList<Effect>());
	}

	public void effectAdd(Effect effectToAdd)
	{
		this.effects.add(effectToAdd);
	}

	public Visual effectsAsVisual()
	{
		Visual returnValue =
		(
			this.effects.size() == 0
			? VisualNone.Instance
			: new VisualGroup
			(
				this.effects.stream().map(x -> x.visual).collect(Collectors.toList())
			)
		);
		return returnValue;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		for (var i = 0; i < this.effects.size(); i++)
		{
			var effect = this.effects.get(i);
			effect.updateForTimerTick(uwpe);
		}
		this.effects = this.effects.stream().filter
		(
			x -> x.isDone() == false
		).collect(Collectors.toList());
	}

	// Clonable.

	public Effectable clone()
	{
		return this;
	}

	public Effectable overwriteWith(Effectable other)
	{
		return this;
	}
}
