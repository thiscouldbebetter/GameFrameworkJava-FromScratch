package Utility;

import Geometry.*;
import Main.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlatformHelper
{
	private JFrame frame;

	public Coords imageSizeGet(Image image)
	{
		return Coords.fromXY
		(
			image.getWidth(this.frame), image.getHeight(this.frame)
		);
	}

	public void initialize()
	{
		var frame = new JFrame("GameFramework");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);

		this.frame = frame;
	}

	public void platformableAdd(Platformable platformableToAdd)
	{
		var contentPane = this.frame.getContentPane();
		contentPane.add
		(
			platformableToAdd.toJComponent(), BorderLayout.CENTER
		);

		if (platformableToAdd.isKeyListener())
		{
			var platformableToAddAsKeyListener =
				(KeyListener)platformableToAdd;
			this.frame.addKeyListener(platformableToAddAsKeyListener);
		}

		if (platformableToAdd.isMouseListener() )
		{
			var platformableToAddAsMouseListener =
				(MouseListener)platformableToAdd;
			this.frame.addMouseListener(platformableToAddAsMouseListener);

			var platformableToAddAsMouseMotionListener =
				(MouseMotionListener)platformableToAdd;
			this.frame.addMouseMotionListener(platformableToAddAsMouseMotionListener);

		}
	}

	public void resizeForPlatformables()
	{
		this.frame.pack();
	}
}
