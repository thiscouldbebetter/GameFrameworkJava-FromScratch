
package GameFramework.Utility;

import java.util.function.*;

import GameFramework.Model.*;

public class VenueTask implements Venue
{
	public Venue venueInner;
	public Consumer<Universe> perform;
	public BiConsumer<Universe,Object> done;

	public DateTime timeStarted;

	public VenueTask
	(
		Venue venueInner, Consumer<Universe> perform, BiConsumer<Universe,Object> done
	)
	{
		this.venueInner = venueInner;
		this.perform = perform;
		this.done = done;

		this.timeStarted = null;
	}

	public double secondsSinceStarted()
	{
		var returnValue = 0
		if (this.timeStarted != null)
		{
			var now = new Date();
			var millisecondsSinceStarted = now.getTime() - this.timeStarted.getTime();
			returnValue = Math.floor(millisecondsSinceStarted / 1000);
		}
		return returnValue;
	}

	// Venue implementation.

	public void draw(Universe universe)
	{
		this.venueInner.draw(universe);
	}

	public void finalize(Universe universe) {}

	public void initialize(Universe universe) {}

	public void updateForTimerTick(Universe universe)
	{
		this.venueInner.updateForTimerTick(universe);

		this.timeStarted = new Date();

		var timer = setInterval
		(
			() -> { this.draw(universe), 1000 }
		)

		// todo - Make this asynchronous.
		var result = this.perform(universe);

		clearInterval(timer);

		this.done.apply(universe, result);
	}
}
