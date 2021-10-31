
package GameFramework.Model.Actors;

import java.util.function.*;

import GameFramework.Display.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class ActorAction implements Namable
{
	private String _name;
	private Consumer<UniverseWorldPlaceEntities> _perform;

	public ActorAction
	(
		String name,
		Consumer<UniverseWorldPlaceEntities> perform
	)
	{
		this._name = name;
		this._perform = perform;
	}

	public void perform(UniverseWorldPlaceEntities uwpe)
	{
		this._perform.accept(uwpe);
	}

	public void performForUniverse(Universe universe)
	{
		this.perform(UniverseWorldPlaceEntities.fromUniverse(universe) );
	}

	private static ActorAction_Instances _instances;
	public static ActorAction_Instances Instances()
	{
		if (ActorAction._instances == null)
		{
			ActorAction._instances = new ActorAction_Instances();
		}
		return ActorAction._instances;
	}
	
	// Namable.
	
	public String name()
	{
		return this._name;
	}

}

class ActorAction_Instances
{
	public ActorAction DoNothing;
	public ActorAction ShowMenuPlayer;
	public ActorAction ShowMenuSettings;

	public ActorAction_Instances()
	{
		this.DoNothing = new ActorAction
		(
			"DoNothing",
			(UniverseWorldPlaceEntities uwpe) ->
			{	
				// Do nothing.
			}
		);

		this.ShowMenuPlayer = new ActorAction
		(
			"ShowMenuPlayer",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var universe = uwpe.universe;
				var actor = uwpe.entity;
				var control = actor.controllable().toControl
				(
					universe, universe.display.sizeInPixels(), actor,
					universe.venueCurrent, true
				);
				Venue venueNext = control.toVenue();
				venueNext = VenueFader.fromVenuesToAndFrom
				(
					venueNext, universe.venueCurrent
				);
				universe.venueNext = venueNext;
			}
		);

		this.ShowMenuSettings = new ActorAction
		(
			"ShowMenuSettings",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var universe = uwpe.universe;
				var controlBuilder = universe.controlBuilder;
				var control = controlBuilder.gameAndSettings1(universe);
				Venue venueNext = control.toVenue();
				venueNext = VenueFader.fromVenuesToAndFrom
				(
					venueNext, universe.venueCurrent
				);
				universe.venueNext = venueNext;
			}
		);
	}
}
