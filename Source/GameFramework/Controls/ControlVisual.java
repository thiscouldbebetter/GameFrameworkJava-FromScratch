
package GameFramework.Controls;

import GameFramework.Display.*;
import GameFramework.Display.Visuals.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Media.*;
import GameFramework.Model.*;
import GameFramework.Model.Places.*;
import GameFramework.Model.Physics.*;

public class ControlVisual extends ControlBase
{
	public DataBinding<Object,Visual> visual;
	public Color colorBackground;
	public Color colorBorder;

	private Coords _drawPos;
	private Entity _entity;
	private Coords _sizeHalf;

	public ControlVisual
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object, Visual> visual,
		Color colorBackground,
		Color colorBorder
	)
	{
		super(name, pos, size, -1);
		this.visual = visual;
		this.colorBackground = colorBackground;
		this.colorBorder = colorBorder;

		// Helper variables.
		this._drawPos = Coords.create();
		this._entity = new Entity
		(
			this.name(),
			new EntityProperty[]
			{
				new Audible(),
				Locatable.fromPos(this._drawPos),
				Drawable.fromVisual(new VisualNone())
			}
		);
		this._sizeHalf = Coords.create();
	}

	public static ControlVisual from4
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object,Visual> visual
	)
	{
		return new ControlVisual(name, pos, size, visual, null, null);
	}

	public static ControlVisual from5
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<Object, Visual> visual,
		Color colorBackground
	)
	{
		return new ControlVisual(name, pos, size, visual, colorBackground, null);
	}

	public boolean actionHandle(String actionName, Universe universe)
	{
		return false;
	}

	public boolean isEnabled()
	{
		return false;
	}

	public boolean mouseClick(Coords x)
	{
		return false;
	}

	public ControlBase scalePosAndSize(Coords scaleFactors)
	{
		this.pos.multiply(scaleFactors);
		this.size.multiply(scaleFactors);
		this._sizeHalf.multiply(scaleFactors);
		return this;
	}

	// drawable

	public void draw
	(
		Universe universe, Display display, Disposition drawLoc,
		ControlStyle style
	)
	{
		var visualToDraw = this.visual.get();
		if (visualToDraw != null)
		{
			var drawPos = this._drawPos.overwriteWith(drawLoc.pos).add(this.pos);
			style = (style != null ? style : this.style(universe));

			var colorFill =
			(
				this.colorBackground != null
				? this.colorBackground
				: Color.Instances()._Transparent
			);
			var colorBorder =
			(
				this.colorBorder != null
				? this.colorBorder
				: style.colorBorder
			);
			display.drawRectangle
			(
				drawPos, this.size, colorFill, colorBorder, false
			);

			this._sizeHalf.overwriteWith(this.size).half();
			drawPos.add(this._sizeHalf);
			var entity = this._entity;
			entity.locatable().loc.pos.overwriteWith(drawPos);

			var world = universe.world;
			var place = (world == null ? null : world.placeCurrent);
			var uwpe = new UniverseWorldPlaceEntities
			(
				universe, world, place, entity, null
			);
			visualToDraw.draw(uwpe, display);
		}
	}
}
