
package GameFramework.Input;

import java.util.function.*;

import GameFramework.Input.*;
import GameFramework.Model.*;

public class VenueInputCapture implements Venue
{
	public Venue venueToReturnTo;
	public Consumer<Input> functionToPassInputCapturedTo;

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
			for (var i = 0; i < inputsPressed.size(); i++)
			{
				var inputPressed = inputsPressed.get(i);
				if (inputPressed.name.startsWith("Mouse") == false)
				{
					if (inputPressed.isActive)
					{
						this.functionToPassInputCapturedTo.accept(inputPressed);
						universe.venueNext = this.venueToReturnTo;
					}
				}
			}
		}
	}
}
