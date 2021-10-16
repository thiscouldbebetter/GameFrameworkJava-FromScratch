
package GameFramework.Controls;

import java.util.*;

import GameFramework.Display.*;
import GameFramework.Utility.*;

public class ControlStyle implements Namable
{
	public String _name;
	public Color colorBackground;
	public Color colorFill;
	public Color colorBorder;
	public Color colorDisabled;

	public ControlStyle
	(
		String name,
		Color colorBackground,
		Color colorFill,
		Color colorBorder,
		Color colorDisabled
	)
	{
		this.name = name;
		this.colorBackground = colorBackground;
		this.colorFill = colorFill;
		this.colorBorder = colorBorder;
		this.colorDisabled = colorDisabled;
	}

	private static ControlStyle_Instances _instances;
	public static ControlStyle_Instances Instances()
	{
		if (ControlStyle._instances == null)
		{
			ControlStyle._instances = new ControlStyle_Instances();
		}
		return ControlStyle._instances;
	}

	public static ControlStyle byName(String styleName)
	{
		return ControlStyle.Instances()._AllByName.get(styleName);
	}

	// Namable.

	public String name()
	{
		return this._name;
	}
}
