
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
		this.placeCurrent.updateForTimerTick(universe, this);
	}
}
