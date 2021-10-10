
package GameFramework.Media;

import GameFramework.*;

public class TextString implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;

	public TextString(String id, String filePath)
	{}

	// Namable.

	public String name()
	{
		return this.id;
	}
}
