
package GameFramework.Display.Animation;

public class Transform_Animate
{
	public AnimationDefn animationDefn;
	public int ticksSinceStarted;

	public Transform_Animate(AnimationDefn animationDefn, int ticksSinceStarted)
	{
		this.animationDefn = animationDefn;
		this.ticksSinceStarted = ticksSinceStarted;
	}

	public AnimationKeyframe frameCurrent()
	{
		var returnValue = null;

		var animationDefn = this.animationDefn;

		var framesSinceBeginningOfCycle =
			this.ticksSinceStarted
			% animationDefn.intOfFramesTotal;

		var i;

		var keyframes = animationDefn.keyframes;
		for (i = keyframes.length - 1; i >= 0; i--)
		{
			keyframe = keyframes[i];

			if (keyframe.frameIndex <= framesSinceBeginningOfCycle)
			{
				break;
			}
		}

		var keyframe = keyframes[i];
		var framesSinceKeyframe =
			framesSinceBeginningOfCycle - keyframe.frameIndex;

		var keyframeNext = keyframes[i + 1];
		var intOfFrames =
			keyframeNext.frameIndex - keyframe.frameIndex;
		var fractionOfProgressFromKeyframeToNext =
			framesSinceKeyframe / intOfFrames;

		returnValue = keyframe.interpolateWith
		(
			keyframeNext,
			fractionOfProgressFromKeyframeToNext
		);

		return returnValue;
	}

	public Transform_Animate overwriteWith(Transform_Animate other)
	{
		return this; // todo
	}

	public transform(Transformable transformable)
	{
		var frameCurrent = this.frameCurrent();

		var transforms = frameCurrent.transforms;

		for (var i = 0; i < transforms.length; i++)
		{
			var transformToApply = transforms[i];
			transformToApply.transform(transformable);
		}

		return transformable;
	}

	public Coords transformCoords(Coords coordsToTransform)
	{
		return coordsToTransform;
	}
}
