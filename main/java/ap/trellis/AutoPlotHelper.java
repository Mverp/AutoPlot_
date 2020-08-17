package ap.trellis;
import ij.ImagePlus;
import ij.gui.Roi;

import java.awt.Color;
import java.io.File;

import ap.Files;


public class AutoPlotHelper {
	public static int doubleToInt(double value) {
		return (new Double(value)).intValue();
	}
	
	public static double getLn(double value) {
		return Math.log(value);
	}
	
	public static void setRoiColor(ImagePlus imagePlus, Roi roi, Color color) {
		imagePlus.setOverlay(roi, color, 1, null);
		imagePlus.updateAndRepaintWindow();
	}
	
	public static boolean isThirdDyeImagePresent(File file) {
		File[] files = file.listFiles(Files.getFileNameFilterImageDyeThree());
		return files.length > 0;
	}
	
	public static boolean isThirdDyeDataPresent(File file) {
		File[] files = file.listFiles(Files.getFileNameFilterDataDyeThree());
		return files.length > 0;
	}
}
