
package GameFramework.Input;


public class UserInputListener extends Entity
{
	public UserInputListener()
	{
		super
		(
			UserInputListener.name,
			[
				Actor.fromActivityDefnName
				(
					UserInputListener.activityDefnHandleUserInputBuild().name
				),

				Drawable.fromVisual
				(
					UserInputListener.visualBuild()
				),

				Selector.fromCursorDimension(20)
			]
		);
	}

	public static ActivityDefn activityDefnHandleUserInputBuild()
	{
		return new ActivityDefn
		(
			"HandleUserInput",
			UserInputListener.activityDefnHandleUserInputPerform
		);
	}

	public static void activityDefnHandleUserInputPerform
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		var universe = uwpe.universe;
		var world = uwpe.world;
		var place = uwpe.place;

		var inputHelper = universe.inputHelper;

		var placeDefn = place.defn(world);
		var actionsByName = placeDefn.actionsByName;
		var actionToInputsMappingsByInputName =
			placeDefn.actionToInputsMappingsByInputName;

		var actionsToPerform = inputHelper.actionsFromInput
		(
			actionsByName, actionToInputsMappingsByInputName
		);

		for (var i = 0; i < actionsToPerform.length; i++)
		{
			var action = actionsToPerform[i];
			action.perform(uwpe);
		}
	}

	public static Visual visualBuild()
	{
		var returnValue = new VisualSelect
		(
			// childrenByNames
			new HashMap<string, Visual>()
			{{
				put("None", new VisualNone() )
			}},
			// selectChildNames
			(UniverseWorldPlaceEntities uwpe, Display d) ->
			{
				return new String[] { "None" };
			}
		);

		return returnValue;
	}
}
