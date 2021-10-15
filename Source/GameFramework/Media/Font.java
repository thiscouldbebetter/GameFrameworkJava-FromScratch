
package GameFramework.Media;

import GameFramework.Utility.*;

public class Font implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;

	public Font(String id, String filePath)
	{
		this.id = id;
		this.filePath = filePath;
	}

	// Namable.

	public String name()
	{
		return this.id;
	}
}
