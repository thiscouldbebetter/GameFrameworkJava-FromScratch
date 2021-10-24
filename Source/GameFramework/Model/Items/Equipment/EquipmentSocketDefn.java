
package GameFramework.Model.Items.Equipment;

public class EquipmentSocketDefn //
{
	public String name;
	public String[] categoriesAllowedNames;

	public EquipmentSocketDefn(String name, String[] categoriesAllowedNames)
	{
		this.name = name;
		this.categoriesAllowedNames = categoriesAllowedNames;
	}
}
