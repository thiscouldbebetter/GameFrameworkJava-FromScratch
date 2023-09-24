
package Utility;

public class ReferenceDouble
{
	private double value;

	public ReferenceDouble(double value)
	{
		this.value = value;
	}

	public double get()
	{
		return this.value;
	}

	public double set(double valueToSet)
	{
		this.value = valueToSet;
		return this.value;
	}
}
