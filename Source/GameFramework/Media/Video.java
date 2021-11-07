
package GameFramework.Media;

import GameFramework.Model.*;
import GameFramework.Utility.*;

public class Video implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;
	public boolean isFinished;

	public Video(String id, String filePath)
	{
		this.id = id;
		this.filePath = filePath;
		this.isFinished = false;
	}

	// Namable.

	public String name()
	{
		return this.id;
	}

	public void play(Universe universe)
	{}

	public void stop(PlatformHelper platformHelper)
	{}

}
