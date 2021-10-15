
package GameFramework.Media;

import GameFramework.Utility.*;

public class Image2 implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;

	public Image2(String id, String filePath)
	{}

	// Namable.

	public String name()
	{
		return this.id;
	}
}
