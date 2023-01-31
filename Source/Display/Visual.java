package Display;

import Model.*;

public interface Visual
{
	void draw
	(
		Universe universe, World world, Place place, Entity entity
	);
}
