
package GameFramework.Display.Visuals.Animation;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualAnimation implements Visual<VisualAnimation>
{
	public String name;
	public int[] ticksToHoldFrames;
	public Visual[] frames;
	public boolean isRepeating;

	public int ticksToComplete;

	public VisualAnimation
	(
		String name, int[] ticksToHoldFrames, Visual[] frames, boolean isRepeating
	)
	{
		this.name = name;
		this.ticksToHoldFrames = ticksToHoldFrames;
		this.frames = frames;
		this.isRepeating = isRepeating;

		if (this.ticksToHoldFrames == null)
		{
			this.ticksToHoldFrames = new int[this.frames.length];
			for (var f = 0; f < this.frames.length; f++)
			{
				this.ticksToHoldFrames[f] = 1;
			}
		}
		else if (this.ticksToHoldFrames.length < this.frames.length)
		{
			for (var f = 0; f < this.frames.length; f++)
			{
				if (f >= this.ticksToHoldFrames.length)
				{
					throw new Exception("todo");
					//this.ticksToHoldFrames.push(this.ticksToHoldFrames[f % this.ticksToHoldFrames.length]);
				}
			}
		}

		this.ticksToComplete = 0;
		for (var f = 0; f < this.ticksToHoldFrames.length; f++)
		{
			this.ticksToComplete += this.ticksToHoldFrames[f];
		}
	}

	public Visual frameCurrent(World world, int tickStarted)
	{
		var frameIndexCurrent = this.frameIndexCurrent(world, tickStarted);
		var frameCurrent = this.frames[frameIndexCurrent];
		return frameCurrent;
	}

	public int frameIndexCurrent(World world, int tickStarted)
	{
		var returnValue = -1;

		var ticksSinceStarted = world.timerTicksSoFar - tickStarted;

		if (ticksSinceStarted >= this.ticksToComplete)
		{
			if (this.isRepeating)
			{
				ticksSinceStarted = ticksSinceStarted % this.ticksToComplete;
			}
			else
			{
				returnValue = this.frames.length - 1;
			}
		}

		if (returnValue < 0)
		{
			var ticksForFramesSoFar = 0;
			var f = 0;
			for (f = 0; f < this.ticksToHoldFrames.length; f++)
			{
				var ticksToHoldFrame = this.ticksToHoldFrames[f];
				ticksForFramesSoFar += ticksToHoldFrame;
				if (ticksForFramesSoFar >= ticksSinceStarted)
				{
					break;
				}
			}
			returnValue = f;
		}

		return returnValue;
	}

	public boolean isComplete(World world, int tickStarted)
	{
		var ticksSinceStarted = world.timerTicksSoFar - tickStarted;
		var returnValue = (ticksSinceStarted >= this.ticksToComplete);
		return returnValue;
	}

	// Visual.

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var world = uwpe.world;
		var entity = uwpe.entity;

		var animatable = entity.animatable();
		var tickStarted = animatable.animationWithNameStartIfNecessary(this.name, world);
		var frameCurrent = this.frameCurrent(world, tickStarted);
		frameCurrent.draw(uwpe, display);
	}

	// Clonable.

	public VisualAnimation clone()
	{
		return this; // todo
	}

	public VisualAnimation overwriteWith(VisualAnimation other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupForTranslate() { return null; }

	public VisualAnimation transform(Transform transformToApply)
	{
		return this; // todo
	}
}
