
package GameFramework.Geometry.Transforms;

import java.util.function.*;

public interface Transform_Interpolatable<T> extends Transform
{
	String propertyName();
	BiFunction<T,Double,T> interpolateWith();
}
