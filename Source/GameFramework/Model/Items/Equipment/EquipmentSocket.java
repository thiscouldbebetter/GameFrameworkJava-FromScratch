
package GameFramework.Model.Items.Equipment;

import GameFramework.Model.*;

public class EquipmentSocket //
{
	public String defnName;
	public Entity itemEntityEquipped;

	public EquipmentSocket(String defnName, Entity itemEntityEquipped)
	{
		this.defnName = defnName;
		this.itemEntityEquipped = itemEntityEquipped;
	}

	public EquipmentSocketDefn defn(EquipmentSocketDefnGroup defnGroup)
	{
		return defnGroup.socketDefnsByName.get(this.defnName);
	}

	public String toString(World world)
	{
		var itemEntityEquippedAsString =
		(
			this.itemEntityEquipped == null
			? "[empty]"
			: this.itemEntityEquipped.item().toString(world)
		);
		var returnValue = this.defnName + ": " + itemEntityEquippedAsString;
		return returnValue;
	}

	public void unequip()
	{
		this.itemEntityEquipped = null;
	}
}
