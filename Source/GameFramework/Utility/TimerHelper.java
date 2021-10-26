
package GameFramework.Utility;

import GameFramework.Model.*;
import GameFramework.Model.Places.*;

public class TimerHelper
{
	public int ticksPerSecond;

	private int millisecondsPerTick;
	private boolean isRunning;
	
	public int ticksSoFar;

	public TimerHelper(double ticksPerSecond)
	{
		this.ticksPerSecond = ticksPerSecond;

		this.millisecondsPerTick =
			(int)(Math.round(1000 / this.ticksPerSecond));
		this.isRunning = false;
	}

	public void initialize(Universe universe)
	{
		if (this.isRunning == false)
		{
			this.isRunning = true;

			while (this.isRunning)
			{
				try
				{
					universe.updateForTimerTick();
					Thread.sleep(this.millisecondsPerTick);
				}
				catch (InterruptedException e)
				{
					// Do nothing.
				}
			}
		}
	}

	public void stop()
	{
		this.isRunning = false;
	}

	public String ticksToStringH_M_S(int ticksToConvert)
	{
		return this.ticksToString(ticksToConvert, " h ", " m ", " s");
	}

	public String ticksToStringHColonMColonS(int ticksToConvert)
	{
		return this.ticksToString(ticksToConvert, ":", ":", "");
	}

	public String ticksToStringHours_Minutes_Seconds(int ticksToConvert)
	{
		return this.ticksToString(ticksToConvert, " hours ", " minutes ", " seconds");
	}

	public String ticksToString
	(
		int ticksToConvert, String unitStringHours,
		String unitStringMinutes, String unitStringSeconds
	)
	{
		var secondsTotal = Math.floor
		(
			ticksToConvert / this.ticksPerSecond
		);
		var minutesTotal = Math.floor(secondsTotal / 60);
		var hoursTotal = Math.floor(minutesTotal / 60);

		var timeAsString =
			hoursTotal + unitStringHours
			+ (minutesTotal % 60) + unitStringMinutes
			+ (secondsTotal % 60) + unitStringSeconds;

		return timeAsString;
	}

}
