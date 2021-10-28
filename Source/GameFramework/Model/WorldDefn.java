
package GameFramework.Model;

import java.util.*;

import GameFramework.Model.Actors.*;
import GameFramework.Helpers.*;
import GameFramework.Model.Items.*;
import GameFramework.Model.Places.*;

public class WorldDefn
{
	public Map<String,Object[]> defnArraysByTypeName;
	public Map<String,Map<String, Object>> defnsByNameByTypeName;

	public WorldDefn(Object[][] defnArrays)
	{
		defnArrays = (defnArrays != null ? defnArrays : new Object[][] {});

		this.defnArraysByTypeName = new HashMap<String,Object[]>();
		this.defnsByNameByTypeName = new HashMap<String,Map<String,Object>>();

		for (var i = 0; i < defnArrays.length; i++)
		{
			var defnsOfType = defnArrays[i];
			var defnsByName = ArrayHelper.addLookupsByName(defnsOfType);
			if (defnsOfType.length > 0)
			{
				var itemFirst = defnsOfType[0];
				var itemTypeName = itemFirst.getClass().name;
				this.defnArraysByTypeName.put(itemTypeName, defnsOfType);
				this.defnsByNameByTypeName.put(itemTypeName, defnsByName);
			}
		}
	}

	public static WorldDefn _default()
	{
		return new WorldDefn(null);
	}

	// Convenience methods.

	public ActorAction actionByName(String defnName)
	{
		var defnsByName = this.defnsByNameByTypeName.get(Action.class.getName());
		var returnValue = ((ActorAction)(defnsByName.get(defnName)));
		return returnValue;
	}

	public ActivityDefn activityDefnByName(String defnName)
	{
		var defnsByName = this.defnsByNameByTypeName.get(ActivityDefn.class.getName());
		var returnValue = ((ActivityDefn)(defnsByName.get(defnName)));
		return returnValue;
	}

	public Entity entityDefnByName(String defnName)
	{
		var defnsByName = this.defnsByNameByTypeName.get(Entity.class.getName());
		var returnValue = ((Entity)(defnsByName.get(defnName)));
		return returnValue;
	}

	public ItemDefn itemDefnByName(String defnName)
	{
		var defnsByName = this.defnsByNameByTypeName.get(ItemDefn.class.getName());
		var returnValue = ((ItemDefn)(defnsByName.get(defnName)));
		return returnValue;
	}

	public PlaceDefn placeDefnByName(String defnName)
	{
		var defnsByName = this.defnsByNameByTypeName.get(PlaceDefn.class.getName());
		var returnValue = ((PlaceDefn)(defnsByName.get(defnName)));
		return returnValue;
	}

}
