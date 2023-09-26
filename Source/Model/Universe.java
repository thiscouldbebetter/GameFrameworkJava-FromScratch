package Model;

import Controls.*;
import Display.*;
import Input.*;
import Main.*;
import Media.*;
import Profiles.*;
import Utility.*;

public class Universe
{
	public Display display;
	public InputHelper inputHelper;
	public MediaLibrary mediaLibrary;
	public PlatformHelper platformHelper;
	public SoundHelper soundHelper;
	public TimerHelper timerHelper;
	public World world;

	public ControlBuilder controlBuilder;

	public Profile profile;

	public Venue venueCurrent;
	public Venue venueNext;

	public Universe
	(
		Display display,
		InputHelper inputHelper,
		MediaLibrary mediaLibrary,
		PlatformHelper platformHelper,
		SoundHelper soundHelper,
		TimerHelper timerHelper,
		World world
	)
	{
		this.display = display;
		this.inputHelper = inputHelper;
		this.mediaLibrary = mediaLibrary;
		this.platformHelper = platformHelper;
		this.soundHelper = soundHelper;
		this.timerHelper = timerHelper;
		this.world = world;

		this.controlBuilder = ControlBuilder._default();
	}

	public void initialize()
	{
		var controlOpening = this.controlBuilder.opening
		(
			this, this.display.sizeInPixels()
		);
		var venueInitial =
			// this.world.toVenue(this);
			controlOpening.toVenue();
		this.venueTransitionTo(venueInitial);

		this.platformHelper.initialize();

		this.display.initialize(this);
		this.inputHelper.initialize(this);
		this.soundHelper.initialize(this);

		this.timerHelper.initialize(this);
	}

	public void updateForTimerTick()
	{
		this.display.clear();

		this.inputHelper.updateForTimerTick(this);

		if (this.venueNext != null)
		{
			if (this.venueCurrent != null)
			{
				this.venueCurrent.finalize(this);
			}

			this.venueCurrent = this.venueNext;
			this.venueNext = null;

			this.venueCurrent.initialize(this);
		}
		this.venueCurrent.updateForTimerTick(this);

		this.display.updateForTimerTick(this);

		//this.displayRecorder.updateForTimerTick(this);
	}

	public void venueTransitionTo(Venue venueToTransitionTo)
	{
		this.venueNext = venueToTransitionTo;
		/*
		this.controlBuilder.venueTransitionalFromTo
		(
			this.venueCurrent, venueToTransitionTo
		);
		*/
	}

}
