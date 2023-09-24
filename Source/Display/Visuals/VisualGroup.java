
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
		var childrenCloned = new Visual[this.children.length];

		for (var i = 0; i < this.children.length; i++)
		{
			childrenCloned[i] = this.children[i].clone();
		}

		return new VisualGroup
		(
			childrenCloned
		);
	}

	public VisualGroup overwriteWith(VisualGroup other)
	{
		var otherAsVisualGroup = (VisualGroup)other;

		for (var i = 0; i < this.children.length; i++)
		{
			var otherChild = other.children[i];
			this.children[i].overwriteWith(otherChild);
		}

		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return new Coords[] {}; }

	public VisualGroup transform(Transform transformToApply)
	{
		Arrays.asList(this.children).stream().forEach(x -> transformToApply.transform(x));
		return this;
	}
}
