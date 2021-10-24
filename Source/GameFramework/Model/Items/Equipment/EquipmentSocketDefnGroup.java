
package GameFramework.Model.Items.Equipment;

import java.util.*;

import GameFramework.Helpers.*;

public class EquipmentSocketDefnGroup
{
	public String name;
	public EquipmentSocketDefn[] socketDefns;
	public Map<String,EquipmentSocketDefn> socketDefnsByName;

	public EquipmentSocketDefnGroup(String name, EquipmentSocketDefn[] socketDefns)
	{
		this.name = name;
		this.socketDefns = socketDefns;
		this.socketDefnsByName = ArrayHelper.addLookupsByName(this.socketDefns);
	}
}
