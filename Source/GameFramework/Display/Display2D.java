
package GameFramework.Display;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import GameFramework.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;

public class Display2D extends JPanel implements Platformable
{
	public Coords sizeInPixels;

	private BufferedImage bufferedImage;
	private Graphics graphics;

	public Display2D
	(
		Coords[] sizesAvailable,
		String fontName,
		int fontHeightInPixels,
		String colorForeName,
		String colorBackName
	)
	{
		this.sizeInPixels = sizesAvailable[0];
	}

	public void clear()
	{
		this.graphics.setColor(java.awt.Color.BLACK);
		this.graphics.fillRect
		(
			0, 0, (int)this.sizeInPixels.x, (int)this.sizeInPixels.y
		);
	}

	public void drawRectangle(Coords pos, Coords size)
	{
		this.graphics.setColor(java.awt.Color.BLUE);

		this.graphics.drawRect
		(
			(int)pos.x, (int)pos.y, (int)size.x, (int)size.y
		);
	}

	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		graphics.drawImage(this.bufferedImage, 0, 0, this);
	}

	public void initialize(Universe universe)
	{
		this.bufferedImage = new BufferedImage
		(
			(int)this.sizeInPixels.x,
			(int)this.sizeInPixels.y,
			BufferedImage.TYPE_INT_ARGB
		);
		this.graphics = this.bufferedImage.getGraphics();
		this.graphics.setColor(java.awt.Color.BLUE);

		var platformHelper = universe.platformHelper;
		platformHelper.platformableAdd(this);
		// Size has to be set after being added to layout.
		this.setPreferredSize(this.sizeInPixels.toDimension());
		platformHelper.resizeForPlatformables();

		setBackground(java.awt.Color.BLACK);
	}

	public void updateForTimerTick(Universe universe)
	{
		this.repaint();
	}

	// Platformable.

	public boolean isKeyListener()
	{
		return false;
	}

	public JComponent toJComponent()
	{
		return this;
	}
}
