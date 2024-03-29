package Main;

import Geometry.*;
import Model.*;

public class Locatable implements EntityProperty
{
	public Location loc;

	public Locatable(Location loc)
	{
		this.loc = loc;
	}

	// EntityProperty

	public static String nameStatic() { return "Locatable"; }

	public String name() { return Locatable.nameStatic(); }

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var loc = this.loc;
		var pos = loc.pos;
		var vel = loc.vel;

		pos.add(vel);
	}
}
