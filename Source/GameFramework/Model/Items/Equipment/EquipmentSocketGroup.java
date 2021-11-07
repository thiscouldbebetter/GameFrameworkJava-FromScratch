
package GameFramework.Model.Items.Equipment;

import java.util.*;

import GameFramework.Helpers.*;

public class EquipmentSocketGroup
{
	public EquipmentSocketDefnGroup defnGroup;
	public EquipmentSocket[] sockets;
	public Map<String, EquipmentSocket> socketsByDefnName;

	public EquipmentSocketGroup(EquipmentSocketDefnGroup defnGroup)
	{
		this.defnGroup = defnGroup;

		var socketDefns = this.defnGroup.socketDefns;
		this.sockets = new EquipmentSocket[socketDefns.length];

		for (var i = 0; i < socketDefns.length; i++)
		{
			var socketDefn = socketDefns[i];

			var socket = new EquipmentSocket(socketDefn.name, null);

			this.sockets[i] = socket;
		}

		this.socketsByDefnName = ArrayHelper.addLookups
		(
			this.sockets, (EquipmentSocket x) -> x.defnName
		);
	}
}
