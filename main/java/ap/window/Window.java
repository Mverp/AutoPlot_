package ap.window;

import java.awt.Dimension;
import java.awt.Frame;

import ap.Screen;
import ij.plugin.frame.PlugInFrame;

public class Window extends PlugInFrame
{

	private static final long serialVersionUID = 1L;


	public Window(String title)
	{
		super(title);
		closeOpenWindows();
	}


	private void closeOpenWindows()
	{
		Frame[] frames = PlugInFrame.getFrames();
		for (int i = 0; i < frames.length; i++)
		{
			String title = super.getTitle();
			if (title.equals(frames[i].getTitle()) && !frames[i].equals(this))
			{
				frames[i].dispose();
			}
		}
	}


	public void setup()
	{
		pack();
		setWindowLocation();
	}


	private void setWindowLocation()
	{
		Dimension dimension = Screen.getDimension();
		int x = dimension.width / 2 - this.getWidth() / 2;
		int y = dimension.height / 2 - this.getHeight();
		this.setLocation(x, y);
	}

}
