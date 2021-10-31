
package GameFramework.Model;

import GameFramework.Display.*;
import GameFramework.Display.Visuals.*;
import GameFramework.Geometry.*;
import GameFramework.Model.Mortality.*;
import GameFramework.Model.Physics.*;

public class EntityBuilder
{
	public Entity messageFloater(String text, Coords pos, Color color)
	{
		var ticksToLive = 32;
		var riseSpeed = -1;
		var visual = VisualText.fromTextAndColor(text, color);
		pos = pos.clone();
		pos.z--;

		var messageEntity = new Entity
		(
			"Message" + text, // name
			new EntityProperty[]
			{
				Drawable.fromVisual(visual),
				new Ephemeral(ticksToLive, null),
				new Locatable
				(
					Disposition.fromPos(pos).velSet
					(
						new Coords(0, riseSpeed, 0)
					)
				),
			}
		);

		return messageEntity;
	}
}
