
package GameFramework.Media

import GameFramework.Geometry.Transforms.*;
import GameFramework.Display.Visuals.*;

public class VisualSound implements Visual
{
	// Yes, obviously sounds aren't really visual.

	public String soundNameToPlay;
	public boolean isMusic;

	public boolean hasBeenStarted;

	public VisualSound(String soundNameToPlay, boolean isMusic)
	{
		this.soundNameToPlay = soundNameToPlay;
		this.isMusic = isMusic;
	}

	public static VisualSound _default()
	{
		return new VisualSound("Effects_Sound", false);
	}

	public static VisualSound fromSoundName(String soundName)
	{
		return new VisualSound(soundName, false); // isMusic
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var universe = uwpe.universe;
		var entity = uwpe.entity;

		var soundHelper = universe.soundHelper;

		var audible = entity.audible();
		if (audible != null)
		{
			if (audible.hasBeenHeard == false)
			{
				if (this.isMusic)
				{
					soundHelper.soundWithNamePlayAsMusic(universe, this.soundNameToPlay);
				}
				else
				{
					soundHelper.soundWithNamePlayAsEffect(universe, this.soundNameToPlay);
				}

				audible.hasBeenHeard = true;
			}
		}
	}

	// Clonable.

	public VisualSound clone()
	{
		return new VisualSound(this.soundNameToPlay, this.isMusic);
	}

	public VisualSound overwriteWith(VisualSound other)
	{
		this.soundNameToPlay = other.soundNameToPlay;
		this.isMusic = other.isMusic;
		return this;
	}

	// Transformable.

	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}

}
