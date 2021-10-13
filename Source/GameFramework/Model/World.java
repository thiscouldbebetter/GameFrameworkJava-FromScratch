
package GameFramework.Model;

import GameFramework.Model.Places.*;

public class World
{
	public String name;
	public Place[] places;

	public Place placeCurrent;

	public World(String name, Place[] places)
	{
		this.name = name;
		this.places = places;

		this.placeCurrent = this.places[0];
	}

	public void updateForTimerTick(Universe universe)
	{
		var uwpe = new UniverseWorldPlaceEntities
		(
			universe, this, this.placeCurrent, null, null
		);
		this.placeCurrent.updateForTimerTick(uwpe);
	}
}
