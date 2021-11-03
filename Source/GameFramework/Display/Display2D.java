
package GameFramework.Display;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import GameFramework.*;
import GameFramework.Geometry.*;
import GameFramework.Media.*;
import GameFramework.Model.*;
import GameFramework.Utility.*;

public class Display2D extends JPanel implements Display
{
	public Coords _sizeInPixels;
	public Coords[] _sizesAvailable;

	private BufferedImage bufferedImage;
	private double _fontHeightInPixels;
	private Graphics graphics;

	public Display2D
	(
		Coords[] sizesAvailable,
		String fontName,
		double fontHeightInPixels,
		String colorForeName,
		String colorBackName
	)
	{
		this._sizesAvailable = sizesAvailable;
		this._sizeInPixels = sizesAvailable[0];
		this._fontHeightInPixels = fontHeightInPixels;
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

	public static Display2D fromSizeAndIsInvisible
	(
		Coords sizeInPixels, boolean isInvisible
	)
	{
		return Display2D.fromSize(sizeInPixels);
		// todo - isInvisible.
	}

	public void drawRectangle(Coords pos, Coords size)
	{
		this.graphics.setColor(java.awt.Color.BLUE);

		this.graphics.drawRect
		(
			(int)pos.x, (int)pos.y, (int)size.x, (int)size.y
		);
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

	// JComponent.

	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		graphics.drawImage(this.bufferedImage, 0, 0, this);
	}

	// Display.

	public Color colorBack() { return null; }
	public Color colorFore() { return null; }
	public double fontHeightInPixels() { return this._fontHeightInPixels; }
	public String fontName() { return null; }
	public Coords sizeInPixels()
	{
		return this._sizeInPixels;
	}
	public void sizeInPixelsSet(Coords value)
	{
		this._sizeInPixels.overwriteWith(value);
	}
	public Coords sizeInPixelsHalf() { return null; }
	public Coords[] sizesAvailable() { return this._sizesAvailable; }

	public void clear()
	{
		this.graphics.setColor(java.awt.Color.BLACK);
		var sizeInPixels = this.sizeInPixels();
		this.graphics.fillRect
		(
			0, 0, (int)sizeInPixels.x, (int)sizeInPixels.y
		);
	}
	public Display displayToUse() { return this; }
	public void drawArc
	(
		Coords center, double radiusInner, double radiusOuter,
		double angleStartInTurns, double angleStopInTurns, Color colorFill,
		Color colorBorder
	) {}
	public void drawBackground(Color colorBack, Color colorBorder) {}
	public void drawCircle
	(
		Coords center, double radius, Color colorFill, Color colorBorder,
		double borderThickness
	){}
	public void drawCircleWithGradient
	(
		Coords center, double radius, ValueBreakGroup gradientFill,
		Color colorBorder
	){}
	public void drawCrosshairs
	(
		Coords center, double numberOfLines, double radiusOuter,
		double radiusInner, Color color, double lineThickness
	){}
	public void drawEllipse
	(
		Coords center, double semimajorAxis, double semiminorAxis,
		double rotationInTurns, Color colorFill, Color colorBorder
	){}
	public void drawImage(Image2 imageToDraw, Coords pos){}
	public void drawImagePartial
	(
		Image2 imageToDraw, Coords pos, Box regionToDrawAsBox
	){}
	public void drawImagePartialScaled
	(
		Image2 imageToDraw, Coords pos, Box regionToDrawAsBox, Coords sizeToDraw
	){}
	public void drawImageScaled(Image2 imageToDraw, Coords pos, Coords size){}
	public void drawLine(Coords fromPos, Coords toPos, Color color, double lineThickness){}
	public void drawMeshWithOrientation(MeshTextured mesh, Orientation meshOrientation){}
	public void drawPath
	(
		Coords[] vertices, Color color, double lineThickness, boolean isClosed
	){}
	public void drawPixel(Coords pos, Color color){}
	public void drawPolygon(Coords[] vertices, Color colorFill, Color colorBorder){}
	public void drawRectangle
	(
		Coords pos, Coords size, Color colorFill, Color colorBorder,
		boolean areColorsReversed
	)
	{
		this.drawRectangle(pos, size);
	}
	public void drawRectangleCentered
	(
		Coords pos, Coords size, Color colorFill, Color colorBorder
	){}
	public void drawText
	(
		String text, double fontHeightInPixels, Coords pos,
		Color colorFill, Color colorOutline, boolean areColorsReversed,
		boolean isCentered, double widthMaxInPixels
	){}
	public void drawWedge
	(
		Coords center, double radius, double angleStartInTurns,
		double angleStopInTurns, Color colorFill, Color colorBorder
	){}
	public void eraseModeSet(boolean value){}
	public void fontSet(String fontName, double fontHeightInPixels){}
	public void flush(){}
	public void hide(Universe universe){}

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
		var dimensionInPixels = new Dimension();
		dimensionInPixels.width = (int)sizeInPixels.x;
		dimensionInPixels.height = (int)sizeInPixels.y;
		this.setPreferredSize(dimensionInPixels);
		platformHelper.resizeForPlatformables();

		setBackground(java.awt.Color.BLACK);

		return this;
	}

	public void rotateTurnsAroundCenter
	(
		double turnsToRotate, Coords centerOfRotation
	){}
	public Coords sizeDefault()
	{
		return this._sizesAvailable[0];
	}
	public Coords scaleFactor(){ return null; }
	public void stateRestore(){}
	public void stateSave(){}
	public double textWidthForFontHeight
	(
		String textToMeasure, double fontHeightInPixels
	){ return Double.NaN; }
	public Image2 toImage(){ return null; }
}
