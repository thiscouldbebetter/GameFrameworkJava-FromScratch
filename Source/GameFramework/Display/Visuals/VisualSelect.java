
package GameFramework.Display.Visuals;

import java.util.*;
import java.util.function.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;

public class VisualSelect implements Visual
{
	public Map<String,Visual> childrenByName;
	public BiFunction<UniverseWorldPlaceEntities,Display,String[]> selectChildNames;

	public VisualSelect
	(
		Map<String,Visual> childrenByName,
		BiFunction<UniverseWorldPlaceEntities,Display,String[]> selectChildNames
	)
	{
		this.childrenByName = childrenByName;
		this.selectChildNames = selectChildNames;
	}

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var childrenToSelectNames =
			this.selectChildNames(uwpe, display);
		var childrenSelected = childrenToSelectNames.map
		(
			childToSelectName -> this.childrenByName.get(childToSelectName)
		);
		childrenSelected.forEach
		(
			childSelected -> childSelected.draw(uwpe, display)
		);
	}

	// Clonable.

	public Visual clone()
	{
		return this; // todo
	}

	public Visual overwriteWith(Visual other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }
	
	public Transformable transform(Transform transformToApply)
	{
		return this; // todo
	}
}
