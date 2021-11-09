
package GameFramework.Geometry.Transforms;

import java.util.function.*;

public interface Transform_Interpolatable<T> extends Transform
{
	String propertyName();
	Transform_Interpolatable<T> interpolateWith
	(
		Transform_Interpolatable<T> transformNext, double fractionOfProgressFromPrevToNext
	);
}
