
package GameFramework.Controls;

public class Controllable implements EntityProperty
{
	Object toControl;

	constructor(Object toControl)
	{
		this.toControl = toControl;
	}

	// EntityProperty.

	void finalize(uwpe: UniverseWorldPlaceEntities) {}
	void initialize(uwpe: UniverseWorldPlaceEntities) {}
	void updateForTimerTick(uwpe: UniverseWorldPlaceEntities) {}

}
