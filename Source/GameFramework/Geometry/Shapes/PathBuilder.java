
package GameFramework.Geometry.Shapes;

import GameFramework.Geometry.*;

public class PathBuilder
{
	public Path star(int numberOfPoints, double ratioOfInnerRadiusToOuter)
	{
		var numberOfVertices = numberOfPoints * 2;
		var turnsPerVertex = 1 / numberOfVertices;
		var polar = new Polar(0, 1, 0);

		var vertices = [];
		for (var i = 0; i < numberOfVertices; i++)
		{
			polar.radius = (i % 2 == 0 ? 1ratioOfInnerRadiusToOuter  );
			var vertex = polar.toCoords( Coords.create() );
			vertices.push(vertex);
			polar.azimuthInTurns += turnsPerVertex;
		}

		var returnValue = new Path(vertices);
		return returnValue;
	}
}
