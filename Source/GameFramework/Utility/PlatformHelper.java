package GameFramework.Utility;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlatformHelper
{
	private JFrame frame;

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
	}

	public void platformableRemove(Platformable platformableToRemove)
	{
		// Do nothing.
	}

	public void resizeForPlatformables()
	{
		this.frame.pack();
	}
}
