
package Controls;

import Display.*;
import Geometry.*;
import Model.*;

import java.util.*;
import java.util.function.*;

public class VenueMessage<TContext> implements Venue
{
	public DataBinding<TContext,String> messageToShow;
	public Consumer<UniverseWorldPlaceEntities> acknowledge;
	public Venue venuePrev;
	public Coords _sizeInPixels;
	public boolean showMessageOnly;

	public Venue _venueInner;

	public VenueMessage
	(
		DataBinding<TContext,String> messageToShow,
		Consumer<UniverseWorldPlaceEntities> acknowledge,
		Venue venuePrev,
		Coords sizeInPixels,
		boolean showMessageOnly
	)
	{
		this.messageToShow = messageToShow;
		this.acknowledge = acknowledge;
		this.venuePrev = venuePrev;
		this._sizeInPixels = sizeInPixels;
		this.showMessageOnly = showMessageOnly;
	}

	public static VenueMessage<String> fromMessage
	(
		DataBinding<String,String> message
	)
	{
		return VenueMessage.fromMessageAndAcknowledge(message, null);
	}

	public static <TContext> VenueMessage<TContext> fromMessageAndAcknowledge
	(
		DataBinding<TContext,String> messageToShow,
		Consumer<UniverseWorldPlaceEntities> acknowledge
	)
	{
		return new VenueMessage<TContext>
		(
			messageToShow, acknowledge, null, null, false
		);
	}

	public static VenueMessage<String> fromText(String message)
	{
		var dataBinding = new DataBinding<String,String>
		(
			message,
			(String c) -> message,
			(String c, String v) -> {}
		);
		return VenueMessage.fromMessage(dataBinding);
	}

	// instance methods

	public void draw(Universe universe)
	{
		this.venueInner(universe).draw(universe);
	}

	public void finalize(Universe universe) {}

	public void initialize(Universe universe) {}

	public void updateForTimerTick(Universe universe)
	{
		this.venueInner(universe).updateForTimerTick(universe);
	}

	public Coords sizeInPixels(Universe universe)
	{
		return (this._sizeInPixels == null ? universe.display.sizeInPixels() : this._sizeInPixels);
	}

	public Venue venueInner(Universe universe)
	{
		if (this._venueInner == null)
		{
			var sizeInPixels = this.sizeInPixels(universe);

			var controlMessage = universe.controlBuilder.message
			(
				universe,
				sizeInPixels,
				this.messageToShow,
				this.acknowledge,
				this.showMessageOnly
			);

			var venuesToLayer = new ArrayList<Venue>();

			if (this.venuePrev != null)
			{
				venuesToLayer.add(this.venuePrev);
			}

			venuesToLayer.add(controlMessage.toVenue());

			this._venueInner = new VenueLayered
			(
				venuesToLayer.toArray(new Venue[] {}), null
			);
		}

		return this._venueInner;
	}
}
