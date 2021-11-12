
package GameFramework.Display.Animation;

import java.util.*;
import java.util.stream.*;

import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class Animatable2<T extends Clonable<T> & Transformable<T>> implements Clonable<Animatable2<T>>, EntityProperty<Animatable2<T>>
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
		).collect
		(
			Collectors.toList()
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

	public T transformableReset()
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

	public Animatable2<T> clone()
	{
		return this; // todo
	}

	public Animatable2<T> overwriteWith(Animatable2<T> other)
	{
		return this; // todo
	}
}
