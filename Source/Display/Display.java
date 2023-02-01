
package Display;

import javax.swing.*;

import Display.*;
import Geometry.*;
import Media.*;
import Model.*;
import Utility.*;

public interface Display extends Platformable
{
	void clear();

	/*
	Color colorBack();
	Color colorFore();
	Display displayToUse();
	void drawArc
	(
		Coords center, double radiusInner, double radiusOuter,
		double angleStartInTurns, double angleStopInTurns, Color colorFill,
		Color colorBorder
	);
	void drawBackground(Color colorBack, Color colorBorder);
	void drawCircle
	(
		Coords center, double radius, Color colorFill, Color colorBorder,
		double borderThickness
	);
	void drawCircleWithGradient
	(
		Coords center, double radius, ValueBreakGroup gradientFill,
		Color colorBorder
	);
	void drawCrosshairs
	(
		Coords center, double numberOfLines, double radiusOuter,
		double radiusInner, Color color, double lineThickness
	);
	void drawEllipse
	(
		Coords center, double semimajorAxis, double semiminorAxis,
		double rotationInTurns, Color colorFill, Color colorBorder
	);
	*/
	void drawImage(Image2 imageToDraw, Coords pos);
	/*
	void drawImagePartial
	(
		Image2 imageToDraw, Coords pos, Box regionToDrawAsBox
	);
	void drawImagePartialScaled
	(
		Image2 imageToDraw, Coords pos, Box regionToDrawAsBox, Coords sizeToDraw
	);
	*/
	void drawImageScaled(Image2 imageToDraw, Coords pos, Coords size);
	/*
	void drawLine(Coords fromPos, Coords toPos, Color color, double lineThickness);
	void drawMeshWithOrientation(MeshTextured mesh, Orientation meshOrientation);
	void drawPath
	(
		Coords vertices[], Color color, double lineThickness, boolean isClosed
	);
	void drawPixel(Coords pos, Color color);
	void drawPolygon(Coords vertices[], Color colorFill, Color colorBorder);
	*/
	void drawRectangle
	(
		Coords pos, Coords size, Color colorFill, Color colorBorder,
		boolean areColorsReversed
	);
	/*
	void drawRectangleCentered
	(
		Coords pos, Coords size, Color colorFill, Color colorBorder
	);
	void drawText
	(
		String text, double fontHeightInPixels, Coords pos,
		Color colorFill, Color colorOutline, boolean areColorsReversed,
		boolean isCentered, Double widthMaxInPixels
	);
	void drawWedge
	(
		Coords center, double radius, double angleStartInTurns,
		double angleStopInTurns, Color colorFill, Color colorBorder
	);
	*/
	void eraseModeSet(boolean value);
	double fontHeightInPixels();
	String fontName();
	void fontSet(String fontName, double fontHeightInPixels);
	void flush();
	void hide(Universe universe);
	Display initialize(Universe universe);
	void rotateTurnsAroundCenter
	(
		double turnsToRotate, Coords centerOfRotation
	);
	Coords sizeDefault();
	Coords sizeInPixels();
	void sizeInPixelsSet(Coords value);
	Coords sizeInPixelsHalf();
	Coords[] sizesAvailable();
	Coords scaleFactor();
	void stateRestore();
	void stateSave();
	double textWidthForFontHeight
	(
		String textToMeasure, double fontHeightInPixels
	);
	Image2 toImage();
	JComponent toJComponent();
	void updateForTimerTick(Universe universe);
}
