
package GameFramework.Geometry.Shapes.Maps;

import java.util.*;
import java.util.function.*;

import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class MapOfCells<T>
{
	public String name;
	public Coords sizeInCells;
	public Coords cellSize;
	private Supplier<T> _cellCreate;
	private Function<Triple<MapOfCells<T>,Coords,T>,T> _cellAtPosInCells;
	private Object cellSource;

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
		Object cellSource
	)
	{
		this.name = name;
		this.sizeInCells = sizeInCells;
		this.cellSize = cellSize;
		this._cellCreate = cellCreate;
		this._cellAtPosInCells = cellAtPosInCells;
		this.cellSource =
		(
			cellSource != null
			? cellSource
			: new ArrayList<T>()
		);

		this.sizeInCellsMinusOnes = this.sizeInCells.clone().subtract
		(
			Coords.Instances().Ones
		);
		this.size = this.sizeInCells.clone().multiply(this.cellSize);
		this.sizeHalf = this.size.clone().half();
		this.cellSizeHalf = this.cellSize.clone().half();

		// Helper variables.
		this._cell = this.cellCreate();
		this._posInCells = Coords.create();
		this._posInCellsMax = Coords.create();
		this._posInCellsMin = Coords.create();
	}

	public static <T> MapOfCells<T> fromNameSizeInCellsAndCellSize
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
		return this._cellAtPosInCells.apply
		(
			new Triple(this, cellPosInCells, this._cell)
		);
	}

	public T cellAtPosInCellsDefault(Triple<MapOfCells<T>,Coords,T> mapPosCell)
	{
		var map = mapPosCell.first;
		var cellPosInCells = mapPosCell.second;
		var cell = mapPosCell.third;

		var cellIndex = cellPosInCells.y * this.sizeInCells.x + cellPosInCells.x;
		var cell = ((T)(this.cellSource[cellIndex]));
		if (cell == null)
		{
			cell = this.cellCreate();
			this.cellSource[cellIndex] = cell;
		}
		return cell;
	}

	public T cellCreate()
	{
		return this._cellCreate.call();
	}

	public int cellsCount()
	{
		return this.sizeInCells.x * this.sizeInCells.y;
	}

	public List<T> cellsInBoxAddToList(Box box, List<T> cellsInBox)
	{
		ArrayHelper.clear(cellsInBox);

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

	public Entity[] cellsAsEntities
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

				var cellAsEntity = mapAndCellPosToEntity(this, cellPosInCells);

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
		this.cellSource.overwriteWith(other.cellSource);
		return this;
	}
}
