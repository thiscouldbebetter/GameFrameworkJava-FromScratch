
package GameFramework.Geometry.Collisions;

import java.util.*;

import GameFramework.Geometry.Shapes.Maps.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class CollisionTrackerMapCell implements Clonable<CollisionTrackerMapCell>, MapCell
{
	public List<Entity> entitiesPresent;

	public CollisionTrackerMapCell()
	{
		this.entitiesPresent = new ArrayList<Entity>();
	}

	// Clonable.
	public CollisionTrackerMapCell clone() { return this; }
	public CollisionTrackerMapCell overwriteWith(CollisionTrackerMapCell other) { return this; }

}
