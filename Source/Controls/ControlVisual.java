
package Controls;

import Display.*;
import Display.Visuals.*;
import Geometry.*;
import Geometry.Transforms.*;
import Media.*;
import Model.*;
//import Model.Places.*;
//import Model.Physics.*;

public class ControlVisual<TContext,TVisual extends Visual> extends ControlBase
{
	public DataBinding<TContext,TVisual> visual;
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
		DataBinding<TContext,TVisual> visual,
		Color colorBackground,
		Color colorBorder
	)
	{
		super(name, pos, size, null);
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
				// todo - new Audible(),
				Locatable.fromPos(this._drawPos),
				Drawable.fromVisual(new VisualNone())
			}
		);
		this._sizeHalf = Coords.create();
	}

	public static <TContext,TVisual extends Visual> ControlVisual from4
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<TContext,TVisual> visual
	)
	{
		return new ControlVisual<TContext,TVisual>(name, pos, size, visual, null, null);
	}

	public static <TContext, TVisual extends Visual> ControlVisual from5
	(
		String name,
		Coords pos,
		Coords size,
		DataBinding<TContext,TVisual> visual,
		Color colorBackground
	)
	{
		return new ControlVisual<TContext,TVisual>(name, pos, size, visual, colorBackground, null);
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
