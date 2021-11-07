
package GameFramework.Model.Journal;

import java.util.*;

public class Journal
{
	public List<JournalEntry> entries;

	public Journal(JournalEntry[] entries)
	{
		this.entries = Arrays.asList(entries);
	}
}
