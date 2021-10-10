package GameFramework;

import java.awt.event.*;

public class ActivityUserInputAccept implements Activity
{
	public void perform
	(
		Universe universe, World world, Place place, Entity entity
	)
	{
		var inputHelper = universe.inputHelper;
		var keyCodesPressed = inputHelper.keyCodesPressed;
		for (var i = 0; i < keyCodesPressed.size(); i++)
		{
			var keyCode = keyCodesPressed.get(i);

			Coords directionToAccelerate = null;

			if (keyCode == KeyEvent.VK_A)
			{
				directionToAccelerate = new Coords(-1, 0);
			}
			else if (keyCode == KeyEvent.VK_D)
			{
				directionToAccelerate = new Coords(1, 0);
			}
			else if (keyCode == KeyEvent.VK_S)
			{
				directionToAccelerate = new Coords(0, 1);
			}
			else if (keyCode == KeyEvent.VK_W)
			{
				directionToAccelerate = new Coords(0, -1);
			}

			if (directionToAccelerate != null)
			{
				var entityVel = entity.locatable().loc.vel;
				var amountToAccelerate = 0.1;
				var acceleration =
					directionToAccelerate.multiplyScalar(amountToAccelerate);
				entityVel.add(acceleration);
			}
		}
	}
}
