
package GameFramework;

import GameFramework.Geometry.*;

public class Locatable implements EntityProperty
{
	public Location loc;

	public Locatable(Location loc)
	{
		this.loc = loc;
	}

	public void updateForTimerTick
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		var loc = this.loc;
		var pos = loc.pos;
		var vel = loc.vel;

		pos.add(vel);
	}
}
