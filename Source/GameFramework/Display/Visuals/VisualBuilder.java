
package GameFramework.Display.Visuals;

import java.util.*;

import GameFramework.Display.*;
import GameFramework.Display.Visuals.Animation.*;
import GameFramework.Geometry.*;
import GameFramework.Geometry.Shapes.*;
import GameFramework.Geometry.Transforms.*;
import GameFramework.Model.*;

public class VisualBuilder
{
	public VisualBuilder()
	{}

	public static VisualBuilder _instance;
	public static VisualBuilder Instance()
	{
		if (VisualBuilder._instance == null)
		{
			VisualBuilder._instance = new VisualBuilder();
		}
		return VisualBuilder._instance;
	}

	public Visual circleWithEyes
	(
		double circleRadius, Color circleColor, double eyeRadius, Visual visualEyes
	)
	{
		visualEyes =
		(
			visualEyes != null
			? visualEyes
			: this.eyesBlinking(eyeRadius)
		);

		var visualEyesDirectional = new VisualDirectional
		(
			visualEyes, // visualForNoDirection
			new Visual[]
			{
				new VisualOffset(visualEyes, new Coords(1, 0, 0).multiplyScalar(eyeRadius)),
				new VisualOffset(visualEyes, new Coords(0, 1, 0).multiplyScalar(eyeRadius)),
				new VisualOffset(visualEyes, new Coords(-1, 0, 0).multiplyScalar(eyeRadius)),
				new VisualOffset(visualEyes, new Coords(0, -1, 0).multiplyScalar(eyeRadius))
			},
			null
		);

		Visual circleWithEyes = new VisualGroup
		(
			new Visual[]
			{
				VisualCircle.fromRadiusAndColorFill(circleRadius, circleColor),
				visualEyesDirectional
			}
		);

		circleWithEyes = new VisualOffset(circleWithEyes, new Coords(0, -circleRadius, 0));

		return circleWithEyes;
	}

	public Visual circleWithEyesAndLegs
	(
		double circleRadius, Color circleColor, double eyeRadius,
		Visual visualEyes
	)
	{
		var circleWithEyes =
			this.circleWithEyes(circleRadius, circleColor, eyeRadius, visualEyes);

		var lineThickness = 2;
		var spaceBetweenLegsHalf = eyeRadius * .75;
		var legLength = eyeRadius * 1.5;
		var legLengthHalf = legLength / 2;
		var footLength = eyeRadius;
		var footLengthHalf = footLength / 2;
		var offsetLegLeft = new Coords(-spaceBetweenLegsHalf, 0, 0);
		var offsetLegRight = new Coords(spaceBetweenLegsHalf, 0, 0);
		var ticksPerStep = 2;
		var isRepeating = true;

		var visualLegDownLeft = new VisualPath
		(
			new Path
			(
				new Coords[]
				{
					new Coords(0, -legLength, 0),
					new Coords(0, legLength, 0),
					new Coords(-footLengthHalf, legLength + footLengthHalf, 0)
				}
			),
			circleColor,
			lineThickness,
			false // isClosed
		);

		var visualLegDownRight = new VisualPath
		(
			new Path
			(
				new Coords[]
				{
					new Coords(0, -legLength, 0),
					new Coords(0, legLength, 0),
					new Coords(footLengthHalf, legLength + footLengthHalf, 0)
				}
			),
			circleColor,
			lineThickness,
			false // isClosed
		);

		var visualLegsFacingDownStanding = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset(visualLegDownLeft, offsetLegLeft),
				new VisualOffset(visualLegDownRight, offsetLegRight)
			}
		);

		var ticksPerStepAsArray = new int[] { ticksPerStep, ticksPerStep };

		var visualLegsFacingDownWalking = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							visualLegDownLeft,
							new VisualOffset
							(
								visualLegDownLeft,
								new Coords(0, -legLengthHalf, 0)
							)
						},
						isRepeating
					),
					offsetLegLeft
				),
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							new VisualOffset
							(
								visualLegDownRight,
								new Coords(0, -legLengthHalf, 0)
							),
							visualLegDownRight
						},
						isRepeating
					),
					offsetLegRight
				),
			}
		);

		var visualLegUpLeft = new VisualPath
		(
			new Path
			(
				new Coords[]
				{
					new Coords(0, -legLength, 0),
					new Coords(0, legLength, 0),
					new Coords(-footLengthHalf, legLength - footLengthHalf, 0)
				}
			),
			circleColor,
			lineThickness,
			false // isClosed
		);

		var visualLegUpRight = new VisualPath
		(
			new Path
			(
				new Coords[]
				{
					new Coords(0, -legLength, 0),
					new Coords(0, legLength, 0),
					new Coords(footLengthHalf, legLength - footLengthHalf, 0)
				}
			),
			circleColor,
			lineThickness,
			false // isClosed
		);

		var visualLegsFacingUpStanding = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset(visualLegUpLeft, offsetLegLeft),
				new VisualOffset(visualLegUpRight, offsetLegRight)
			}
		);

		var visualLegsFacingUpWalking = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							visualLegUpLeft,
							new VisualOffset
							(
								visualLegUpLeft,
								new Coords(0, -legLengthHalf, 0)
							)
						},
						isRepeating
					),
					offsetLegLeft
				),
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							new VisualOffset
							(
								visualLegUpRight,
								new Coords(0, -legLengthHalf, 0)
							),
							visualLegUpRight
						},
						isRepeating
					),
					offsetLegRight
				),
			}
		);

		var visualLegFacingLeft = new VisualPath
		(
			new Path
			(
				new Coords[]
				{
					new Coords(0, -legLength, 0),
					new Coords(0, legLength, 0),
					new Coords(-footLength, legLength, 0)
				}
			),
			circleColor,
			lineThickness,
			false // isClosed
		);

		var visualLegsFacingLeftStanding = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset(visualLegFacingLeft, offsetLegLeft),
				new VisualOffset(visualLegFacingLeft, offsetLegRight)
			}
		);

		var visualLegsFacingLeftWalking = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							visualLegFacingLeft,
							new VisualOffset
							(
								visualLegFacingLeft,
								new Coords(0, -legLengthHalf, 0)
							)
						},
						isRepeating
					),
					offsetLegLeft
				),
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							new VisualOffset
							(
								visualLegFacingLeft,
								new Coords(0, -legLengthHalf, 0)
							),
							visualLegFacingLeft
						},
						isRepeating
					),
					offsetLegRight
				),
			}
		);

		var visualLegFacingRight = new VisualPath
		(
			new Path
			(
				new Coords[]
				{
					new Coords(0, -legLength, 0),
					new Coords(0, legLength, 0),
					new Coords(footLength, legLength, 0)
				}
			),
			circleColor,
			lineThickness,
			false // isClosed
		);

		var visualLegsFacingRightStanding = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset(visualLegFacingRight, offsetLegLeft),
				new VisualOffset(visualLegFacingRight, offsetLegRight)
			}
		);

		var visualLegsFacingRightWalking = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							visualLegFacingRight,
							new VisualOffset
							(
								visualLegFacingRight,
								new Coords(0, -legLengthHalf, 0)
							)
						},
						isRepeating
					),
					offsetLegLeft
				),
				new VisualOffset
				(
					new VisualAnimation
					(
						null, // name
						ticksPerStepAsArray,
						new Visual[]
						{
							new VisualOffset
							(
								visualLegFacingRight,
								new Coords(0, -legLengthHalf, 0)
							),
							visualLegFacingRight
						},
						isRepeating
					),
					offsetLegRight
				)
			}
		);

		var visualLegsStandingNamesByHeading = new String[]
		{
			"FacingRightStanding",
			"FacingDownStanding",
			"FacingLeftStanding",
			"FacingUpStanding"
		};

		var visualLegsWalkingNamesByHeading = new String[]
		{
			"FacingRightWalking",
			"FacingDownWalking",
			"FacingLeftWalking",
			"FacingUpWalking"
		};

		var visualLegsDirectional = new VisualSelect
		(
			// childrenByName
			new HashMap<String,Visual>()
			{{
				put("FacingRightStanding", visualLegsFacingRightStanding);
				put("FacingDownStanding", visualLegsFacingDownStanding);
				put("FacingLeftStanding", visualLegsFacingLeftStanding); 
				put("FacingUpStanding", visualLegsFacingUpStanding);
				put("FacingRightWalking", visualLegsFacingRightWalking); 
				put("FacingDownWalking", visualLegsFacingDownWalking);
				put("FacingLeftWalking", visualLegsFacingLeftWalking); 
				put("FacingUpWalking", visualLegsFacingUpWalking);
			}},
			// selectChildNames
			(UniverseWorldPlaceEntities uwpe, Display d) ->
			{
				var e = uwpe.entity;
				var entityLoc = e.locatable().loc;
				var entityForward = entityLoc.orientation.forward;
				var entityForwardInTurns = entityForward.headingInTurns();
				String childNameToSelect;
				if (entityForwardInTurns == -1)
				{
					childNameToSelect = "FacingDownStanding";
				}
				else
				{
					var headingCount = 4;
					var headingIndex =
						(int)Math.floor(entityForwardInTurns * headingCount); // todo
					var entitySpeed = entityLoc.vel.magnitude();
					String[] namesByHeading;
					var speedMin = 0.2;
					if (entitySpeed > speedMin)
					{
						namesByHeading = visualLegsWalkingNamesByHeading;
					}
					else
					{
						namesByHeading = visualLegsStandingNamesByHeading;
					}
					childNameToSelect = namesByHeading[headingIndex];
				}
				return new String[] { childNameToSelect };
			}
		);

		var returnValue = new VisualGroup
		(
			new Visual[]
			{
				visualLegsDirectional,
				circleWithEyes
			}
		);

		return returnValue;
	}

	public Visual circleWithEyesAndLegsAndArms
	(
		double circleRadius, Color circleColor, double eyeRadius,
		Visual visualEyes
	)
	{
		var lineThickness = 2;

		var circleWithEyesAndLegs =
			this.circleWithEyesAndLegs(circleRadius, circleColor, eyeRadius, visualEyes);

		var visualNone = new VisualNone();
		Visual visualWieldable = new VisualDynamic
		(
			(UniverseWorldPlaceEntities uwpe) ->
			{
				var w = uwpe.world;
				var e = uwpe.entity;

				var equipmentUser = e.equipmentUser();
				var entityWieldableEquipped =
					equipmentUser.itemEntityInSocketWithName("Wielding");
				var itemDrawable = entityWieldableEquipped.drawable();
				var itemVisual =
				(
					itemDrawable == null
					? entityWieldableEquipped.item().defn(w).visual
					: itemDrawable.visual
				);
				return itemVisual;
			}
		);

		var orientationToAnchorTo = Orientation.Instances().ForwardXDownZ;

		visualWieldable = new VisualAnchor
		(
			visualWieldable, null, orientationToAnchorTo
		);

		var visualArmAndWieldableFacingRight = new VisualGroup
		(
			new Visual[]
			{
				// arm
				new VisualAnchor
				(
					new VisualLine
					(
						Coords.create(),
						new Coords(2, 1, 0).multiplyScalar(circleRadius),
						circleColor,
						lineThickness
					),
					null, orientationToAnchorTo
				),
				// wieldable
				new VisualOffset
				(
					visualWieldable,
					new Coords(2, 1, 0).multiplyScalar(circleRadius)
				)
			}
		);

		var visualArmAndWieldableFacingDown = new VisualGroup
		(
			new Visual[]
			{
				// arm
				new VisualAnchor
				(
					new VisualLine
					(
						Coords.create(),
						new Coords(-2, 0, 0).multiplyScalar(circleRadius),
						circleColor,
						lineThickness
					),
					null, orientationToAnchorTo
				),
				// wieldable
				new VisualOffset
				(
					visualWieldable,
					new Coords(-2, 0, 0).multiplyScalar(circleRadius)
				)
			}
		);

		var visualArmAndWieldableFacingLeft = new VisualGroup
		(
			new Visual[]
			{
				// arm
				new VisualAnchor
				(
					new VisualLine
					(
						Coords.create(),
						new Coords(-2, 1, 0).multiplyScalar(circleRadius),
						circleColor,
						lineThickness
					),
					null, orientationToAnchorTo
				),
				// wieldable
				new VisualOffset
				(
					visualWieldable,
					new Coords(-2, 1, 0).multiplyScalar(circleRadius)
				)
			}
		);

		var visualArmAndWieldableFacingUp = new VisualGroup
		(
			new Visual[]
			{
				// arm
				new VisualAnchor
				(
					new VisualLine
					(
						Coords.create(),
						new Coords(2, 0, 0).multiplyScalar(circleRadius),
						circleColor,
						lineThickness
					),
					null, orientationToAnchorTo
				),
				// wieldable
				new VisualOffset
				(
					visualWieldable,
					new Coords(2, 0, 0).multiplyScalar(circleRadius)
				)
			}
		);

		var visualArmAndWieldableDirectional = new VisualDirectional
		(
			visualArmAndWieldableFacingDown, // visualForNoDirection,
			new Visual[]
			{
				visualArmAndWieldableFacingRight,
				visualArmAndWieldableFacingDown,
				visualArmAndWieldableFacingLeft,
				visualArmAndWieldableFacingUp
			},
			null
		);

		var visualArmAndWieldableDirectionalOffset = new VisualOffset
		(
			visualArmAndWieldableDirectional,
			new Coords(0, 0 - circleRadius, 0)
		);

		var visualWielding = new VisualSelect
		(
			new HashMap<String,Visual>()
			{{
				put("Visible", visualArmAndWieldableDirectionalOffset),
				put("Hidden", visualNone)
			}},
			(UniverseWorldPlaceEntities uwpe, Display d) -> // selectChildNames
			{
				var e = uwpe.entity;
				var itemEntityWielded =
					e.equipmentUser().itemEntityInSocketWithName("Wielding");
				var returnValue =
					(itemEntityWielded == null ? "Hidden" : "Visible");
				return new String[] { returnValue };
			}
		);

		var returnValue = new VisualGroup
		(
			new Visual[]
			{
				visualWielding,
				circleWithEyesAndLegs
			}
		);

		return returnValue;
	}

	public Visual eyesBlinking(double visualEyeRadius)
	{
		var visualPupilRadius = visualEyeRadius / 2;

		var visualEye = new VisualGroup
		(
			new Visual[]
			{
				VisualCircle.fromRadiusAndColorFill(visualEyeRadius, Color.byName("White")),
				VisualCircle.fromRadiusAndColorFill(visualPupilRadius, Color.byName("Black"))
			}
		);

		var visualEyes = new VisualGroup
		(
			new Visual[]
			{
				new VisualOffset
				(
					visualEye, Coords.fromXY(-visualEyeRadius, 0)
				),
				new VisualOffset
				(
					visualEye, Coords.fromXY(visualEyeRadius, 0)
				)
			}
		);

		var visualEyesBlinking = new VisualAnimation
		(
			"EyesBlinking",
			new int[] { 50, 5 }, // ticksToHoldFrames
			new Visual[] { visualEyes, new VisualNone() },
			true // isRepeating?
		);

		return visualEyesBlinking;
	}

	public Visual flame(double dimension)
	{
		var dimensionHalf = dimension / 2;
		Visual flameVisualStatic = new VisualGroup
		(
			new Visual[]
			{
				VisualPolygon.fromPathAndColorFill
				(
					new Path
					(
						new Coords[]
						{
							new Coords(0, -dimension * 2, 0),
							new Coords(dimension, 0, 0),
							new Coords(-dimension, 0, 0),
						}
					),
					Color.byName("Orange")
				),
				VisualPolygon.fromPathAndColorFill
				(
					new Path
					(
						new Coords[]
						{
							new Coords(0, -dimension, 0),
							new Coords(dimensionHalf, 0, 0),
							new Coords(-dimensionHalf, 0, 0),
						}
					),
					Color.byName("Yellow")
				)
			}
		);

		var flameVisualStaticSmall = (VisualGroup)
		(
			flameVisualStatic.clone().transform
			(
				new Transform_Scale(new Coords(1, .8, 1))
			)
		);

		var flameVisualStaticLarge = (VisualGroup)
		(
			flameVisualStatic.clone().transform
			(
				new Transform_Scale(new Coords(1, 1.2, 1))
			)
		);

		var ticksPerFrame = 3;
		var flameVisual = new VisualAnimation
		(
			"Flame", // name
			new int[] { ticksPerFrame, ticksPerFrame, ticksPerFrame, ticksPerFrame },
			new Visual[]
			{
				flameVisualStaticSmall,
				flameVisualStatic,
				flameVisualStaticLarge,
				flameVisualStatic
			},
			true // isRepeating
		);

		return flameVisual;
	}

	public Visual ice(double dimension)
	{
		var dimensionHalf = dimension / 2;
		var color = Color.byName("Cyan");
		var visual = new VisualGroup
		(
			new Visual[]
			{
				VisualPolygon.fromPathAndColors
				(
					new Path
					(
						new Coords[]
						{
							new Coords(-1, -1, 0),
							new Coords(1, -1, 0),
							new Coords(1, 1, 0),
							new Coords(-1, 1, 0),
						}
					).transform
					(
						new Transform_Scale(new Coords(1, 1, 1).multiplyScalar(dimensionHalf))
					),
					null, // colorFill
					color // border
				),
			}
		);

		return visual;
	}

	public Visual sun(double dimension)
	{
		var color = Color.Instances().Yellow;
		var rayThickness = 1;
		var dimensionOblique = dimension * Math.sin(Math.PI / 4);
		var sunVisual = new VisualGroup
		(
			new Visual[]
			{
				new VisualLine
				(
					new Coords(-dimension, 0, 0), new Coords(dimension, 0, 0), color, rayThickness
				),
				new VisualLine
				(
					new Coords(0, -dimension, 0), new Coords(0, dimension, 0), color, rayThickness
				),
				new VisualLine
				(
					new Coords(-dimensionOblique, -dimensionOblique, 0),
					new Coords(dimensionOblique, dimensionOblique, 0),
					color, rayThickness
				),
				new VisualLine
				(
					new Coords(-dimensionOblique, dimensionOblique, 0),
					new Coords(dimensionOblique, -dimensionOblique, 0),
					color, rayThickness
				),

				VisualCircle.fromRadiusAndColorFill(dimension / 2, color)
			}
		);

		return sunVisual;
	}
}
