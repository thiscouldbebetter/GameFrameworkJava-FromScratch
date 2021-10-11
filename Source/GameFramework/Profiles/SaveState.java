
package GameFramework.Profiles;

public class SaveState
{
	public String name;
	public String placeName;
	public String timePlayingAsString;
	public DateTime timeSaved;
	public Image2 imageSnapshot;
	public World world;

	public SaveState
	(
		String name, String placeName, String timePlayingAsString,
		DateTime timeSaved, Image2 imageSnapshot, World world
	)
	{
		this.name = name;
		this.placeName = placeName;
		this.timePlayingAsString = timePlayingAsString;
		this.timeSaved = timeSaved;
		this.imageSnapshot = imageSnapshot;
		this.world = world;
	}

	public void load()
	{
		// todo
	}

	public void unload()
	{
		this.world = null;
	}
}
