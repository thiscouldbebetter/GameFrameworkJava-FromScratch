
package GameFramework.Display.Visuals;

import java.util.stream.*;

import GameFramework.Display.*;
import GameFramework.Display.Visuals.*;
import GameFramework.Helpers.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class VisualGroup implements Visual
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

	public Visual clone()
	{
		return new VisualGroup(ArrayHelper.clone(this.children) );
	}

	public Visual overwriteWith(Visual other)
	{
		var otherAsVisualGroup = (VisualGroup)other;
		ArrayHelper.overwriteWith(this.children, otherAsVisualGroup.children);
		return this;
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { throw new Exception("todo"); }
	
	public Transformable transform(Transform transformToApply)
	{
		this.children.stream().forEach(x -> transformToApply.transform(x));
		return this;
	}
}
