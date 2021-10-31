
package GameFramework.Geometry.Shapes.Maps;

import GameFramework.Geometry.*;
import GameFramework.Utility.*;

public abstract class MapOfCellsCellSource<TCell> implements Clonable<MapOfCellsCellSource<TCell>>
{
	public abstract TCell cellAtPosInCells
	(
		MapOfCells<TCell> map, Coords cellPosInCells, TCell cellToOverwrite
	);

	public abstract TCell cellCreate();

}

