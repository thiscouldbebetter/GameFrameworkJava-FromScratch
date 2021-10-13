package GameFramework;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Media.*;
import GameFramework.Model.*;

public class GameFramework
{
	public static void main(String[] args)
	{
		// It may be necessary to clear local storage to prevent errors on
		// deserialization of existing saved items after the schema changes.
		// localStorage.clear();

		var mediaLibrary = MediaLibrary.fromFileNamesByCategory
		(
			"../Content/",
			new String[] { "Title.png", },
			new String[] { "Sound.wav" },
			new String[] { "Music.mp3" },
			new String[] { "Movie.webm" },
			new String[] { "Font.ttf" },
			new String[] { "Conversation.json", "Instructions.txt" }
		);

		var displaySizesAvailable = new Coords[]
		{
			new Coords(400, 300, 1),
			new Coords(640, 480, 1),
			new Coords(800, 600, 1),
			new Coords(1200, 900, 1),
			// Wrap.
			new Coords(200, 150, 1),
		};

		var display = new Display2D
		(
			displaySizesAvailable,
			"Font", // fontName
			10, // fontHeightInPixels
			"Gray", "White" // colorFore, colorBack
		);

		var timerHelper = new TimerHelper(20);

		var universe = Universe.create
		(
			"Game Framework Demo Game",
			"0.0.0-20211105-0000",
			timerHelper,
			display,
			mediaLibrary,
			null, // controlBuilder
			null // worldCreate
		);

		universe.initialize
		(
			() -> { universe.start(); }
		);
	}
}
