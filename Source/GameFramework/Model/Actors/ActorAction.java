
package GameFramework.Model.Actors;

import java.util.function.*;

import GameFramework.Model.*;

public class ActorAction //
{
	public String name;
	private Consumer<UniverseWorldPlaceEntities> _perform;

	public ActorAction
	(
		String name,
		Consumer<UniverseWorldPlaceEntities> perform
	)
	{
		this.name = name;
		this._perform = perform;
	}

	public void perform(UniverseWorldPlaceEntities uwpe)
	{
		this._perform(uwpe);
	}

	public void performForUniverse(Universe universe)
	{
		this.perform(UniverseWorldPlaceEntities.fromUniverse(universe) );
	}

	private static Action_Instances _instances;
	public static Action_Instances Instances()
	{
		if (Action._instances == null)
		{
			Action._instances = new Action_Instances();
		}
		return Action._instances;
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
					universe, universe.display.sizeInPixels, actor,
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
