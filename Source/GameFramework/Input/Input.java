
package GameFramework.Input;

import java.util.*;

import GameFramework.Helpers.*;

public class Input
{
	public String name;

	public boolean isActive;
	public int ticksActive;

	public Input(String name)
	{
		this.name = name;

		this.isActive = true;
		this.ticksActive = 0;
	}

	public static Input_Names _names;
	public static Input_Names Names()
	{
		if (Input._names == null)
		{
			Input._names = new Input_Names();
		}

		return Input._names;
	}
}
