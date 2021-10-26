
package GameFramework.Media;

import GameFramework.Utility.*;

public class TextString implements MediaLibraryItem, Namable
{
	public String id;
	public String filePath;

	public String value;

	public TextString(String id, String filePath)
	{
		this.id = id;
		this.filePath = filePath;
		
		this.value = "todo - TextString";
	}

	// Namable.

	public String name()
	{
		return this.id;
	}
}
