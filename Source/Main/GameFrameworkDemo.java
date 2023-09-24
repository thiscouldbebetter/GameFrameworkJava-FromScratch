package Main;

import java.util.*;
import java.util.stream.*;

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

		var imageDirectoryPath = "../Content/Images/";
		var imageFileExtension = ".png";
		var imageNames = new String[]
		{
			"Friendly",
			"Opening",
			"Producer",
			"Title",
		};
		var images = new Image2[imageNames.length];

		for (var i = 0; i < imageNames.length; i++)
		{
			var imageName = imageNames[i];
			var imageFilePath =
				imageDirectoryPath + imageName + imageFileExtension;
			var image =
				new Image2(imageName, imageFilePath).load(platformHelper);
			images[i] = image;
		}

		var mediaLibrary = new MediaLibrary(images);

		var soundHelper = new SoundHelper();

		var timerHelper = new TimerHelper(20); // ticksPerSecond

		var visualFriendlyImage = mediaLibrary.imageGetByName("Friendly");
		var visualFriendly = VisualImageImmediate.fromImage(visualFriendlyImage);

		var visualOpeningImage = mediaLibrary.imageGetByName("Opening");
		var visualOpening = VisualImageImmediate.fromImage(visualOpeningImage);

		var entityPlayer = new Entity
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
					visualFriendly
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
				entityPlayer
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
