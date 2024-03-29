
package Controls;

import Display.*;
import Helpers.*;

import java.util.*;

public class ControlStyle_Instances
{
	public ControlStyle Default;
	public ControlStyle Dark;

	public ControlStyle[] _All;
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

		this._All = new ControlStyle[] { this.Default, this.Dark };

		this._AllByName = ArrayHelper.addLookupsByName(this._All);
	}
}
