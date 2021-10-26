
package GameFramework.Media;

import java.util.*;

import GameFramework.Model;

public class VenueVideo implements Venue
{
	public String videoName;
	public Venue venueNext;

	public ActionToInputsMapping actionToInputsMappings[];
	public Map<String,ActionToInputsMapping> actionToInputsMappingsByInputName;
	public boolean hasVideoBeenStarted;
	public Video video;

	public VenueVideo(String videoName, Venue venueNext)
	{
		this.videoName = videoName;
		this.venueNext = venueNext;

		this.hasVideoBeenStarted = false;

		var inputNames = Input.Names();
		var controlActionNames = ControlActionNames.Instances();
		this.actionToInputsMappings = new ActionToInputsMapping[]
		{
			new ActionToInputsMapping
			(
				controlActionNames.ControlCancel,
				new String[] { inputNames.Escape, inputNames.GamepadButton0 + "0"},
				true
			),
		};

		this.actionToInputsMappingsByInputName = ArrayHelper.addLookupsMultiple
		(
			this.actionToInputsMappings, (ActionToInputsMapping x) -> x.inputNames
		);
	}

	public void draw()
	{
		// do nothing
	}

	public void finalize(Universe universe) {}
	public void initialize(Universe universe) {}

	public void updateForTimerTick(Universe universe)
	{
		if (this.video == null)
		{
			universe.platformHelper.platformableHide(universe.display);
			this.video = universe.videoHelper.videosByName.get(this.videoName);
			this.video.play(universe);
		}

		if (this.video.isFinished == false)
		{
			var shouldVideoBeStopped = false;

			var inputHelper = universe.inputHelper;
			if (inputHelper.isMouseClicked(null))
			{
				inputHelper.isMouseClicked(false);
				shouldVideoBeStopped = true;
			}
			else
			{
				var controlActionNames = ControlActionNames.Instances();
				var inputsPressed = inputHelper.inputsPressed;
				for (var i = 0; i < inputsPressed.length; i++)
				{
					var inputPressed = inputsPressed[i];
					if (inputPressed.isActive)
					{
						var actionToInputsMapping =
							this.actionToInputsMappingsByInputName.get(inputPressed.name);
						if (actionToInputsMapping != null)
						{
							inputPressed.isActive = false;
							var actionName = actionToInputsMapping.actionName;
							if (actionName == controlActionNames.ControlCancel)
							{
								shouldVideoBeStopped = true;
							}
						}
					}
				}
			}

			if (shouldVideoBeStopped)
			{
				this.video.stop(universe.platformHelper);
			}
		}

		if (this.video.isFinished)
		{
			var display = universe.display;
			var colorBlack = Color.byName("Black");
			display.drawBackground(colorBlack, colorBlack);
			universe.platformHelper.platformableShow(display);
			universe.venueNext =
				universe.controlBuilder.venueTransitionalFromTo
				(
					this, this.venueNext
				);
		}
	}
}
