
package GameFramework.Geometry.Shapes.Maps;

import GameFramework.Geometry.*;

public class MapOfCellsCellSourceArray<TCell> extends MapOfCellsCellSource<TCell>
{
	private T[] cells;

	public MapOfCellsCellSourceArray(T[] cells)
	{
		this.cells = cells;
	}

	public TCell cellAtPosInCells
	(
		MapOfCells<TCell> map, Coords cellPosInCells, TCell cellToOverwrite
	)
	{
		var cellIndex = cellPosInCells.y * this.sizeInCells.x + cellPosInCells.x;
		var cell = ((T)(this.cellSource[cellIndex]));
		if (cell == null)
		{
			cell = this.cellCreate();
			this.cellSource[cellIndex] = cell;
		}
		return cell;
	}

	public TCell cellCreate()
	{
		return new TCell();
	}

	// Clonable.

	public MapOfCellsCellSource<TCell> clone()
	{
		return this; // todo
	}

	public MapOfCellsCellSource<TCell> overwriteWith
	(
		MapOfCellsCellSource<TCell> other
	)
	{
		return this; // todo
	}

}
