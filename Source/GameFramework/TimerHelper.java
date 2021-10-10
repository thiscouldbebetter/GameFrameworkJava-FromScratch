
package GameFramework;

public class TimerHelper
{
	public double ticksPerSecond;

	private int millisecondsPerTick;
	private boolean isRunning;

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
}
