
package GameFramework.Media;

import GameFramework.*;

public class Font implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;

	public Font(String id, String filePath)
	{}

	// Namable.

	public String name()
	{
		return this.id;
	}
}
