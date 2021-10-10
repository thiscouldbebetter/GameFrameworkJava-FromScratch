
package GameFramework.Model;

import java.util.*;
import java.util.function.*;

import GameFramework.*;
import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Collisions.*;
import GameFramework.Media.*;
import GameFramework.Inputs.*;
import GameFramework.Storage.*;
import GameFramework.Utility.*;

public class Universe
{
	public String name;
	public String version;
	public TimerHelper timerHelper;
	public Display display;
	public MediaLibrary mediaLibrary;
	public ControlStyle controlStyle;
	public Function<Universe,World> _worldCreate;

	public World world;

	public CollisionHelper collisionHelper;
	public ControlBuilder controlBuilder;
	public DisplayRecorder displayRecorder;
	public EntityBuilder entityBuilder;
	public IDHelper idHelper;
	public InputHelper inputHelper;
	public PlatformHelper platformHelper;
	public RandomizerSystem randomizer;
	public Serializer serializer;
	public SoundHelper soundHelper;
	public StorageHelper storageHelper;
	public VideoHelper videoHelper;

	public String debuggingModeName;
	public Profile profile;
	public Venue venueNext;
	public Venue venueCurrent;

	public Universe
	(
		String name,
		String version,
		TimerHelper timerHelper,
		Display display,
		MediaLibrary mediaLibrary,
		ControlBuilder controlBuilder,
		Function<Universe,World> worldCreate
	)
	{
		this.name = name;
		this.version = version;
		this.timerHelper = timerHelper;
		this.display = display;
		this.mediaLibrary = mediaLibrary;
		this.controlBuilder = controlBuilder;
		this._worldCreate = worldCreate;

		this.collisionHelper = new CollisionHelper();
		this.displayRecorder = new DisplayRecorder
		(
			1, // ticksPerFrame
			100, // bufferSizeInFrames - 5 seconds at 20 fps.
			true // isCircular
		);
		this.entityBuilder = new EntityBuilder();
		this.idHelper = IDHelper.Instance();
		this.platformHelper = new PlatformHelper();
		this.randomizer = new RandomizerSystem();
		this.serializer = new Serializer();

		this.venueNext = null;

		var debuggingModeName =
			URLParser.fromWindow().queryStringParameterByName("debug");
		this.debuggingModeName = debuggingModeName;
	}

	// static methods

	public static Universe create
	(
		String name,
		String version,
		TimerHelper timerHelper,
		Display display,
		MediaLibrary mediaLibrary,
		ControlBuilder controlBuilder,
		Function<Universe,World> worldCreate
	)
	{
		var returnValue = new Universe
		(
			name,
			version,
			timerHelper,
			display,
			mediaLibrary,
			controlBuilder,
			worldCreate
		);

		return returnValue;
	}

	public static Universe _default()
	{
		var universe = Universe.create
		(
			"Default",
			"0.0.0", // version
			new TimerHelper(20),
			Display2D.fromSize
			(
				Coords.fromXY(200, 150)
			),
			MediaLibrary._default(),
			ControlBuilder._default(),
			() -> World._default()
		);

		return universe;
	}

	// instance methods

	public void initialize(Consumer<Universe> callback)
	{
		this.platformHelper.initialize(this);
		this.storageHelper = new StorageHelper
		(
			StringHelper.replaceAll(this.name, " ", "_") + "_",
			this.serializer,
			new CompressorLZW()
		);

		this.display.initialize(this);
		this.platformHelper.platformableAdd(this.display);

		this.soundHelper = new SoundHelper(this.mediaLibrary.sounds);
		this.videoHelper = new VideoHelper(this.mediaLibrary.videos);

		Venue venueInitial = null;

		if (this.debuggingModeName == "SkipOpening")
		{
			venueInitial = Profile.venueWorldGenerate(this);
		}
		else
		{
			venueInitial = this.controlBuilder.opening
			(
				this, this.display.sizeInPixels
			).toVenue();
		}

		venueInitial = VenueFader.fromVenuesToAndFrom
		(
			venueInitial, venueInitial
		);

		this.venueNext = venueInitial;

		this.inputHelper = new InputHelper();
		this.inputHelper.initialize(this);

		var universe = this;
		this.mediaLibrary.waitForItemsAllToLoad
		(
			() -> callback.call(universe)
		);
	}

	public void reset()
	{
		// hack
		this.soundHelper.reset();
	}

	public void start()
	{
		this.timerHelper.initialize
		(
			() -> this.updateForTimerTick()
		);
	}

	public void updateForTimerTick()
	{
		this.inputHelper.updateForTimerTick(this);

		if (this.venueNext != null)
		{
			if(this.venueCurrent != null)
			{
				this.venueCurrent.finalize(this);
			}

			this.venueCurrent = this.venueNext;
			this.venueNext = null;

			this.venueCurrent.initialize(this);
		}
		this.venueCurrent.updateForTimerTick(this);

		this.displayRecorder.updateForTimerTick(this);
	}

	public World worldCreate()
	{
		this.world = this._worldCreate.call(this);
		return this.world;
	}
}
