
package GameFramework.Controls;

public class ControlActionNames
{
	public static ControlActionNames_Instances _instances;

	public static ControlActionNames_Instances Instances()
	{
		if (ControlActionNames._instances == null)
		{
			ControlActionNames._instances = new ControlActionNames_Instances();
		}

		return ControlActionNames._instances;
	}
}
