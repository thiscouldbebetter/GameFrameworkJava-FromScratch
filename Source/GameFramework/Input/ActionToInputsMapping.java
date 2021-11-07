
package GameFramework.Input;

import java.util.*;

import GameFramework.Helpers.*;
import GameFramework.Model.*;
import GameFramework.Model.Actors.*;
import GameFramework.Utility.*;

public class ActionToInputsMapping implements Clonable<ActionToInputsMapping>
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
			ArrayHelper.cloneNonClonables(this.inputNames).toArray(new String[] {}),
			this.inactivateInputWhenActionPerformed
		);
	}

	public ActionToInputsMapping overwriteWith(ActionToInputsMapping other)
	{
		this.actionName = other.actionName;
		ArrayHelper.overwriteWithNonClonables(this.inputNames, other.inputNames);
		this.inactivateInputWhenActionPerformed =
			other.inactivateInputWhenActionPerformed;
		return this;
	}
}
