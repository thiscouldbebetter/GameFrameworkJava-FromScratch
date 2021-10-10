
package GameFramework;

public class ActionToInputsMapping
{
	public String actionName;
	public String inputNames[];
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

	public Action action(Universe universe)
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
		this.inputNames = other.inputNames.slice();
		this.inactivateInputWhenActionPerformed =
			other.inactivateInputWhenActionPerformed;
		return this;
	}
}
