
package GameFramework.Model;

import GameFramework.Controls.*;

public class VenueWorld implements Venue
{
	public String name;
	public World world;

	public VenueControls venueControls;

	public VenueWorld(World world)
	{
		this.name = "World";
		this.world = world;
	}

	public void draw(Universe universe)
	{
		this.world.draw(universe);
	}

	public void finalize(Universe universe)
	{
		universe.soundHelper.soundForMusic.pause(universe);
	}

	public void initialize(Universe universe)
	{
		universe.world = this.world;
		var uwpe = UniverseWorldPlaceEntities.fromUniverseAndWorld
		(
			universe, this.world
		);
		this.world.initialize(uwpe);

		var soundHelper = universe.soundHelper;
		soundHelper.soundWithNamePlayAsMusic(universe, "Music_Music");

		this.venueControls = new VenueControls
		(
			this.world.toControl(universe),
			true // ignoreKeyboardAndGamepadInputs
		);
	}

	public void updateForTimerTick(Universe universe)
	{
		var uwpe = UniverseWorldPlaceEntities.fromUniverseAndWorld
		(
			universe, this.world
		);
		this.world.updateForTimerTick(uwpe);
		this.draw(uwpe.universe);
		this.venueControls.updateForTimerTick(universe);
	}
}
