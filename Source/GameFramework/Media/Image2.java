
package GameFramework.Media;

import GameFramework.Geometry.*;
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

	public Coords sizeInPixels()
	{
		throw new Exception("todo");
	}
	
	// Loadable.
	
	public void load()
	{
		// todo
	}

	public void unload()
	{
		// todo
	}
}
