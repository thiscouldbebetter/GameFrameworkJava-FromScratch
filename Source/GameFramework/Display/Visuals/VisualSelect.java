
package GameFramework.Display.Visuals;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import GameFramework.Display.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualSelect implements Visual<VisualSelect>
{
	public Map<String,Visual> childrenByName;
	public BiFunction<UniverseWorldPlaceEntities,Display,String[]> _selectChildNames;

	public VisualSelect
	(
		Map<String,Visual> childrenByName,
		BiFunction<UniverseWorldPlaceEntities,Display,String[]> selectChildNames
	)
	{
		this.childrenByName = childrenByName;
		this._selectChildNames = selectChildNames;
	}

	public String[] selectChildNames
	(
		UniverseWorldPlaceEntities uwpe, Display display
	)
	{
		return this._selectChildNames.apply(uwpe, display);
	}

	// Visual

	public void draw(UniverseWorldPlaceEntities uwpe, Display display)
	{
		var childrenToSelectNames =
			this.selectChildNames(uwpe, display);
		var childrenSelected = Arrays.asList
		(
			childrenToSelectNames
		).stream().map
		(
			childToSelectName -> this.childrenByName.get(childToSelectName)
		);
		childrenSelected.forEach
		(
			childSelected -> childSelected.draw(uwpe, display)
		);
	}

	// Clonable.

	public VisualSelect clone()
	{
		return this; // todo
	}

	public VisualSelect overwriteWith(VisualSelect other)
	{
		return this; // todo
	}

	// Transformable.

	public Coords[] coordsGroupToTranslate() { return null; }
	
	public VisualSelect transform(Transform transformToApply)
	{
		return this; // todo
	}
}
