
package GameFramework.Geometry.Collisions;

import java.util.*;

import GameFramework.Geometry.Shapes.Maps.*;
import GameFramework.Model.*;

public class CollisionTrackerMapCell implements MapCell
{
	public List<Entity> entitiesPresent;

	public CollisionTrackerMapCell()
	{
		this.entitiesPresent = new ArrayList<Entity>();
	}
}
