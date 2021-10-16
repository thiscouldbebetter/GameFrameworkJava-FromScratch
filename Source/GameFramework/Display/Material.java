
package GameFramework.Display;

import GameFramework.Utility.*;

public class Material implements Namable
{
	public String _name;
	public Color colorStroke;
	public Color colorFill;
	public Texture texture;

	public Material(String name, Color colorStroke, Color colorFill, Texture texture)
	{
		this._name = name;
		this.colorStroke = colorStroke;
		this.colorFill = colorFill;
		this.texture = texture;
	}

	private static Material_Instances _instances;
	public static Material_Instances Instances()
	{
		if (Material._instances == null)
		{
			Material._instances = new Material_Instances();
		}

		return Material._instances;
	}

	// Namable.

	public String name()
	{
		return this._name;
	}
}
