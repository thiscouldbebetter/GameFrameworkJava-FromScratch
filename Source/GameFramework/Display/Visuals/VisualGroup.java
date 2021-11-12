
package GameFramework.Display.Visuals;

import java.util.*;
import java.util.stream.*;

import GameFramework.Display.*;
import GameFramework.Display.Visuals.*;
import GameFramework.Helpers.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class VisualGroup implements Visual<VisualGroup>
{
	public Visual[] children;

	public VisualGroup(Visual[] children)
	{
		this.children = children;
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		for (var i = 0; i < this.children.length; i++)
		{
			var child = this.children[i];
			child.draw(uwpe, display);
		}
	}

	// Clonable.

	public VisualGroup clone()
	{
		return new VisualGroup
		(
			ArrayHelper.clone(this.children).toArray(new Visual[] {})
		);
	}

	public VisualGroup overwriteWith(VisualGroup other)
	{
		var otherAsVisualGroup = (VisualGroup)other;
		ArrayHelper.overwriteWith(this.children, otherAsVisualGroup.children);
		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { throw new Exception("todo"); }

	public VisualGroup transform(Transform transformToApply)
	{
		Arrays.asList(this.children).stream().forEach(x -> transformToApply.transform(x));
		return this;
	}
}
