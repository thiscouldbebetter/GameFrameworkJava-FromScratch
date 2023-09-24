
package Media;

public class MediaLibrary
{
	private Image2[] images;

	public MediaLibrary(Image2[] images)
	{
		this.images = images;
	}

	public Image2 imageGetByName(String name)
	{
		return images[0]; // todo
	}
}
