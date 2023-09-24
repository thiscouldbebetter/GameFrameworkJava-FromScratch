package Main;

import Display.*;
import Display.Visuals.*;
import Geometry.*;
import Input.*;
import Media.*;
import Model.*;
import Utility.*;

public class GameFrameworkDemo
{
	public static void main(String[] args)
	{
		var gameFrameworkDemo = new GameFrameworkDemo();
		gameFrameworkDemo.run();
	}

	public void run()
	{
		var displaySize = Coords.fromXY(400, 300);
		var display = new Display2D(displaySize);

		var inputHelper = new InputHelper();

		var platformHelper = new PlatformHelper();

		var mediaLibrary = new MediaLibrary
		(
			new Image2[]
			{
				Image2.fromSourcePath
				(
					"../Content/Images/Friendly.png"
				).load(platformHelper)
			}
		);

		var soundHelper = new SoundHelper();

		var timerHelper = new TimerHelper(20); // ticksPerSecond

		var visualImage = Image2.fromSourcePath
		(
			"../Content/Images/Friendly.png"
		).load(platformHelper);

		var visual2 = VisualImageImmediate.fromImage(visualImage);

		var visual = VisualRectangle.fromSize(Coords.fromXY(40, 30) );

		var entity0 = new Entity
		(
			"Player",

			new EntityProperty[]
			{
				new Actor
				(
					new ActivityUserInputAccept()
				),

				new Drawable
				(
					visual2
				),

				new Locatable
				(
					Disposition.fromPos
					(
						displaySize.clone().half()
					)
				)
			}
		);

		var place0 = new Place
		(
			"Place0",
			new Entity[]
			{
				entity0
			}
		);

		var worldDefn = new WorldDefn();

		var world = new World
		(
			"World0",
			worldDefn,
			new Place[]
			{
				place0
			}
		);

		var universe = new Universe
		(
			display,
			inputHelper,
			mediaLibrary,
			platformHelper,
			soundHelper,
			timerHelper,
			world
		);

		universe.initialize();
	}
}
