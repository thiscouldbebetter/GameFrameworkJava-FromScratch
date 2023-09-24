
package Geometry;

public class Polar
{
	public double azimuthInTurns;
	public double radius;
	public double elevationInTurns;

	public Polar(double azimuthInTurns, double radius, double elevationInTurns)
	{
		this.azimuthInTurns = azimuthInTurns;
		this.radius = radius;
		this.elevationInTurns = elevationInTurns;
	}

	public static Polar create()
	{
		return new Polar(0, 1, 0);
	}

	public static Polar fromAzimuthInTurns(double azimuthInTurns)
	{
		return new Polar(azimuthInTurns, 1, 0);
	}

	// constants

	public static double DegreesPerTurn = 360;
	public static double RadiansPerTurn = Math.PI * 2;

	// instance methods

	public Polar addToAzimuthInTurns(double turnsToAdd)
	{
		this.azimuthInTurns += turnsToAdd;
		return this;
	}

	public Polar fromCoords(Coords coordsToConvert)
	{
		this.azimuthInTurns =
			Math.atan2(coordsToConvert.y, coordsToConvert.x)
			/ Polar.RadiansPerTurn;

		if (this.azimuthInTurns < 0)
		{
			this.azimuthInTurns += 1;
		}

		this.radius = coordsToConvert.magnitude();

		if (this.radius == 0)
		{
			this.elevationInTurns = 0;
		}
		else
		{
			this.elevationInTurns =
				Math.asin(coordsToConvert.z / this.radius)
				/ Polar.RadiansPerTurn;
		}

		return this;
	}

	public Polar overwriteWith(Polar other)
	{
		this.azimuthInTurns = other.azimuthInTurns;
		this.radius = other.radius;
		this.elevationInTurns = other.elevationInTurns;
		return this;
	}

	public Polar overwriteWithAzimuthRadiusElevation
	(
		double azimuthInTurns, double radius, double elevationInTurns
	)
	{
		this.azimuthInTurns = azimuthInTurns;
		this.radius = radius;
		this.elevationInTurns = elevationInTurns;
		return this;
	}

	/*
	public Polar random(Randomizer randomizer)
	{
		if (randomizer == null)
		{
			randomizer = new RandomizerSystem();
		}

		this.azimuthInTurns = randomizer.fraction();
		this.elevationInTurns = randomizer.fraction();
		return this;
	}
	*/

	public Coords toCoords(Coords coords)
	{
		var azimuthInRadians = this.azimuthInTurns * Polar.RadiansPerTurn;
		var elevationInRadians = this.elevationInTurns * Polar.RadiansPerTurn;

		var cosineOfElevation = Math.cos(elevationInRadians);

		coords.overwriteWithDimensions
		(
			Math.cos(azimuthInRadians) * cosineOfElevation,
			Math.sin(azimuthInRadians) * cosineOfElevation,
			Math.sin(elevationInRadians)
		).multiplyScalar(this.radius);

		return coords;
	}

	public Polar wrap()
	{
		while (this.azimuthInTurns < 0)
		{
			this.azimuthInTurns++;
		}
		while (this.azimuthInTurns >= 1)
		{
			this.azimuthInTurns--;
		}
		return this;
	}

	// Clonable.

	public Polar clone()
	{
		return new Polar(this.azimuthInTurns, this.radius, this.elevationInTurns);
	}
}
