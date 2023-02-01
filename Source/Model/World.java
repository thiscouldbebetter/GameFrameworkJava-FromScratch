package Model;

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

	public void draw(Universe universe)
	{
		if (this.placeCurrent != null)
		{
			this.placeCurrent.draw(universe, this, universe.display);
		}
	}

	public Venue toVenue(Universe universe)
	{
		return new VenueWorld(this);
	}

	public void updateForTimerTick(Universe universe)
	{
		this.placeCurrent.updateForTimerTick(universe, this);
	}
}
