package GameFramework.Model;

import java.util.*;

import GameFramework.Controls.*;
import GameFramework.Helpers.*;
import GameFramework.Model.Places.*;
import GameFramework.Utility.*;

public class World //
{
	public String name;
	public DateTime dateCreated;
	public WorldDefn defn;
	public Place[] places;
	public Map<String,Place> placesByName;

	public DateTime dateSaved;
	public int timerTicksSoFar;
	public Place placeCurrent;
	public Place placeNext;

	public World
	(
		String name, DateTime dateCreated, WorldDefn defn, Place[] places
	)
	{
		this.name = name;
		this.dateCreated = dateCreated;

		this.timerTicksSoFar = 0;

		this.defn = defn;

		this.places = places;
		this.placesByName = ArrayHelper.addLookupsByName(this.places);
		this.placeNext = this.places[0];
	}

	public static World _default()
	{
		return new World
		(
			"name",
			DateTime.now(),
			WorldDefn._default(),
			new Place[]
			{
				Place._default()
			} // places
		);
	}

	public void draw(Universe universe)
	{
		if (this.placeCurrent != null)
		{
			this.placeCurrent.draw(universe, this, universe.display);
		}
	}

	public void initialize(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.world = this;

		if (this.placeNext != null)
		{
			if (this.placeCurrent != null)
			{
				this.placeCurrent.finalize(uwpe);
			}
			this.placeCurrent = this.placeNext;
			this.placeNext = null;
		}

		if (this.placeCurrent != null)
		{
			uwpe.place = this.placeCurrent;
			this.placeCurrent.initialize(uwpe);
		}
	}

	public String timePlayingAsStringShort(Universe universe)
	{
		return universe.timerHelper.ticksToStringH_M_S(this.timerTicksSoFar);
	}

	public String timePlayingAsStringLong(Universe universe)
	{
		return universe.timerHelper.ticksToStringHours_Minutes_Seconds
		(
			this.timerTicksSoFar
		);
	}

	public VenueWorld toVenue()
	{
		return new VenueWorld(this);
	}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		uwpe.world = this;
		if (this.placeNext != null)
		{
			if (this.placeCurrent != null)
			{
				this.placeCurrent.finalize(uwpe);
			}
			this.placeCurrent = this.placeNext;
			this.placeNext = null;
			uwpe.place = this.placeCurrent;
			this.placeCurrent.initialize(uwpe);
		}
		this.placeCurrent.updateForTimerTick(uwpe);
		this.timerTicksSoFar++;
	}

	// Controls.

	public ControlBase toControl(Universe universe)
	{
		return this.placeCurrent.toControl(universe, this);
	}
}
