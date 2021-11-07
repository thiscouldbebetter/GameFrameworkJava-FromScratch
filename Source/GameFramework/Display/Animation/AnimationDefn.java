
package GameFramework.Display.Animation;

import java.util.*:

import GameFramework.Utility.*;

public class AnimationDefn implements Namable
{
	public String name;
	public AnimationKeyframe[] keyframes;
	public int intOfFramesTotal;

	public AnimationDefn(String name, AnimationKeyframe keyframes[])
	{
		this._name = name;
		this.keyframes = keyframes;

		this.intOfFramesTotal =
			this.keyframes[this.keyframes.length - 1].frameIndex
			- this.keyframes[0].frameIndex;

		this.propagateTransformsToAllKeyframes();
	}

	public void propagateTransformsToAllKeyframes()
	{
		var propertyNamesAll = new ArrayList<String>();
		var propertyNameLookup = new HashMap<String,String>();

		for (var f = 0; f < this.keyframes.length; f++)
		{
			var keyframe = this.keyframes[f];
			var transforms = keyframe.transforms;

			for (var t = 0; t < transforms.length; t++)
			{
				var transform = transforms[t];
				var propertyName = transform.propertyName;
				if (propertyNameLookup.get(propertyName) == null)
				{
					propertyNameLookup.put(propertyName, propertyName);
					propertyNamesAll.add(propertyName);
				}
			}
		}

		keyframe = null;
		var keyframePrev = null;

		for (var f = 0; f < this.keyframes.length; f++)
		{
			keyframePrev = keyframe;
			keyframe = this.keyframes[f];

			var transformsByPropertyName = keyframe.transformsByPropertyName;

			for (var p = 0; p < propertyNamesAll.length; p++)
			{
				var propertyName = propertyNamesAll[p];
				if (transformsByPropertyName.get(propertyName) == null)
				{
					var keyframeNext = null;

					for (var g = f + 1; g < this.keyframes.length; g++)
					{
						var keyframeFuture = this.keyframes[g];
						var transformFuture = keyframeFuture.transformsByPropertyName.get(propertyName);
						if (transformFuture != null)
						{
							keyframeNext = keyframeFuture;
							break;
						}
					}

					if (keyframePrev != null && keyframeNext != null)
					{
						var transformPrev = keyframePrev.transformsByPropertyName.get(propertyName);
						var transformNext = keyframeNext.transformsByPropertyName.get(propertyName);

						var intOfFramesFromPrevToNext =
							keyframeNext.frameIndex
							- keyframePrev.frameIndex;

						var intOfFramesFromPrevToCurrent =
							keyframe.frameIndex
							- keyframePrev.frameIndex;

						var fractionOfProgressFromPrevToNext =
							intOfFramesFromPrevToCurrent
							/ intOfFramesFromPrevToNext;

						var transformNew = transformPrev.interpolateWith
						(
							transformNext,
							fractionOfProgressFromPrevToNext
						);
						transformsByPropertyName.set(propertyName, transformNew);
						keyframe.transforms.add(transformNew);
					}
				}
			}
		}
	}

	// Namable.

	public String name() { return this._name; }
}
