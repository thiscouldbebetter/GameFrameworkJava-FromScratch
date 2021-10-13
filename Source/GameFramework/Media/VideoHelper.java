
package GameFramework.Media;

import java.util.*;

public class VideoHelper
{
	public List<Video> videos;
	public Map<String,Video> videosByName;

	public VideoHelper(List<Video> videos)
	{
		this.videos = videos;
		this.videosByName = ArrayHelper.addLookupsByName(this.videos);
	}
}
