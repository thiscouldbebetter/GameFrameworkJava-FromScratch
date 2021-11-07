
package GameFramework.Display.Animation;

import GameFramework.Helper.*:

public class AnimationKeyframe implements Interpolatable<AnimationKeyframe>
{
	public int frameIndex;
	public Transform_Interpolatable transforms[];
	public Map transformsByPropertyName<string, Transform_Interpolatable>;

	public AnimationKeyframe(int frameIndex, Transform transforms_Interpolatable[])
	{
		this.frameIndex = frameIndex;
		this.transforms = transforms;
		this.transformsByPropertyName = ArrayHelper.addLookups
		(
			this.transforms, (Transform_Interpolatable x) -> x.propertyName
		);
	}

	public interpolateWith(AnimationKeyframe other, double fractionOfProgressTowardOther)
	{
		var transformsInterpolated =
			new Transform_Interpolatable[this.transforms.length];

		for (var i = 0; i < this.transforms.length; i++)
		{
			var transformThis = this.transforms[i];
			var transformOther = other.transformsByPropertyName.get
			(
				transformThis.propertyName
			);

			var transformInterpolated = transformThis.interpolateWith
			(
				transformOther,
				fractionOfProgressTowardOther
			);

			transformsInterpolated.push(transformInterpolated);
		}

		var returnValue = new AnimationKeyframe
		(
			0, // frameIndex
			transformsInterpolated
		);

		return returnValue;
	}
}

}
