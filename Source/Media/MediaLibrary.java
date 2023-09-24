
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
		Image2 imageFound = null;

		for (var i = 0; i < images.length; i++)
		{
			var image = images[i];
			if (image.name == name)
			{
				imageFound = image;
				break;
			}
		}

		return imageFound;
	}
}
