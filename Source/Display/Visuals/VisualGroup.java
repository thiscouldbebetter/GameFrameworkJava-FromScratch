
package Display.Visuals;

import Display.*;
import Display.Visuals.*;
import Helpers.*;
import Geometry.*;
import Geometry.Transforms.*;
import Model.*;
import Utility.*;

import java.util.*;
import java.util.stream.*;

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
