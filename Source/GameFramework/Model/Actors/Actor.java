
package GameFramework.Model.Actors;

import java.util.*;

public class Actor implements EntityProperty
{
	public Activity activity;

	public List<Action> actions;

	public Actor(Activity activity)
	{
		this.activity = activity;
		this.actions = new List<Action>;
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

	public void clone()
	{
		return new Actor(this.activity.clone());
	}

	public void overwriteWith(Actor other)
	{
		this.activity.overwriteWith(other.activity);
		return this;
	}
}
