
package GameFramework.Controls;

import java.util.*;

import GameFramework.Display.*;

public class ControlStyle
{
	public String name;
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
}

class ControlStyle_Instances
{
	public ControlStyle Default;
	public ControlStyle Dark;

	public List<ControlStyle> _All;
	public Map<String,ControlStyle> _AllByName;

	public ControlStyle_Instances()
	{
		this.Default = new ControlStyle
		(
			"Default", // name
			Color.fromRGB(240/255, 240/255, 240/255), // colorBackground
			Color.byName("White"), // colorFill
			Color.byName("Gray"), // colorBorder
			Color.byName("GrayLight") // colorDisabled
		);

		this.Dark = new ControlStyle
		(
			"Dark", // name
			Color.byName("GrayDark"), // colorBackground
			Color.byName("Black"), // colorFill
			Color.byName("White"), // colorBorder
			Color.byName("GrayLight") // colorDisabled
		);

		this._All = Arrays.asList(new ControlStyle[] { this.Default, this.Dark });

		this._AllByName = ArrayHelper.addLookupsByName(this._All);
	}
}
