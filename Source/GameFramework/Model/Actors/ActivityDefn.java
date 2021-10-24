
package GameFramework.Model.Actors;

import java.util.*;
import java.util.function.*;

import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class ActivityDefn
{
	public String name;
	private Consumer<UniverseWorldPlaceEntities> _perform;

	public ActivityDefn
	(
		String name,
		Consumer<UniverseWorldPlaceEntities> perform
	)
	{
		this.name = name;
		this._perform = perform;
	}

	private static ActivityDefn_Instances _instances;
	public static ActivityDefn_Instances Instances()
	{
		if (ActivityDefn._instances == null)
		{
			ActivityDefn._instances = new ActivityDefn_Instances();
		}
		return ActivityDefn._instances;
	}

	public void perform(UniverseWorldPlaceEntities uwpe)
	{
		this._perform(uwpe);
	}
}

class ActivityDefn_Instances
{
	public ActivityDefn[] _All;
	public Map<String,ActivityDefn> _AllByName;

	public ActivityDefn DoNothing;
	public ActivityDefn Simultaneous;

	public ActivityDefn_Instances()
	{
		this.DoNothing = new ActivityDefn
		(
			"DoNothing",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{}
		);

		this.Simultaneous = new ActivityDefn
		(
			"Simultaneous",
			// perform
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var w = uwpe.world;
				var e = uwpe.entity;
				var activity = e.actor().activity;
				var childDefnNames = (String[])activity.target();
				for (var i = 0; i < childDefnNames.length; i++)
				{
					var childDefnName = childDefnNames[i];
					var childDefn = w.defn.activityDefnByName(childDefnName);
					childDefn.perform(uwpe);
				}
			}
		);

		this._All = new ActivityDefn[]
		{
			this.DoNothing,
			this.Simultaneous
		};

		this._AllByName = ArrayHelper.addLookupsByName(this._All);
	}
}
