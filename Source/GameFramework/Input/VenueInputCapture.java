
package GameFramework.Input;

public class VenueInputCapture implements Venue
{
	public Venue venueToReturnTo;
	public Object functionToPassInputCapturedTo;

	public boolean isFirstTime;

	public VenueInputCapture
	(
		Venue venueToReturnTo,
		Object functionToPassInputCapturedTo
	)
	{
		this.venueToReturnTo = venueToReturnTo;
		this.functionToPassInputCapturedTo = functionToPassInputCapturedTo;

		this.isFirstTime = true;
	}

	// Venue.

	public void draw(Universe universe)
	{
		// Do nothing.
	}

	public void finalize(Universe universe) {}

	public void initialize(Universe universe) {}

	public void updateForTimerTick(Universe universe)
	{
		var inputHelper = universe.inputHelper;

		if (this.isFirstTime)
		{
			this.isFirstTime = false;
			inputHelper.inputsRemoveAll();
		}
		else
		{
			var inputsPressed = inputHelper.inputsPressed;
			for (var i = 0; i < inputsPressed.length; i++)
			{
				var inputPressed = inputsPressed[i];
				if (inputPressed.name.startsWith("Mouse") == false)
				{
					if (inputPressed.isActive)
					{
						this.functionToPassInputCapturedTo(inputPressed);
						universe.venueNext = this.venueToReturnTo;
					}
				}
			}
		}
	}
}

}
