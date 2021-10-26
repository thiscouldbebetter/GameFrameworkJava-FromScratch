
package GameFramework.Storage.TarFiles;

public class TarFileTypeFlag
{
	public String id;
	public String value;
	public String name;

	public TarFileTypeFlag(String value, String name)
	{
		this.value = value;
		this.id = "_" + this.value;
		this.name = name;
	}

	private static TarFileTypeFlag_Instances _instances;
	public static TarFileTypeFlag_Instances Instances()
	{
		if (TarFileTypeFlag._instances == null)
		{
			TarFileTypeFlag._instances = new TarFileTypeFlag_Instances();
		}
		return TarFileTypeFlag._instances;
	}

	public static TarFileTypeFlag byId(String id)
	{
		return TarFileTypeFlag.Instances()._AllById.get(id);
	}
}

