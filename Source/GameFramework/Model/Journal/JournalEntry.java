
package GameFramework.Model.Journal;

public class JournalEntry
{
	public int tickRecorded;
	public String title;
	public String body;

	public JournalEntry(int tickRecorded, String title, String body)
	{
		this.tickRecorded = tickRecorded;
		this.title = title;
		this.body = body;
	}

	public String timeRecordedAsStringH_M_S(Universe universe)
	{
		return universe.timerHelper.ticksToStringH_M_S(this.tickRecorded)
	}

	public String toString(Universe universe)
	{
		var returnValue =
		(
			universe.timerHelper.ticksToStringHColonMColonS(this.tickRecorded)
			+ " - " + this.title
		);

		return returnValue;
	}
}
