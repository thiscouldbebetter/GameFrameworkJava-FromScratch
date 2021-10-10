
package GameFramework.Media;

import java.util.*;

public class VideoHelper
{
	public Video[] videos;
	public Map<String,Video> videosByName;

	public VideoHelper(Video[] videos)
	{
		this.videos = videos;
		this.videosByName = ArrayHelper.addLookupsByName(this.videos);
	}
}
