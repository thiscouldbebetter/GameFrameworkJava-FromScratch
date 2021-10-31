
package GameFramework.Model.Places;

import java.util.*;
import java.util.function.*;

import GameFramework.Helpers.*;
import GameFramework.Input.*;
import GameFramework.Model.*;
import GameFramework.Model.Actors.*;

public class PlaceDefn
{
	public String name;
	public ActorAction[] actions;
	public Map<String,ActorAction> actionsByName;
	public ActionToInputsMapping[] actionToInputsMappings;
	public String[] propertyNamesToProcess;
	private Consumer<UniverseWorldPlaceEntities>_placeInitialize;
	private Consumer<UniverseWorldPlaceEntities>_placeFinalize;

	public Map<String,ActionToInputsMapping> actionToInputsMappingsByInputName;
	public ActionToInputsMapping actionToInputsMappingSelected;
	public ActionToInputsMapping[] actionToInputsMappingsDefault;
	public ActionToInputsMapping[] actionToInputsMappingsEdited;

	public PlaceDefn
	(
		String name,
		ActorAction[] actions,
		ActionToInputsMapping[] actionToInputsMappings,
		String[] propertyNamesToProcess,
		Consumer<UniverseWorldPlaceEntities> placeInitialize,
		Consumer<UniverseWorldPlaceEntities> placeFinalize
	)
	{
		this.name = name;
		this.actions = actions;
		this.actionsByName = ArrayHelper.addLookupsByName(this.actions);
		this.actionToInputsMappingsDefault = actionToInputsMappings;
		this.propertyNamesToProcess = propertyNamesToProcess;
		this._placeInitialize = placeInitialize;
		this._placeFinalize = placeFinalize;

		this.actionToInputsMappings =
			ArrayHelper.clone(this.actionToInputsMappingsDefault);
		this.actionToInputsMappingsEdited =
			ArrayHelper.clone(this.actionToInputsMappings);

		this.actionToInputsMappingsByInputName = ArrayHelper.addLookupsMultiple
		(
			Arrays.asList(this.actionToInputsMappings),
			(ActionToInputsMapping x) -> x.inputNames
		);
	}

	public static PlaceDefn _default()
	{
		return new PlaceDefn
		(
			"Default", // name,
			new ActorAction[] {}, // actions,
			new ActionToInputsMapping[] {}, // actionToInputsMappings,
			new String[] {}, // propertyNamesToProcess,
			null, // placeInitialize
			null // placeFinalize
		);
	}

	public static PlaceDefn from4
	(
		String name,
		ActorAction[] actions,
		ActionToInputsMapping[] actionToInputsMappings,
		String[] propertyNamesToProcess
	)
	{
		return new PlaceDefn
		(
			name,
			actions,
			actionToInputsMappings,
			propertyNamesToProcess,
			null, null // placeInitialize, placeFinalize
		);
	}

	public void actionToInputsMappingsEdit()
	{
		ArrayHelper.overwriteWith
		(
			this.actionToInputsMappingsEdited,
			this.actionToInputsMappings
		);

		this.actionToInputsMappingSelected = null;
	}

	public void actionToInputsMappingsRestoreDefaults()
	{
		ArrayHelper.overwriteWith
		(
			this.actionToInputsMappingsEdited,
			this.actionToInputsMappingsDefault
		);
	}

	public void actionToInputsMappingsSave()
	{
		this.actionToInputsMappings = ArrayHelper.clone
		(
			this.actionToInputsMappingsEdited
		);
		this.actionToInputsMappingsByInputName = ArrayHelper.addLookupsMultiple
		(
			Arrays.asList(this.actionToInputsMappings),
			(ActionToInputsMapping x) -> x.inputNames
		);
	}

	public void placeFinalize(UniverseWorldPlaceEntities uwpe)
	{
		if (this._placeFinalize != null)
		{
			this._placeFinalize.accept(uwpe);
		}
	}

	public void placeInitialize(UniverseWorldPlaceEntities uwpe)
	{
		if (this._placeInitialize != null)
		{
			this._placeInitialize.accept(uwpe);
		}
	}
}
