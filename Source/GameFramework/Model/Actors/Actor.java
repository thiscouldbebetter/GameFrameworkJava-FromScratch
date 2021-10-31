
package GameFramework.Model.Actors;

import java.util.*;

import GameFramework.Model.*;

public class Actor implements EntityProperty
{
	public Activity activity;

	public List<ActorAction> actions;

	public Actor(Activity activity)
	{
		this.activity = activity;
		this.actions = new ArrayList<ActorAction>();
	}

	public static Actor create()
	{
		return Actor.fromActivityDefnName
		(
			ActivityDefn.Instances().DoNothing.name
		);
	}

	public static Actor fromActivityDefnName(String activityDefnName)
	{
		var activity = Activity.fromDefnName(activityDefnName);
		var returnValue = new Actor(activity);
		return returnValue;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		this.activity.perform(uwpe);
	}

	// Clonable.

	public Actor clone()
	{
		return new Actor(this.activity.clone());
	}

	public Actor overwriteWith(Actor other)
	{
		this.activity.overwriteWith(other.activity);
		return this;
	}
}
