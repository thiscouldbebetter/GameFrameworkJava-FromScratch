
package Display;

import java.util.*;

import Helpers.*;
import Utility.*;

public class Color_Instances
{
	public Color _Transparent;
	public Color Black;
	public Color Blue;
	public Color BlueDark;
	public Color BlueLight;
	public Color Brown;
	public Color Cyan;
	public Color Gray;
	public Color GrayDark;
	public Color GrayDarker;
	public Color GrayLight;
	public Color GrayLighter;
	public Color Green;
	public Color GreenDark;
	public Color GreenDarker;
	public Color GreenLight;
	public Color GreenMediumDark;
	public Color GreenMediumLight;
	public Color Orange;
	public Color Pink;
	public Color Red;
	public Color RedDark;
	public Color Tan;
	public Color Violet;
	public Color White;
	public Color Yellow;
	public Color YellowDark;

	public Color[] _All;
	public Map<String,Color> _AllByCode;
	public Map<String,Color> _AllByName;

	public Color_Instances()
	{
		this._Transparent = new Color("Transparent", ".", new double[] { 0, 0, 0, 0 } );

		this.Black = new Color("Black", "k", new double[] { 0, 0, 0, 1 } );
		this.Blue = new Color("Blue", "b", new double[] { 0, 0, 1, 1 } );
		this.BlueDark = new Color("BlueDark", "B", new double[] { 0, 0, .5, 1 } );
		this.BlueLight = new Color("BlueLight", "$", new double[] { .5, .5, 1, 1 } );
		this.Brown = new Color("Brown", "O", new double[] { 0.5, 0.25, 0, 1 } );
		this.Cyan = new Color("Cyan", "c", new double[] { 0, 1, 1, 1 } );
		this.Gray = new Color("Gray", "a", new double[] { 0.5, 0.5, 0.5, 1 } );
		this.GrayDark = new Color("GrayDark", "A", new double[] { 0.25, 0.25, 0.25, 1 } );
		this.GrayDarker = new Color("GrayDarker", "#", new double[] { 0.125, 0.125, 0.125, 1 } );
		this.GrayLight = new Color("GrayLight","@", new double[] { 0.75, 0.75, 0.75, 1 } );
		this.GrayLighter = new Color("GrayLighter","-", new double[] { 0.825, 0.825, 0.825, 1 } );
		this.Green = new Color("Green",	"g", new double[] { 0, 1, 0, 1 } );
		this.GreenDark = new Color("GreenDark", "G", new double[] { 0, .5, 0, 1 } );
		this.GreenDarker = new Color("GreenDarker", "", new double[] { 0, .25, 0, 1 } );
		this.GreenLight = new Color("GreenLight", "%", new double[] { .5, 1, .5, 1 } );
		this.GreenMediumDark = new Color("GreenMediumDark", "", new double[] { 0, .75, 0, 1 } );
		this.GreenMediumLight = new Color("GreenMediumLight", "", new double[] { .25, 1, .25, 1 } );
		this.Orange = new Color("Orange", "o", new double[] { 1, 0.5, 0, 1 } );
		this.Pink = new Color("Pink", "p", new double[] { 1, 0.5, 0.5, 1 } );
		this.Red = new Color("Red", "r", new double[] { 1, 0, 0, 1 } );
		this.RedDark = new Color("RedDark", "R", new double[] { .5, 0, 0, 1 } );
		// this.Tan = null; // todo - Color.fromSystemColor("Tan");
		this.Violet = new Color("Violet", "v", new double[] { 1, 0, 1, 1 } );
		this.White = new Color("White", "w", new double[] { 1, 1, 1, 1 } );
		this.Yellow = new Color("Yellow", "y", new double[] { 1, 1, 0, 1 } );
		this.YellowDark = new Color("YellowDark", "Y", new double[] { .5, .5, 0, 1 } );

		this._All = new Color[]
		{
			this._Transparent,

			this.Black,
			this.Blue,
			this.BlueDark,
			this.BlueLight,
			this.Brown,
			this.Cyan,
			this.Gray,
			this.GrayDark,
			this.GrayDarker,
			this.GrayLight,
			this.GrayLighter,
			this.Green,
			this.GreenDark,
			this.GreenDarker,
			this.GreenLight,
			this.GreenMediumDark,
			this.GreenMediumLight,
			this.Orange,
			this.Pink,
			this.Red,
			this.RedDark,
			//this.Tan,
			this.Violet,
			this.White,
			this.Yellow,
			this.YellowDark,
		};

		this._AllByCode =
			ArrayHelper.addLookups(this._All, (Color x) -> x.code);

		this._AllByName = ArrayHelper.addLookupsByName(this._All);
	}
}
