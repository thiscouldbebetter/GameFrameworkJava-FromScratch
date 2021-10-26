
package GameFramework.Utility;

public class Reference<T>
{
	private T value;

	public Reference(T value)
	{
		this.value = value;
	}

	public T get()
	{
		return this.value;
	}

	public T set(T valueToSet)
	{
		this.value = valueToSet;
		return this.value;
	}
}
