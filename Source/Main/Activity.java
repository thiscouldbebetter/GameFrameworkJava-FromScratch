package Main;

import Model.*;

public interface Activity
{
	void perform(Universe universe, World world, Place place, Entity entity);
}
