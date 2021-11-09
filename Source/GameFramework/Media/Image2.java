
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
		return null; // todo
	}

	// Loadable.

	public boolean isLoaded()
	{
		return true;
	}

	public Image2 load()
	{
		// todo
		return this;
	}

	public void unload()
	{
		// todo
	}
}
