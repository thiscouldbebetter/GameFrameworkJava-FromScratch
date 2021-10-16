
package GameFramework.Input;

import java.util.*;

import GameFramework.Model.*;
import GameFramework.Model.Actors.*;

public class ActionToInputsMapping
{
	public String actionName;
	public List<String> inputNames;
	public boolean inactivateInputWhenActionPerformed;

	public ActionToInputsMapping
	(
		String actionName,
		String[] inputNames,
		boolean inactivateInputWhenActionPerformed
	)
	{
		this.actionName = actionName;
		this.inputNames = Arrays.asList(inputNames);
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

	public ActionToInputsMapping overwriteWith(ActionToInputsMapping other)
	{
		this.actionName = other.actionName;
		ArrayHelper.overwriteWith(this.inputNames, other.inputNames);
		this.inactivateInputWhenActionPerformed =
			other.inactivateInputWhenActionPerformed;
		return this;
	}
}
