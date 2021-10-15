
package GameFramework.Utility;

public interface Clonable<T>
{
	T clone();
	T overwriteWith(T other);
}
