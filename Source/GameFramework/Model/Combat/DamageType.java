
package GameFramework.Model.Combat;

import GameFramework.Utility.*;

public class DamageType implements Namable
{
	public String _name;

	public DamageType(String name)
	{
		this._name = name;
	}

	private static DamageType_Instances _instances;
	public static DamageType_Instances Instances()
	{
		if (DamageType._instances == null)
		{
			DamageType._instances = new DamageType_Instances();
		}
		return DamageType._instances;
	}

	public static DamageType byName(String name)
	{
		var damageTypes = DamageType.Instances();
		var damageType = damageTypes._AllByName.get(name);
		damageType = (damageType != null ? damageType : damageTypes._Unspecified);
		return damageType;
	}

	// Namable.

	public String name()
	{
		return this._name;
	}
}
