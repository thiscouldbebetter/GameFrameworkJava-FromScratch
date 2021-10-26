
package GameFramework.Media;

import java.util.*;
import java.util.stream.*;

import GameFramework.Controls.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;

public class SoundHelper
{
	public List<Sound> sounds;

	public Map<String,Sound> soundsByName;
	public double musicVolume;
	public double soundVolume;
	public Sound soundForMusic;

	//private AudioContext _audioContext;

	public SoundHelper(List<Sound> sounds)
	{
		this.sounds = sounds;
		this.soundsByName = ArrayHelper.addLookupsByName(this.sounds);

		this.musicVolume = 1;
		this.soundVolume = 1;

		this.soundForMusic = null;
	}

	public static ControlSelectOption[] controlSelectOptionsVolume()
	{
		var returnValue = new ControlSelectOption[]
		{
			new ControlSelectOption(1, "100%"),
			new ControlSelectOption(0, "0%"),
			new ControlSelectOption(.1, "10%"),
			new ControlSelectOption(.2, "20%"),
			new ControlSelectOption(.3, "30%"),
			new ControlSelectOption(.4, "40%"),
			new ControlSelectOption(.5, "50%"),
			new ControlSelectOption(.6, "60%"),
			new ControlSelectOption(.7, "70%"),
			new ControlSelectOption(.8, "80%"),
			new ControlSelectOption(.9, "90%"),
		};

		return returnValue;
	}

	// instance methods

	/*
	audioContext(): AudioContext
	{
		if (this._audioContext == null)
		{
			this._audioContext = new AudioContext();
		}

		return this._audioContext;
	}
	*/

	public void reset()
	{
		for (var i = 0; i < this.sounds.size(); i++)
		{
			var sound = this.sounds.get(i);
			sound.offsetInSeconds = 0;
		}
	}

	public void soundWithNamePlayAsEffect(Universe universe, String soundName)
	{
		var sound = this.soundsByName.get(soundName);
		sound.isRepeating = false;
		sound.play(universe, this.soundVolume);
	}

	public void soundWithNamePlayAsMusic
	(
		Universe universe, String soundToPlayName
	)
	{
		var soundToPlay = this.soundsByName.get(soundToPlayName);
		soundToPlay.isRepeating = true;

		var soundAlreadyPlaying = this.soundForMusic;

		if (soundAlreadyPlaying == null)
		{
			soundToPlay.play(universe, this.musicVolume);
		}
		else if (soundAlreadyPlaying.name() != soundToPlayName)
		{
			soundAlreadyPlaying.stop(universe);
			soundToPlay.play(universe, this.musicVolume);
		}

		this.soundForMusic = soundToPlay;
	}

	public void soundsAllStop(Universe universe)
	{
		this.sounds.stream().forEach(x -> x.stop(universe));
	}
}
