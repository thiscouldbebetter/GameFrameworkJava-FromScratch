
package GameFramework.Utility;

public class ClonableHelper
{
	public static <T> Clonable<T> clone(Clonable<T> clonableToClone)
	{
		return (clonableToClone == null ? null : clonableToClone.clone() );
	}
}
