
package GameFramework.Display;

import java.util.*;

import GameFramework.Geometry.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class VenueFader implements Venue
{
	public Venue venuesToFadeFromAndTo[];
	public Color backgroundColor;
	public int millisecondsPerFade;

	public DateTime timeFadeStarted;
	public int venueIndexCurrent;

	public VenueFader
	(
		Venue venueToFadeTo,
		Venue venueToFadeFrom,
		Color backgroundColor,
		Integer millisecondsPerFade
	)
	{
		this.venuesToFadeFromAndTo = new Venue[]
		{
			venueToFadeFrom, venueToFadeTo
		};

		this.millisecondsPerFade =
			(millisecondsPerFade == null ? 250 : millisecondsPerFade);

		if (venueToFadeFrom == venueToFadeTo)
		{
			this.venueIndexCurrent = 1;
			this.millisecondsPerFade *= 2;
		}
		else
		{
			this.venueIndexCurrent = 0;
		}

		this.backgroundColor =
			(backgroundColor == null ? Color.Instances().Black : backgroundColor);
	}

	public static VenueFader fromVenueTo(Venue venueToFadeTo)
	{
		return new VenueFader(venueToFadeTo, null, null, null);
	}

	public static VenueFader fromVenuesToAndFrom
	(
		Venue venueToFadeTo, Venue venueToFadeFrom
	)
	{
		return new VenueFader(venueToFadeTo, venueToFadeFrom, null, null);
	}

	public void finalize(Universe universe) {}

	public void initialize(Universe universe)
	{
		var venueToFadeTo = this.venueToFadeTo();
		venueToFadeTo.initialize(universe);
	}

	public void updateForTimerTick(Universe universe)
	{
		this.draw(universe);

		var now = new DateTime();

		if (this.timeFadeStarted == null)
		{
			this.timeFadeStarted = now;
		}

		var millisecondsSinceFadeStarted =
			now.getTime() - this.timeFadeStarted.getTime();

		var fractionOfFadeCompleted =
			millisecondsSinceFadeStarted
			/ this.millisecondsPerFade;

		Double alphaOfFadeColor;

		if (this.venueIndexCurrent == 0)
		{
			if (fractionOfFadeCompleted > 1)
			{
				fractionOfFadeCompleted = 1;
				this.venueIndexCurrent++;
				this.timeFadeStarted = null;

				var venueToFadeTo = this.venuesToFadeFromAndTo[1];
				if (venueToFadeTo.draw == null)
				{
					universe.venueNext = venueToFadeTo;
				}

			}
			alphaOfFadeColor = fractionOfFadeCompleted;
		}
		else // this.venueIndexCurrent == 1
		{
			if (fractionOfFadeCompleted > 1)
			{
				fractionOfFadeCompleted = 1;
				universe.venueNext = this.venueCurrent();
			}

			alphaOfFadeColor = 1 - fractionOfFadeCompleted;
		}

		alphaOfFadeColor *= alphaOfFadeColor;
		var fadeColor = this.backgroundColor.clone();
		fadeColor.alpha
		(
			alphaOfFadeColor * this.backgroundColor.alpha()
		);

		var display = universe.display;
		display.drawRectangle
		(
			Coords.create(),
			display.sizeDefault(), // Scaled automatically.
			fadeColor,
			null, false
		);
	}

	public Venue venueToFadeTo()
	{
		return this.venuesToFadeFromAndTo[1];
	}

	public Venue venueCurrent()
	{
		return this.venuesToFadeFromAndTo[this.venueIndexCurrent];
	}

	public void draw(Universe universe)
	{
		var venueCurrent = this.venueCurrent();
		if (venueCurrent != null)
		{
			venueCurrent.draw(universe);
		}
	}
}
