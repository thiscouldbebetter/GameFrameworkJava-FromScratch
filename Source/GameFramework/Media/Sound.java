
package GameFramework.Media;

import GameFramework.*;

public class Sound implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;

	public Sound(String id, String filePath)
	{}

	// Namable.

	public String name()
	{
		return this.id;
	}
}
