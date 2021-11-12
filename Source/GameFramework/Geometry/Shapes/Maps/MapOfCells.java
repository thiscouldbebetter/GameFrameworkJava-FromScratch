
package GameFramework.Geometry.Shapes.Maps;

import java.util.*;
import java.util.function.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Helpers.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class MapOfCells<T extends Clonable<T>>
{
	public String name;
	public Coords sizeInCells;
	public Coords cellSize;
	public Supplier<T> cellCreate;
	public Function<Triple<MapOfCells<T>,Coords,T>,T> _cellAtPosInCells;
	public List<T> cellSource;

	public Coords cellSizeHalf;
	public Coords size;
	public Coords sizeHalf;
	public Coords sizeInCellsMinusOnes;

	private T _cell;
	private Coords _posInCells;
	private Coords _posInCellsMax;
	private Coords _posInCellsMin;

	public MapOfCells
	(
		String name,
		Coords sizeInCells,
		Coords cellSize,
		Supplier<T> cellCreate,
		Function<Triple<MapOfCells<T>,Coords,T>,T> cellAtPosInCells,
		List<T> cellSource
	)
	{
		this.name = name;
		this.sizeInCells = sizeInCells;
		this.cellSize = cellSize;
		this.cellCreate = cellCreate;
		this._cellAtPosInCells =
		(
			cellAtPosInCells != null
			? cellAtPosInCells
			: (Triple<MapOfCells<T>,Coords,T> x) ->
				this.cellAtPosInCellsDefault(x.first, x.second, x.third)
		);
		this.cellSource = cellSource;

		this.sizeInCellsMinusOnes = this.sizeInCells.clone().subtract
		(
			Coords.Instances().Ones
		);
		this.size = this.sizeInCells.clone().multiply(this.cellSize);
		this.sizeHalf = this.size.clone().half();
		this.cellSizeHalf = this.cellSize.clone().half();

		// Helper variables.
		this._cell = cellCreate.get();
		this._posInCells = Coords.create();
		this._posInCellsMax = Coords.create();
		this._posInCellsMin = Coords.create();
	}

	public static <T extends Clonable<T>> MapOfCells<T> fromNameSizeInCellsAndCellSize
	(
		String name, Coords sizeInCells, Coords cellSize
	)
	{
		return new MapOfCells(name, sizeInCells, cellSize, null, null, null);
	}

	public T cellAtPos(Coords pos)
	{
		this._posInCells.overwriteWith(pos).divide(this.cellSize).floor();
		return this.cellAtPosInCells(this._posInCells);
	}

	public T cellAtPosInCells(Coords cellPosInCells)
	{
		var argument = new Triple<MapOfCells<T>,Coords,T>
		(
			this, cellPosInCells, this._cell
		);
		var returnValue = this._cellAtPosInCells.apply(argument);
		return returnValue;
	}

	public T cellAtPosInCellsDefault
	(
		MapOfCells<T> map, Coords cellPosInCells, T cell
	)
	{
		var cellIndex = (int)
		(
			cellPosInCells.y * this.sizeInCells.x + cellPosInCells.x
		);
		cell = this.cellSource.get(cellIndex);
		if (cell == null)
		{
			cell = this.cellCreate.get();
			this.cellSource.add(cellIndex, cell);
		}
		return cell;
	}

	public int cellsCount()
	{
		return (int)(this.sizeInCells.x * this.sizeInCells.y);
	}

	public List<T> cellsInBoxAddToList(Box box, List<T> cellsInBox)
	{
		cellsInBox.clear();

		var minPosInCells = this._posInCellsMin.overwriteWith
		(
			box.min()
		).divide
		(
			this.cellSize
		).floor().trimToRangeMax
		(
			this.sizeInCellsMinusOnes
		);

		var maxPosInCells = this._posInCellsMax.overwriteWith
		(
			box.max()
		).divide
		(
			this.cellSize
		).floor().trimToRangeMax
		(
			this.sizeInCellsMinusOnes
		);

		var cellPosInCells = this._posInCells;
		for (var y = minPosInCells.y; y <= maxPosInCells.y; y++)
		{
			cellPosInCells.y = y;

			for (var x = minPosInCells.x; x <= maxPosInCells.x; x++)
			{
				cellPosInCells.x = x;

				var cellAtPos = this.cellAtPosInCells(cellPosInCells);
				cellsInBox.add(cellAtPos);
			}
		}

		return cellsInBox;
	}

	public List<Entity> cellsAsEntities
	(
		BiFunction<MapOfCells<T>,Coords,Entity> mapAndCellPosToEntity
	)
	{
		var returnValues = new ArrayList<Entity>();

		var cellPosInCells = Coords.create();
		var cellPosStart = Coords.create();
		var cellPosEnd = this.sizeInCells;

		for (var y = cellPosStart.y; y < cellPosEnd.y; y++)
		{
			cellPosInCells.y = y;

			for (var x = cellPosStart.x; x < cellPosEnd.x; x++)
			{
				cellPosInCells.x = x;

				var cellAsEntity =
					mapAndCellPosToEntity.apply(this, cellPosInCells);

				returnValues.add(cellAsEntity);
			}
		}

		return returnValues;
	}

	// cloneable

	public MapOfCells<T> clone()
	{
		return new MapOfCells<T>
		(
			this.name,
			this.sizeInCells,
			this.cellSize,
			this.cellCreate,
			this._cellAtPosInCells,
			this.cellSource
		);
	}

	public MapOfCells<T> overwriteWith(MapOfCells<T> other)
	{
		ArrayHelper.overwriteWith(this.cellSource, other.cellSource);
		return this;
	}
}
