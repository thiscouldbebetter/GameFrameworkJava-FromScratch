
package Utility;

public interface Interpolatable<T>
{
	T interpolateWith(T other, double fractionOfProgressTowardOther);
}
