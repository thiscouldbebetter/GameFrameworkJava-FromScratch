package Display;

import Geometry.*;
import Main.*;
import Media.*;
import Model.*;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Display2D extends JPanel implements Display
{
	private Coords _sizeInPixels;
	private Coords _sizeInPixelsHalf;

	private BufferedImage bufferedImage;
	private Graphics graphics;

	public Display2D(Coords sizeInPixels)
	{
		this._sizeInPixels = sizeInPixels;
		this._sizeInPixelsHalf = this._sizeInPixels.clone().half();
	}

	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		graphics.drawImage(this.bufferedImage, 0, 0, this);
	}

	public Coords sizeDefault()
	{
		return this._sizeInPixels;
	}

	public double textWidthForFontHeight(String text, double fontHeight)
	{
		return -1; // todo
	}

	public Image2 toImage()
	{
		return null; // todo
	}

	// Initialize and update.

	public Display initialize(Universe universe)
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
		this.setPreferredSize(sizeInPixels.toDimension());
		platformHelper.resizeForPlatformables();

		setBackground(java.awt.Color.BLACK);

		return this;
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

	public void clear()
	{
		this.graphics.setColor(java.awt.Color.BLACK);
		var sizeInPixels = this.sizeInPixels();
		this.graphics.fillRect
		(
			0, 0, (int)sizeInPixels.x, (int)sizeInPixels.y
		);
	}

	public void drawImage(Image2 image, Coords pos)
	{
		this.graphics.drawImage(image.systemImage, (int)pos.x, (int)pos.y, this);
	}

	public void drawImageScaled(Image2 image, Coords pos, Coords size)
	{
		this.drawImage(image, pos); // todo - Scaling.
	}

	public void drawRectangle
	(
		Coords pos, Coords size, Color colorFill, Color colorBorder,
		boolean isCentered // todo
	)
	{
		if (colorFill != null)
		{
			this.graphics.setColor(colorFill.systemColor());

			this.graphics.fillRect
			(
				(int)pos.x, (int)pos.y, (int)size.x, (int)size.y
			);
		}

		if (colorBorder != null)
		{
			this.graphics.setColor(colorBorder.systemColor());

			this.graphics.drawRect
			(
				(int)pos.x, (int)pos.y, (int)size.x, (int)size.y
			);
		}
	}

	void drawText
	(
		String text, double fontHeightInPixels, Coords pos,
		Color colorFill, Color colorOutline, boolean areColorsReversed,
		boolean isCentered, Double widthMaxInPixels
	)
	{
		// todo
	}

	// todo - Display methods.

	public void eraseModeSet(boolean value) {}
	public double fontHeightInPixels() {return -1; }
	public String fontName() { return null; }
	public void fontSet(String fontName, double fontHeightInPixels) {}
	public void flush() {}
	public void hide(Universe universe) {}
	public void rotateTurnsAroundCenter
	(
		double turnsToRotate, Coords centerOfRotation
	) {}
	public Coords sizeInPixels() { return this._sizeInPixels; }
	public void sizeInPixelsSet(Coords value) { /* todo */ }
	public Coords sizeInPixelsHalf() { return this._sizeInPixelsHalf; }
	public Coords[] sizesAvailable() { return null; }

	public Coords scaleFactor() { return null; } 

	public void stateRestore()
	{
		// todo
	}

	public void stateSave()
	{
		// todo
	}
}
