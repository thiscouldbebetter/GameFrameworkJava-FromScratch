
package GameFramework.Display.Visuals;

import GameFramework.Display.*;
import GameFramework.Model.*;

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
		var otherAsVisualGroup = other as VisualGroup;
		ArrayHelper.overwriteWith(this.children, otherAsVisualGroup.children);
		return this;
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		this.children.forEach(x => transformToApply.transform(x));
		return this;
	}
}