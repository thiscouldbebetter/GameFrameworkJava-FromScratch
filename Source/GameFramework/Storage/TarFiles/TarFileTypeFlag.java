
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
	public static Instances(): TarFileTypeFlag_Instances
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

class TarFileTypeFlag_Instances
{
	public TarFileTypeFlag Normal;
	public TarFileTypeFlag HardLink;
	public TarFileTypeFlag SymbolicLink;
	public TarFileTypeFlag CharacterSpecial;
	public TarFileTypeFlag BlockSpecial;
	public TarFileTypeFlag Directory;
	public TarFileTypeFlag FIFO;
	public TarFileTypeFlag ContiguousFile;
	public TarFileTypeFlag LongFilePath;

	public TarFileTypeFlag[] _All;
	public Map<String,TarFileTypeFlag> _AllById;

	public TarFileTypeFlag_Instances()
	{
		this.Normal 		= new TarFileTypeFlag("0", "Normal");
		this.HardLink 		= new TarFileTypeFlag("1", "Hard Link");
		this.SymbolicLink 	= new TarFileTypeFlag("2", "Symbolic Link");
		this.CharacterSpecial 	= new TarFileTypeFlag("3", "Character Special");
		this.BlockSpecial 	= new TarFileTypeFlag("4", "Block Special");
		this.Directory		= new TarFileTypeFlag("5", "Directory");
		this.FIFO			= new TarFileTypeFlag("6", "FIFO");
		this.ContiguousFile = new TarFileTypeFlag("7", "Contiguous File");

		this.LongFilePath 	= new TarFileTypeFlag("L", "././@LongLink");

		// Additional types not implemented:
		// 'g' - global extended header with meta data (POSIX.1-2001)
		// 'x' - extended header with meta data for the next file in the archive (POSIX.1-2001)
		// 'A'-'Z' - Vendor specific extensions (POSIX.1-1988)
		// [other values] - reserved for future standardization

		this._All = new TarFileTypeFlag[]
		{
			this.Normal,
			this.HardLink,
			this.SymbolicLink,
			this.CharacterSpecial,
			this.BlockSpecial,
			this.Directory,
			this.FIFO,
			this.ContiguousFile,
			this.LongFilePath,
		};

		this._AllById = new HashMap<String,TarFileTypeFlag>();
		for (var i = 0; i < this._All.length; i++)
		{
			var flag = this._All[i];
			this._AllById.put(flag.id, flag);
		}
	}
}
