
package GameFramework.Display;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import GameFramework.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;

public interface Display extends Platformable
{
	void clear();
	void drawRectangle(Coords pos, Coords size);
	void initialize(Universe universe);
	Coords sizeDefault();
	Coords sizeInPixels();
	void updateForTimerTick(Universe universe);

	// Platformable.
	boolean isKeyListener();
	JComponent toJComponent();
}
