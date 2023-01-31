package Model;

import Display.*;
import Input.*;
import Main.*;
import Media.*;
import Utility.*;

public class Universe
{
	public Display display;
	public InputHelper inputHelper;
	public PlatformHelper platformHelper;
	public SoundHelper soundHelper;
	public TimerHelper timerHelper;

	public World world;

	public Universe
	(
		Display display,
		InputHelper inputHelper,
		PlatformHelper platformHelper,
		SoundHelper soundHelper,
		TimerHelper timerHelper,
		World world
	)
	{
		this.display = display;
		this.inputHelper = inputHelper;
		this.platformHelper = platformHelper;
		this.soundHelper = soundHelper;
		this.timerHelper = timerHelper;

		this.world = world;
	}

	public void initialize()
	{
		this.platformHelper.initialize();

		this.display.initialize(this);
		this.inputHelper.initialize(this);
		this.soundHelper.initialize(this);

		this.timerHelper.initialize(this);
	}

	public void updateForTimerTick()
	{
		this.display.clear();

		this.world.updateForTimerTick(this);

		this.display.updateForTimerTick(this);
	}
}
