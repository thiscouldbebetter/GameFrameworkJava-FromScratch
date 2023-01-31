package Model;

public class VenueWorld implements Venue
{
	public String name;
	public World world;

	// venueControls: VenueControls;

	public VenueWorld(World world)
	{
		this.name = "World";
		this.world = world;
	}

	public void draw(Universe universe)
	{
		var display = universe.display;

		display.clear();

		this.world.draw(universe);

		display.updateForTimerTick(universe);
	}

	public void finalize(Universe universe)
	{
		// todo
	}

	public void initialize(Universe universe)
	{
		universe.world = this.world;
		/*
		var uwpe = UniverseWorldPlaceEntities.fromUniverseAndWorld
		(
			universe, this.world
		);

		this.world.initialize(uwpe);

		this.venueControls = new VenueControls
		(
			this.world.toControl(universe),
			true // ignoreKeyboardAndGamepadInputs
		);
		*/
	}

	public void updateForTimerTick(Universe universe)
	{
		/*
		var uwpe = UniverseWorldPlaceEntities.fromUniverseAndWorld
		(
			universe, this.world
		);
		*/

		this.world.updateForTimerTick(universe);

		this.draw(universe);

		// this.venueControls.updateForTimerTick(universe);
	}
}
