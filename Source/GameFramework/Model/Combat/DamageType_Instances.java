package GameFramework.Model.Combat;

import java.util.*;

import GameFramework.Helpers.*;

public class DamageType_Instances
{
	DamageType _Unspecified;
	DamageType Cold;
	DamageType Heat;
	
	public DamageType _All[];
	public Map<String,DamageType> _AllByName;

	public DamageType_Instances()
	{
		this._Unspecified = new DamageType("Unspecified");
		this.Cold = new DamageType("Cold");
		this.Heat = new DamageType("Heat");

		this._All = new DamageType[]
		{
			this._Unspecified,
			this.Cold,
			this.Heat
		};

		this._AllByName = ArrayHelper.addLookupsByName(this._All);
	}
}
