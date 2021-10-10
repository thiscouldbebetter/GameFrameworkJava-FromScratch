
package GameFramework.Display;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import GameFramework.*;
import GameFramework.Geometry.*;

public interface Display extends Platformable
{
	void clear();
	void drawRectangle(Coords pos, Coords size);
	void initialize(Universe universe);
	void updateForTimerTick(Universe universe);

	// Platformable.
	boolean isKeyListener();
	JComponent toJComponent();
}
