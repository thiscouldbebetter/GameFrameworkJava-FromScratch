
package GameFramework.Geometry;

public class Orientation
{
	public Coords forward;
	public Coords down;
	public Coords right;

	public Coords[] axes;
	public Coords[] axesRDF;

	public Orientation(Coords forward, Coords down)
	{
		this.forward = (forward != null ? forward : new Coords(1, 0, 0));
		this.forward = this.forward.clone().normalize();
		down = (down != null ? down : new Coords(0, 0, 1));
		this.right = down.clone().crossProduct(this.forward).normalize();
		this.down = this.forward.clone().crossProduct(this.right).normalize();

		this.axes = new Coords[] { this.forward, this.right, this.down };
		this.axesRDF = new Coords[] { this.right, this.down, this.forward };
	}

	public static Orientation _default()
	{
		return new Orientation(new Coords(1, 0, 0), new Coords(0, 0, 1) );
	}

	public static Orientation fromForward(Coords forward)
	{
		return new Orientation(forward, new Coords(0, 0, 1) ); 
	}

	public Orientation toDefault()
	{
		var coordsInstances = Coords.Instances();
		this.forwardDownSet(coordsInstances.OneZeroZero, coordsInstances.ZeroZeroOne);
		return this;
	}

	// instances

	public static Orientation_Instances _instances;
	public static Orientation_Instances Instances()
	{
		if (Orientation._instances == null)
		{
			Orientation._instances = new Orientation_Instances();
		}
		return Orientation._instances;
	}


	// methods

	public Orientation clone()
	{
		return new Orientation(this.forward.clone(), this.down.clone());
	}

	public boolean equals(Orientation other)
	{
		var returnValue =
		(
			this.forward.equals(other.forward)
			&& this.right.equals(other.right)
			&& this.down.equals(other.down)
		);
		return returnValue;
	}

	public Orientation forwardSet(Coords value)
	{
		this.forward.overwriteWith(value);
		return this.orthogonalize();
	}

	public Orientation forwardDownSet(Coords forward, Coords down)
	{
		this.forward.overwriteWith(forward);
		this.down.overwriteWith(down);
		return this.orthogonalize();
	}

	public Orientation orthogonalize()
	{
		this.forward.normalize();
		this.right.overwriteWith(this.down).crossProduct(this.forward).normalize();
		this.down.overwriteWith(this.forward).crossProduct(this.right).normalize();
		return this;
	}

	public Orientation overwriteWith(Orientation other)
	{
		this.forward.overwriteWith(other.forward);
		this.right.overwriteWith(other.right);
		this.down.overwriteWith(other.down);
		return this;
	}

	public Coords projectCoords(Coords coordsToProject)
	{
		coordsToProject.overwriteWithDimensions
		(
			coordsToProject.dotProduct(this.forward),
			coordsToProject.dotProduct(this.right),
			coordsToProject.dotProduct(this.down)
		);
		return coordsToProject;
	}

	public Coords unprojectCoords(Coords coordsToUnproject)
	{
		var returnValue = Coords.create();

		var axisScaled = Coords.create();

		for (var i = 0; i < this.axes.length; i++)
		{
			var axis = this.axes[i];

			axisScaled.overwriteWith(axis).multiplyScalar
			(
				coordsToUnproject.dimensionGet(i)
			);

			returnValue.add(axisScaled);
		}

		return coordsToUnproject.overwriteWith(returnValue);
	}

	public Coords projectCoordsRDF(Coords coordsToProject)
	{
		coordsToProject.overwriteWithDimensions
		(
			coordsToProject.dotProduct(this.right),
			coordsToProject.dotProduct(this.down),
			coordsToProject.dotProduct(this.forward)
		);
		return coordsToProject;
	}

	public Coords unprojectCoordsRDF(Coords coordsToUnproject)
	{
		var returnValue = Coords.create();

		var axisScaled = Coords.create();

		for (var i = 0; i < this.axesRDF.length; i++)
		{
			var axis = this.axesRDF[i];

			axisScaled.overwriteWith(axis).multiplyScalar
			(
				coordsToUnproject.dimensionGet(i)
			);

			returnValue.add(axisScaled);
		}

		return coordsToUnproject.overwriteWith(returnValue);
	}
}
