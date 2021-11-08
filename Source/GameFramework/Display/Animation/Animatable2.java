
package GameFramework.Display.Animation;

import java.util.*;

import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class Animatable2<T extends Transformable> implements EntityProperty<Animatable2<T>>
{
	public AnimationDefnGroup animationDefnGroup;
	public T transformableAtRest;
	public T transformableTransformed;
	public Map<String,Integer> ticksStartedByAnimationName;

	public Animatable2
	(
		AnimationDefnGroup animationDefnGroup,
		T transformableAtRest,
		T transformableTransformed
	)
	{
		this.animationDefnGroup = animationDefnGroup;
		this.transformableAtRest = transformableAtRest;
		this.transformableTransformed = transformableTransformed;

		this.ticksStartedByAnimationName = new HashMap<String,Integer>();
	}

	public static Animatable2 create()
	{
		return new Animatable2(null, null, null);
	}

	public void animationStartByName(String name, World world)
	{
		if (this.ticksStartedByAnimationName.containsKey(name) == false)
		{
			this.ticksStartedByAnimationName.put(name, world.timerTicksSoFar);
		}
	}

	public void animationStopByName(String name)
	{
		this.ticksStartedByAnimationName.remove(name);
	}

	public Integer animationWithNameStartIfNecessary(String animationName, World world)
	{
		if (this.ticksStartedByAnimationName.containsKey(animationName) == false)
		{
			this.ticksStartedByAnimationName.put(animationName, world.timerTicksSoFar);
		}
		return this.ticksStartedByAnimationName.get(animationName);
	}

	public List<AnimationDefn> animationDefnsRunning()
	{
		var animationsRunningNames = this.animationsRunningNames();
		var returnValues = animationsRunningNames.stream().map
		(
			x -> this.animationDefnGroup.animationDefnsByName.get(x)
		);
		return returnValues;
	}

	public List<String> animationsRunningNames()
	{
		var animationsRunningNames = 
		this.ticksStartedByAnimationName.keySet().stream().filter
		(
			x -> this.ticksStartedByAnimationName.containsKey(x)
		).collect
		(
			Collectors.toList()
		);
		return animationsRunningNames;
	}

	public void animationsStopAll()
	{
		this.ticksStartedByAnimationName.clear();
	}

	public Transformable transformableReset()
	{
		this.transformableTransformed.overwriteWith(this.transformableAtRest);
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe){}
	public void initialize(UniverseWorldPlaceEntities uwpe){}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var world = uwpe.world;
		var animationDefnsRunning = this.animationDefnsRunning();
		for (var i = 0; i < animationDefnsRunning.size(); i++)
		{
			var animationDefn = animationDefnsRunning.get(i);
			var tickAnimationStarted =
				this.ticksStartedByAnimationName.get(animationDefn.name());
			var ticksSinceAnimationStarted =
				world.timerTicksSoFar - tickAnimationStarted;
			var transform = new Transform_Animate
			(
				animationDefn, ticksSinceAnimationStarted
			);
			this.transformableTransformed.overwriteWith
			(
				this.transformableAtRest
			);
			transform.transform(this.transformableTransformed);
		}
	}

	// Clonable.

	public Animatable2 clone()
	{
		return this; // todo
	}

	public Animatable2 overwriteWith(Animatable2 other)
	{
		return this; // todo
	}
}
