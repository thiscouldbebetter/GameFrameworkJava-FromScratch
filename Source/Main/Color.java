package Main;

public class Color
{
	public String name;
	public java.awt.Color systemColor;

	public Color(String name, java.awt.Color systemColor)
	{
		this.name = name;
		this.systemColor = systemColor;
	}

	private static Color_Instances _instances;
	public static Color_Instances Instances()
	{
		if (Color._instances == null)
		{
			Color._instances = new Color_Instances();
		}
		return Color._instances;
	}

	public static Color byName(String colorName)
	{
		return Color.Instances().Blue; // todo
	}
}

class Color_Instances
{
	public static Color Blue = new Color("Blue", java.awt.Color.BLUE);

	public static Color[] _All = new Color[]
	{
		Blue
	};
}
