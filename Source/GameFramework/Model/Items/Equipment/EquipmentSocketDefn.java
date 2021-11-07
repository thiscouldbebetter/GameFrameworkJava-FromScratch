
package GameFramework.Model.Items.Equipment;

import GameFramework.Utility.*;

public class EquipmentSocketDefn implements Namable
{
	public String _name;
	public String[] categoriesAllowedNames;

	public EquipmentSocketDefn(String name, String[] categoriesAllowedNames)
	{
		this._name = name;
		this.categoriesAllowedNames = categoriesAllowedNames;
	}

	// Namable.

	public String name()
	{
		return this._name;
	}
}
