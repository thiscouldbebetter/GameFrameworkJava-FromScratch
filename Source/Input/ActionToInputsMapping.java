package Input;

import java.util.*;

import Model.*;
import Model.Actors.*;
import Helpers.*;

public class ActionToInputsMapping
{
	public String actionName;
	public String[] inputNames;
	public boolean inactivateInputWhenActionPerformed;

	public ActionToInputsMapping
	(
		String actionName,
		String[] inputNames,
		boolean inactivateInputWhenActionPerformed
	)
	{
		this.actionName = actionName;
		this.inputNames = inputNames;
		this.inactivateInputWhenActionPerformed =
			inactivateInputWhenActionPerformed;
	}

	public static ActionToInputsMapping fromActionNameAndInputName
	(
		String actionName, String inputName
	)
	{
		return new ActionToInputsMapping
		(
			actionName, new String[] { inputName }, false
		);
	}

	// Static methods.

	public static Map<String,ActionToInputsMapping> mappingsToMappingsByInputName
	(
		ActionToInputsMapping[] mappings
	)
	{
		var mappingsByInputName =
			new HashMap<String,ActionToInputsMapping>();

		for (var i = 0; i < mappings.length; i++)
		{
			var mapping = mappings[i];
			var mappingInputNames = mapping.inputNames;
			for (var n = 0; n < mappingInputNames.length; n++)
			{
				var inputName = mappingInputNames[n];
				mappingsByInputName.put
				(
					inputName, mapping
				);
			}
		}

		return mappingsByInputName;
	}

	// Instance methods.

	public ActorAction action(Universe universe)
	{
		return universe.world.defn.actionByName(this.actionName);
	}

	// Cloneable implementation.

	public ActionToInputsMapping clone()
	{
		return new ActionToInputsMapping
		(
			this.actionName,
			ArrayHelper.clone(this.inputNames),
			this.inactivateInputWhenActionPerformed
		);
	}

	public ActionToInputsMapping overwriteWith
	(
		ActionToInputsMapping other
	) 
	{
		this.actionName = other.actionName;
		this.inputNames = ArrayHelper.clone(other.inputNames);
		this.inactivateInputWhenActionPerformed =
			other.inactivateInputWhenActionPerformed;
		return this;
	}
}
