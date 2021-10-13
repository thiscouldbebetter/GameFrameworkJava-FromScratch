
package GameFramework.Utility;

public class Reference
{
	Object value;

	public Reference(Object value)
	{
		this.value = value;
	}

	public Object get()
	{
		return this.value;
	}

	public Object set(Object valueToSet)
	{
		this.value = valueToSet;
		return this.value;
	}
}
