
package GameFramework.Media;

import GameFramework.*;

public class Video implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;

	public Video(String id, String filePath)
	{}

	// Namable.

	public String name()
	{
		return this.id;
	}
}
