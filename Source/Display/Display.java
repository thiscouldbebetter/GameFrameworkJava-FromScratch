package Display;

import Geometry.*;
import Main.*;
import Media.*;
import Model.*;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Display extends JPanel implements Platformable
{
	public Coords sizeInPixels;

	private BufferedImage bufferedImage;
	private Graphics graphics;

	public Display(Coords sizeInPixels)
	{
		this.sizeInPixels = sizeInPixels;
	}

	public void clear()
	{
		this.graphics.setColor(java.awt.Color.BLACK);
		this.graphics.fillRect
		(
			0, 0, (int)this.sizeInPixels.x, (int)this.sizeInPixels.y
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

	// Draw methods.

	public void drawImage(Image2 image, Coords pos)
	{
		this.graphics.drawImage(image.systemImage, (int)pos.x, (int)pos.y, this);
	}

	public void drawImageScaled(Image2 image, Coords pos, Coords size)
	{
		this.drawImage(image, pos); // todo - Scaling.
	}

	public void drawRectangle(Coords pos, Coords size, Color colorFill, Color colorBorder)
	{
		if (colorFill != null)
		{
			this.graphics.setColor(colorFill.systemColor);

			this.graphics.fillRect
			(
				(int)pos.x, (int)pos.y, (int)size.x, (int)size.y
			);
		}

		if (colorBorder != null)
		{
			this.graphics.setColor(colorBorder.systemColor);

			this.graphics.drawRect
			(
				(int)pos.x, (int)pos.y, (int)size.x, (int)size.y
			);
		}
	}

}
