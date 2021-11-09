
package GameFramework.Model.Actors;

import java.util.*;

import GameFramework.Model.*;

public class Activity
{
	public String defnName;
	public Map<String,Object> targetsByName;

	public Activity(String defnName, Map<String,Object> targetsByName)
	{
		this.defnName = defnName;
		this.targetsByName =
		(
			targetsByName != null
			? targetsByName
			: new HashMap<String,Object>()
		);
	}

	public static Activity fromDefnName(String defnName)
	{
		return new Activity(defnName, null);
	}

	public static Activity fromDefnNameAndTarget(String defnName, Object target)
	{
		return new Activity
		(
			defnName,
			new HashMap<String,Object>()
			{{
				put(defnName, target);
			}}
		);
	}

	public Activity clear()
	{
		this.defnName = ActivityDefn.Instances().DoNothing.name();
		this.targetClear();
		return this;
	}

	public ActivityDefn defn(World world)
	{
		return world.defn.activityDefnByName(this.defnName);
	}

	public Activity defnNameAndTargetSet(String defnName, Object target)
	{
		this.defnName = defnName;
		this.targetSet(target);
		return this;
	}

	public void perform(UniverseWorldPlaceEntities uwpe)
	{
		if (this.defnName != null)
		{
			var defn = this.defn(uwpe.world);
			defn.perform(uwpe);
		}
	}

	public Object target()
	{
		return this.targetByName(this.defnName);
	}

	public Object targetByName(String targetName)
	{
		return this.targetsByName.get(targetName);
	}

	public Activity targetClear()
	{
		this.targetClearByName(this.defnName);
		return this;
	}

	public Activity targetClearByName(String name)
	{
		this.targetsByName.remove(name);
		return this;
	}

	public Activity targetSet(Object value)
	{
		this.targetSetByName(this.defnName, value);
		return this;
	}

	public Activity targetSetByName(String name, Object value)
	{
		this.targetsByName.put(name, value);
		return this;
	}

	// Clonable.

	public Activity clone()
	{
		return Activity.fromDefnName(this.defnName);
	}

	public Activity overwriteWith(Activity other)
	{
		this.defnName = other.defnName;
		return this;
	}
}
