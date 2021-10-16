
package GameFramework.Display;

import java.util.*;

import GameFramework.Helpers.*;
import GameFramework.Utility.*;

public class Color implements Interpolatable<Color>, Namable
{
	public String _name;
	public String code;
	public double[] componentsRGBA;

	public java.awt.Color _systemColor;

	public Color(String name, String code, double[] componentsRGBA)
	{
		this._name = name;
		this.code = code;
		this.componentsRGBA = componentsRGBA;
	}

	public static Color byName(String colorName)
	{
		return Color.Instances()._AllByName.get(colorName);
	}

	public static Color fromRGB(double red, double green, double blue)
	{
		return new Color(null, null, new double[] {red, green, blue, 1});
	}

	public static Color fromSystemColor(java.awt.Color systemColor)
	{
		var returnValue = new Color(null, null, null); // todo
		returnValue._systemColor = systemColor;
		return returnValue;
	}

	public static java.awt.Color systemColorGet(Color color)
	{
		return (color == null ? null : color.systemColor() );
	}

	// constants

	public static int NumberOfComponentsRGBA = 4;

	// instances

	public static Color_Instances _instances;

	public static Color_Instances Instances()
	{
		if (Color._instances == null)
		{
			Color._instances = new Color_Instances();
		}

		return Color._instances;
	}

	// methods

	public double alpha()
	{
		return this.componentsRGBA[3];
	}
	
	public double alpha(double valueToSet)
	{
		this.componentsRGBA[3] = valueToSet;
		this._systemColor = null;
		return this.componentsRGBA[3];
	}

	public Color multiplyRGBScalar(double scalar)
	{
		for (var i = 0; i < 3; i++)
		{
			this.componentsRGBA[i] *= scalar;
		}
		return this;
	}

	public java.awt.Color systemColor()
	{
		if (this._systemColor == null)
		{
			this._systemColor =
				new java.awt.Color
				(
					(float)this.componentsRGBA[0],
					(float)this.componentsRGBA[1],
					(float)this.componentsRGBA[2],
					(float)this.componentsRGBA[3]
				);
		}

		return this._systemColor;
	}

	// Clonable.

	public Color clone()
	{
		return new Color(this.name(), this.code, ArrayHelper.clone(this.componentsRGBA));
	}

	public Color overwriteWith(Color other)
	{
		this._name = other.name();
		this.code = other.code;
		ArrayHelper.overwriteWith(this.componentsRGBA, other.componentsRGBA);
		this._systemColor = null;
		return this;
	}

	// Interpolatable.

	public Color interpolateWith
	(
		Color other, double fractionOfProgressTowardOther
	)
	{
		var componentsRGBAThis = this.componentsRGBA;
		var componentsRGBAOther = other.componentsRGBA;
		var componentsRGBAInterpolated = new double[4];
		for (var i = 0; i < componentsRGBAThis.length; i++)
		{
			var componentThis = componentsRGBAThis[i];
			var componentOther = componentsRGBAOther[i];
			var componentInterpolated =
				componentThis
				+ componentOther * fractionOfProgressTowardOther;
			componentsRGBAInterpolated[i] = componentInterpolated;
		}

		var colorInterpolated = new Color
		(
			"Interpolated",
			null, // code
			componentsRGBAInterpolated
		);

		return colorInterpolated;
	}

	// Namable.

	public String name()
	{
		return this._name;
	}
}
