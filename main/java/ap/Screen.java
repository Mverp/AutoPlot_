package ap;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Screen {
	private static Dimension dimension = new Dimension(0, 0);
	
	public static Dimension getDimension() {
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		setDimension(graphicsDevices);
		return dimension;
	}
	
	private static void setDimension(GraphicsDevice[] graphicsDevices) {
		if (graphicsDevices.length > 0) {
			DisplayMode dm = graphicsDevices[0].getDisplayMode();
			dimension = new Dimension(dm.getWidth(), dm.getHeight());
		}		
	}
}