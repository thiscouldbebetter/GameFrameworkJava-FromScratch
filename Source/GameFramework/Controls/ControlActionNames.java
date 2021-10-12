
package GameFramework.Controls;

class ControlActionNames_Instances
{
	public String ControlCancel;
	public String ControlConfirm;
	public String ControlDecrement;
	public String ControlIncrement;
	public String ControlNext;
	public String ControlPrev;

	public ControlActionNames_Instances()
	{
		this.ControlCancel = "ControlCancel";
		this.ControlConfirm = "ControlConfirm";
		this.ControlDecrement = "ControlDecrement";
		this.ControlIncrement = "ControlIncrement";
		this.ControlNext = "ControlNext";
		this.ControlPrev = "ControlPrev";
	}
}

public class ControlActionNames
{
	public static _instances: ControlActionNames_Instances;

	static Instances()
	{
		if (ControlActionNames._instances == null)
		{
			ControlActionNames._instances = new ControlActionNames_Instances();
		}

		return ControlActionNames._instances;
	}
}
