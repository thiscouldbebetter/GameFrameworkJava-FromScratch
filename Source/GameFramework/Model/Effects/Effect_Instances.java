package GameFramework.Model.Effects;

import java.util.*;

import GameFramework.Display.*;
import GameFramework.Display.Visuals.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;
import GameFramework.Model.Combat.*;

public class Effect_Instances
{
	public Effect Burning;
	public Effect Frozen;
	public Effect Healing;

	public Effect[] _All;
	public Map<String,Effect> _AllByName;

	public Effect_Instances()
	{
		var visualDimension = 5;
		this.Burning = new Effect
		(
			"Burning",
			20, // ticksPerCycle
			5, // cyclesToLive
			VisualBuilder.Instance().flame(visualDimension),
			(UniverseWorldPlaceEntities uwpe, Effect effect) ->
			{
				var damage = Damage.fromAmountAndTypeName(1, "Heat");
				var e = uwpe.entity;
				uwpe.entity2 = e;
				e.killable().damageApply(uwpe, damage );
			}
		);

		this.Frozen = new Effect
		(
			"Frozen",
			20, // ticksPerCycle
			5, // cyclesToLive
			VisualCircle.fromRadiusAndColorFill(visualDimension, Color.byName("Cyan")),
			(UniverseWorldPlaceEntities uwpe, Effect effect) ->
			{
				var damage = Damage.fromAmountAndTypeName(1, "Cold");
				var e = uwpe.entity;
				uwpe.entity2 = e;
				e.killable().damageApply(uwpe, damage );
			}
		);

		this.Healing = new Effect
		(
			"Healing",
			40, // ticksPerCycle
			10, // cyclesToLive
			VisualPolygon.fromPathAndColorFill
			(
				new Path
				(
					new Coords[]
					{
						new Coords(-0.5, -0.2, 0),
						new Coords(-0.2, -0.2, 0),
						new Coords(-0.2, -0.5, 0),
						new Coords(0.2, -0.5, 0),
						new Coords(0.2, -0.2, 0),
						new Coords(0.5, -0.2, 0),
						new Coords(0.5, 0.2, 0),
						new Coords(0.2, 0.2, 0),
						new Coords(0.2, 0.5, 0),
						new Coords(-0.2, 0.5, 0),
						new Coords(-0.2, 0.2, 0),
						new Coords(-0.5, 0.2, 0)
					}
				).transform
				(
					Transform_Scale.fromScalar(visualDimension * 1.5)
				),
				Color.byName("Red")
			),
			(UniverseWorldPlaceEntities uwpe, Effect effect) ->
			{
				var damage = Damage.fromAmountAndTypeName(-1, "Healing");
				var e = uwpe.entity;
				uwpe.entity2 = e;
				e.killable().damageApply(uwpe, damage);
			}
		);

		this._All = new Effect[]
		{
			this.Burning,
			this.Frozen,
			this.Healing
		};

		this._AllByName = ArrayHelper.addLookupsByName(this._All);
	}
}
