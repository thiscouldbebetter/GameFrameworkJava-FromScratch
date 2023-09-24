package Model;

import Main.*;
import Utility.*;

public interface EntityProperty // extends Clonable<EntityProperty>
{
	String name();
	void updateForTimerTick(UniverseWorldPlaceEntities uwpe);
	EntityProperty cloneAsEntityProperty();
}
