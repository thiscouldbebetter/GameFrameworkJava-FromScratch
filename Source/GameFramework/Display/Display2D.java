
package GameFramework.Display;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import GameFramework.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;

public class Display2D extends JPanel implements Platformable
{
	public Coords _sizeInPixels;

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
		this._sizeInPixels = sizesAvailable[0];
	}

	public static Display2D fromSize(Coords sizeInPixels)
	{
		return new Display2D
		(
			new Coords[] { sizeInPixels },
			null, // fontName
			10, // fontHeightInPixels
			"White", "Black" // colorNames
		);
	}

	public void clear()
	{
		this.graphics.setColor(java.awt.Color.BLACK);
		var sizeInPixels = this.sizeInPixels();
		this.graphics.fillRect
		(
			0, 0, (int)sizeInPixels.x, (int)sizeInPixels.y
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
		var sizeInPixels = this.sizeInPixels();
		this.bufferedImage = new BufferedImage
		(
			(int)sizeInPixels.x,
			(int)sizeInPixels.y,
			BufferedImage.TYPE_INT_ARGB
		);
		this.graphics = this.bufferedImage.getGraphics();
		this.graphics.setColor(java.awt.Color.BLUE);

		var platformHelper = universe.platformHelper;
		platformHelper.platformableAdd(this);
		// Size has to be set after being added to layout.
		var dimensionInPixels = new Dimension();
		dimensionInPixels.width = (int)sizeInPixels.x;
		dimensionInPixels.height = (int)sizeInPixels.y;
		this.setPreferredSize(dimensionInPixels);
		platformHelper.resizeForPlatformables();

		setBackground(java.awt.Color.BLACK);
	}

	public Coords sizeInPixels()
	{
		return this._sizeInPixels;
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
