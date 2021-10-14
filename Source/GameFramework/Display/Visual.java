
package GameFramework.Display;

import GameFramework.*;
import GameFramework.Model.*;
import GameFramework.Model.Places.*;

public interface Visual
{
	void draw
	(
		Universe universe, World world, Place place, Entity entity
	);
}

