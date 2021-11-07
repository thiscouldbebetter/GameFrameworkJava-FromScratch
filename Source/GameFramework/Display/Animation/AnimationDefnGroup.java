
package GameFramework.Display.Animation;

import java.util.*;

import GameFramework.Helpers.*;

public class AnimationDefnGroup
{
	public String name;
	public AnimationDefn[] animationDefns;
	public Map<String,AnimationDefn> animationDefnsByName;

	public AnimationDefnGroup(String name, AnimationDefn[] animationDefns)
	{
		this.name = name;
		this.animationDefns = animationDefns;
		this.animationDefnsByName = ArrayHelper.addLookupsByName(this.animationDefns);
	}
}
