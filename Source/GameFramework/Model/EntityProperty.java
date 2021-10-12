
public GameFramework.Model;

public interface EntityProperty
{
	void finalize(UniverseWorldPlaceEntities uwpe);
	void initialize(UniverseWorldPlaceEntities uwpe);
	void updateForTimerTick(UniverseWorldPlaceEntities uwpe);
}
