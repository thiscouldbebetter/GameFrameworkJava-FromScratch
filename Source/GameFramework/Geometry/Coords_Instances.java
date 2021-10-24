
package GameFramework.Geometry;

import GameFramework.Helpers.*;
import GameFramework.Utility.*;

public class Coords_Instances
{
	public Coords HalfHalfZero;
	public Coords Halves;
	public Coords MinusOneZeroZero;
	public Coords Ones;
	public Coords OneOneZero;
	public Coords OneZeroZero;
	public Coords TwoTwoZero;
	public Coords ZeroZeroOne;
	public Coords ZeroMinusOneZero;
	public Coords ZeroOneZero;
	public Coords Zeroes;

	public Coords_Instances()
	{
		this.HalfHalfZero = new Coords(.5, .5, 0);
		this.Halves = new Coords(.5, .5, .5);
		this.MinusOneZeroZero = new Coords(-1, 0, 0);
		this.Ones = new Coords(1, 1, 1);
		this.OneOneZero = new Coords(1, 1, 0);
		this.OneZeroZero = new Coords(1, 0, 0);
		this.TwoTwoZero = new Coords(2, 2, 0);
		this.ZeroZeroOne = new Coords(0, 0, 1);
		this.ZeroMinusOneZero = new Coords(0, -1, 0);
		this.ZeroOneZero = new Coords(0, 1, 0);
		this.Zeroes = Coords.create();
	}
}
