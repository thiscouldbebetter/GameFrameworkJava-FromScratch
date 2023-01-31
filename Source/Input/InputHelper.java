package Input;

import Main.*;
import Model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class InputHelper extends JComponent implements KeyListener, Platformable
{
	public java.util.List<Integer> keyCodesPressed;

	public void initialize(Universe universe)
	{
		var platformHelper = universe.platformHelper;
		platformHelper.platformableAdd(this);

		this.keyCodesPressed = new ArrayList<Integer>();
	}

	// KeyListener.

	public void keyPressed(KeyEvent e)
	{
		var keyCode = e.getKeyCode();
		if (this.keyCodesPressed.contains(keyCode) == false)
		{
			this.keyCodesPressed.add(keyCode);
		}
	}

	public void keyReleased(KeyEvent e)
	{
		var keyCode = e.getKeyCode();
		if (this.keyCodesPressed.contains(keyCode))
		{
			var indexToRemoveAt = this.keyCodesPressed.indexOf(keyCode);
			this.keyCodesPressed.remove(indexToRemoveAt);
		}
	}

	public void keyTyped(KeyEvent e)
	{
		// Do nothing.
	}

	// Platformable.

	public boolean isKeyListener()
	{
		return true;
	}

	public JComponent toJComponent()
	{
		return this;
	}

}
