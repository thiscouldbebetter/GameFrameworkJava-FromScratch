
package GameFramework.Media;

import GameFramework.Model.*;
import GameFramework.Utility.*;

public class Sound implements Namable, Platformable
{
	public String _name;
	public String sourcePath;

	public double offsetInSeconds;
	public boolean isRepeating;

	// public any domElement;

	public Sound(String name, String sourcePath)
	{
		this.name = name;
		this.sourcePath = sourcePath;

		this.offsetInSeconds = 0;
	}

	/*
	domElementBuild(Universe universe, number volume): any
	{
		this.domElement = document.createElement("audio");
		this.domElement.sound = this;
		this.domElement.autoplay = true;
		this.domElement.onended = this.stopOrRepeat.bind(this, universe);
		this.domElement.loop = this.isRepeating;
		this.domElement.volume = volume;

		var domElementForSoundSource = document.createElement("source");
		domElementForSoundSource.src = this.sourcePath;

		this.domElement.appendChild
		(
			domElementForSoundSource
		);

		return this.domElement;
	}
	*/

	public void pause(Universe universe)
	{
		var offsetInSeconds = 0; // todo - Formerly this.domElement.currentTime;
		this.stop(universe);
		this.offsetInSeconds = offsetInSeconds;
	}

	public void play(Universe universe, double volume)
	{
		// todo
		// this.domElementBuild(universe, volume);
		// this.domElement.currentTime = this.offsetInSeconds;

		universe.platformHelper.platformableAdd(this);	}

	public void reset()
	{
		this.offsetInSeconds = 0;
	}

	public void stop(Universe universe)
	{
		universe.platformHelper.platformableRemove(this);
		this.offsetInSeconds = 0;
	}

	public void stopOrRepeat(Universe universe)
	{
		if (this.isRepeating == false)
		{
			this.stop(universe);
		}
	}

	// platformable

	/*
	toDomElement(): any
	{
		return this.domElement;
	}
	*/
	
	// Namable.
	
	public String name()
	{
		return this._name;
	}
}
