
package GameFramework.Model;

import java.util.function.*;

import GameFramework.Model.*;

public class Loadable implements EntityProperty<Loadable>
{
	public boolean isLoaded;
	private Consumer<UniverseWorldPlaceEntities> _load;
	private Consumer<UniverseWorldPlaceEntities> _unload;

	public Loadable
	(
		Consumer<UniverseWorldPlaceEntities> load,
		Consumer<UniverseWorldPlaceEntities> unload
	)
	{
		this.isLoaded = false;
		this._load = load;
		this._unload = unload;
	}

	public void finalize(UniverseWorldPlaceEntities uwpe)
	{
		this._unload.accept(uwpe);
	}

	public void initialize(UniverseWorldPlaceEntities uwpe)
	{
		this._load.accept(uwpe);
	}

	public void load(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isLoaded == false)
		{
			if (this._load != null)
			{
				this._load.accept(uwpe);
			}
			this.isLoaded = true;
		}
	}

	public void unload(UniverseWorldPlaceEntities uwpe)
	{
		if (this.isLoaded)
		{
			if (this._unload != null)
			{
				this._unload.accept(uwpe);
			}
			this.isLoaded = false;
		}
	}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		// Do nothing.
	}
	
	// Clonable.
	public Loadable clone() { return this; }
	public Loadable overwriteWith(Loadable other) { return this; }
}
