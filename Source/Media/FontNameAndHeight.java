
package Media;

import java.awt.*;

public class FontNameAndHeight // implements Clonable<FontNameAndHeight>, Equatable<FontNameAndHeight>
{
	public String name;
	public int heightInPixels;

	public FontNameAndHeight
	(
		String name,
		int heightInPixels
	)
	{
		this.name = name != null ? name : "Font";
		this.heightInPixels = heightInPixels != 0 ? heightInPixels : 10;
	}

	public static FontNameAndHeight create()
	{
		return new FontNameAndHeight(null, 0);
	}

	public static FontNameAndHeight fromHeightInPixels(int heightInPixels)
	{
		return new FontNameAndHeight(null, heightInPixels);
	}

	public java.awt.Font toSystemFont()
	{
		return new java.awt.Font(this.name, java.awt.Font.PLAIN, this.heightInPixels);
	}

	// Clonable.

	public FontNameAndHeight clone()
	{
		return new FontNameAndHeight(this.name, this.heightInPixels);
	}

	public FontNameAndHeight overwriteWith(FontNameAndHeight other)
	{
		this.name = other.name;
		this.heightInPixels = other.heightInPixels;
		return this;
	}

	// Equatable.

	public boolean equals(FontNameAndHeight other)
	{
		var returnValue =
		(
			this.name == other.name
			&& this.heightInPixels == other.heightInPixels
		);
		return returnValue;
	}
}
