package Main;

import Model.*;

public interface Activity
{
	Activity clone();
	void perform(UniverseWorldPlaceEntities uwpe);
}
