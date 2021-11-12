
package GameFramework.Utility;

public class ClonableHelper
{
	public static <T extends Clonable<T>> T clone(Clonable<T> clonableToClone)
	{
		return (clonableToClone == null ? null : clonableToClone.clone() );
	}
}
