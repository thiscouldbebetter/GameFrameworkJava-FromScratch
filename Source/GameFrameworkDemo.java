
public class GameFrameworkDemo
{
	public static void main(String[] args)
	{
		var gameFrameworkDemo = new GameFrameworkDemo();
		gameFrameworkDemo.run();
	}

	public void run()
	{
		var displaySize = new Coords(400, 300);
		var display = new Display(displaySize);

		var inputHelper = new InputHelper();

		var platformHelper = new PlatformHelper();

		var soundHelper = new SoundHelper();

		var timerHelper = new TimerHelper(20); // ticksPerSecond

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
					new VisualRectangle
					(
						Color.Instances().Blue.name,
						new Coords(40, 30) // size
					)
				),

				new Locatable
				(
					new Location
					(
						displaySize.clone().half(), // pos
						new Coords(0, 0) // vel
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

		var world = new World
		(
			"World0",
			new Place[]
			{
				place0
			}
		);

		var universe = new Universe
		(
			display,
			inputHelper,
			platformHelper,
			soundHelper,
			timerHelper,
			world
		);

		universe.initialize();
	}
}
