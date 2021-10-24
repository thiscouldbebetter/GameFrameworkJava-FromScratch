
package GameFramework.Geometry.Transforms;

public interface Transformable
{
	Coords[] coordsGroupToTranslate();
	Transformable transform(Transform t);
}

