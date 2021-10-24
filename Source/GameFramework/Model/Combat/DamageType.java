
package GameFramework.Model.Combat;

public class DamageType
{
	public String name;

	public DamageType(String name)
	{
		this.name = name;
	}

	static DamageType_Instances instances;
	static Instances()
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
}
