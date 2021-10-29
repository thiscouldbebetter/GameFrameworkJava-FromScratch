
package GameFramework.Model;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import GameFramework.Controls.*;
import GameFramework.Display.*;
import GameFramework.Display.Visuals*;
import GameFramework.Geometry.*;
import GameFramework.Helpers.*;
import GameFramework.Model.Actors.*;
import GameFramework.Model.Physics.*;
import GameFramework.Utility.*;

public class Selector implements EntityProperty, Clonable<Selector> 
{
	public double cursorDimension;
	private Consumer<UniverseWorldPlaceEntities> _entitySelect;
	private Consumer<UniverseWorldPlaceEntities> _entityDeselect;

	public List<Entity> entitiesSelected;

	public ControlBase _control;
	public Entity entityForCursor;
	public Entity entityForHalo;

	public Selector
	(
		double cursorDimension,
		Consumer<UniverseWorldPlaceEntities> entitySelect,
		Consumer<UniverseWorldPlaceEntities> entityDeselect
	)
	{
		this.cursorDimension = cursorDimension;
		this._entitySelect = entitySelect;
		this._entityDeselect = entityDeselect;

		this.entitiesSelected = new ArrayList<Entity>();

		var cursorRadius = this.cursorDimension / 2;
		var visualCursor = new VisualGroup
		(
			new Visual[]
			{
				new VisualCircle
				(
					cursorRadius, // radius
					null, // colorFill
					Color.Instances().White, // colorBorder
					1 // borderWidth
				),
				VisualCrosshairs.fromRadiiOuterAndInner
				(
					cursorRadius, cursorRadius / 2
				)
			}
		);

		this.entityForCursor = new Entity
		(
			"Cursor",
			new EntityProperty[]
			{
				Drawable.fromVisualAndIsVisible(visualCursor, false),
				Locatable.create()
			}
		);

		var visualHalo = visualCursor;
		this.entityForHalo = new Entity
		(
			"Halo",
			new EntityProperty[]
			{
				Drawable.fromVisualAndIsVisible(visualHalo, false),
				Locatable.create()
			}
		);

	}

	public static Selector _default()
	{
		return new Selector(20, null, null);
	}

	public static Selector fromCursorDimension(double cursorDimension)
	{
		return new Selector(cursorDimension, null, null);
	}

	public static ActorAction actionEntityAtMouseClickPosSelect()
	{
		return new ActorAction
		(
			"Recording Start/Stop",
			Selector.actionEntityAtMouseClickPosSelectPerform
		);
	}

	public static void actionEntityAtMouseClickPosSelectPerform
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		var selector = uwpe.entity.selector();
		selector.entityAtMouseClickPosSelect(uwpe);
	}

	public void entitiesDeselectAll(UniverseWorldPlaceEntities uwpe)
	{
		this.entitiesSelected.forEach
		(
			(Entity x) -> this.entityDeselect(uwpe.entity2Set(x) )
		);
	}

	public void entityDeselect(UniverseWorldPlaceEntities uwpe)
	{
		var entityToDeselect = uwpe.entity2;
		ArrayHelper.remove(this.entitiesSelected, entityToDeselect);

		if (this._entityDeselect != null)
		{
			this._entityDeselect.accept(uwpe);
		}

		var selectable = entityToDeselect.selectable();
		if (selectable != null)
		{
			selectable.deselect(uwpe);
		}
	}

	public void entitySelect(UniverseWorldPlaceEntities uwpe)
	{
		var entityToSelect = uwpe.entity2;
		this.entitiesSelected.add(entityToSelect);

		if (this._entitySelect != null)
		{
			this._entitySelect.accept(uwpe);
		}

		var selectable = entityToSelect.selectable();
		if (selectable != null)
		{
			selectable.select(uwpe);
		}
	}

	public Entity entityAtMouseClickPosSelect
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		var place = uwpe.place;

		var mousePosAbsolute = this.mouseClickPosAbsoluteGet(uwpe);

		var entitiesInPlace = place.entities;
		var range = this.cursorDimension / 2;
		var entityToSelect = entitiesInPlace.stream().filter
		(
			x ->
			{
				var locatable = x.locatable();
				var entityNotAlreadySelectedInRange =
				(
					this.entitiesSelected.indexOf(x) == -1
					&& locatable != null
					&& locatable.distanceFromPos(mousePosAbsolute) < range
				);
				return entityNotAlreadySelectedInRange;
			}
		).sort
		(
			(Entity a, Entity b) ->
				a.locatable().distanceFromPos(mousePosAbsolute)
				- b.locatable().distanceFromPos(mousePosAbsolute)
		)[0];

		this.entitiesDeselectAll(uwpe);
		if (entityToSelect != null)
		{
			uwpe.entity2 = entityToSelect;
			this.entitySelect(uwpe);
		}

		return entityToSelect;
	}

	public Coords mouseClickPosAbsoluteGet(UniverseWorldPlaceEntities uwpe)
	{
		return this.mousePosConvertToAbsolute
		(
			uwpe,
			uwpe.universe.inputHelper.mouseClickPos
		);
	}

	public Coords mouseMovePosAbsoluteGet(UniverseWorldPlaceEntities uwpe)
	{
		return this.mousePosConvertToAbsolute
		(
			uwpe,
			uwpe.universe.inputHelper.mouseMovePos
		);
	}

	private Coords mousePosConvertToAbsolute
	(
		UniverseWorldPlaceEntities uwpe,
		Coords mousePosRelativeToCameraView
	)
	{
		var mousePosAbsolute = mousePosRelativeToCameraView.clone();

		var cameraEntity = uwpe.place.camera();

		if (cameraEntity != null)
		{
			var camera = cameraEntity.camera();

			mousePosAbsolute.divide
			(
				uwpe.universe.display.scaleFactor()
			).add
			(
				camera.loc.pos
			).subtract
			(
				camera.viewSizeHalf
			).clearZ();
		}

		return mousePosAbsolute;
	} 

	// Clonable.

	public Selector clone()
	{
		return new Selector
		(
			this.cursorDimension, this._entitySelect, this._entityDeselect
		);
	}

	public Selector overwriteWith(Selector other)
	{
		this.cursorDimension = other.cursorDimension;
		this._entitySelect = other._entitySelect;
		return this;
	}

	// Controllable.

	public ControlBase toControl(Coords size, Coords pos)
	{
		var fontHeightInPixels = 12.0;
		var margin = fontHeightInPixels / 2;

		var labelSize = Coords.fromXY(size.x, fontHeightInPixels);

		var selectionAsContainer = new ControlContainer
		(
			"visualPlayerSelection",
			pos,
			size,
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelSelected",
					Coords.fromXY(1, 0).multiplyScalar(margin), // pos
					labelSize,
					false, // isTextCentered
					"Selected:",
					fontHeightInPixels
				),

				new ControlLabel
				(
					"textEntitySelectedName",
					Coords.fromXY(1, 1.5).multiplyScalar(margin), // pos
					labelSize,
					false, // isTextCentered
					DataBinding.fromContextAndGet
					(
						this,
						(Selector c) ->
							(
								c.entitiesSelected.size() == 0
								? "-"
								: c.entitiesSelected.get(0).name
							)
					),
					fontHeightInPixels
				)
			},
			null, null
		);

		var controlSelection =
			new ControlContainerTransparent(selectionAsContainer);

		this._control = controlSelection;

		return this._control;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe) {}

	public void initialize(UniverseWorldPlaceEntities uwpe)
	{
		var place = uwpe.place;
		place.entityToSpawnAdd(this.entityForCursor);
	}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		var cursorPos = this.entityForCursor.locatable().loc.pos;
		var mousePosAbsolute = this.mouseMovePosAbsoluteGet(uwpe);
		cursorPos.overwriteWith(mousePosAbsolute);

		var entitySelected = this.entitiesSelected[0];
		var isEntitySelected = (entitySelected != null);
		if (isEntitySelected)
		{
			var haloLoc = this.entityForHalo.locatable().loc;
			var entitySelectedLoc = entitySelected.locatable().loc;
			haloLoc.overwriteWith(entitySelectedLoc);
			haloLoc.pos.z--;
			var uwpeHalo = uwpe.clone().entitySet(this.entityForHalo);
			this.entityForHalo.drawable().updateForTimerTick(uwpeHalo);
		}

		if (this._control != null)
		{
			this._control._isVisible = isEntitySelected;
		}
	}
}
