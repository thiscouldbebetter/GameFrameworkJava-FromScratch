
package GameFramework.Model.Items.Equipment;

import GameFramework.Model.*;

public class ItemCategory
{
	public String name;

	public ItemCategory(String name)
	{
		this.name = name;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}
	public void initialize(UniverseWorldPlaceEntities uwpe) {}
	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe) {}
}
