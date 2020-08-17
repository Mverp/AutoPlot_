package ap;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.gui.MessageDialog;

import java.awt.Frame;
import java.io.File;

import ap.window.WindowMainActions;


public class NavigatorImage {
	private String image_directory = "";
	
	public NavigatorImage(String imageDirectory) {
		this.image_directory = imageDirectory;
	}
	
	public void goTo(String direction) {
		ImagePlus activeImagePlus = WindowManager.getCurrentImage();
		if (activeImagePlus != null) {
			determineNextImageAndNavigate(activeImagePlus, direction);
		}
	}
	
	private void determineNextImageAndNavigate(ImagePlus activeImagePlus, String direction) {
		String imagePath = this.image_directory + activeImagePlus.getTitle();
		String[] pathParts = imagePath.split("\\\\");
		String wellDirectory = pathParts[pathParts.length - 2];
		File[] files = getActiveImageWellDirectoryFileList(pathParts);
		for (int i=0; i<files.length; i++) {
			String path = files[i].getPath() + File.separator; 
			if(path.equals(this.image_directory)) {
				File destinationDirectory = getDestinationDirectory(files, direction, i);
				if (destinationDirectory != null) {
					String filePath = imagePath.replaceAll(wellDirectory, destinationDirectory.getName());
					navigateTo(destinationDirectory, filePath);
					activeImagePlus.close();
				}
			}
		}
	}
	
	private File[] getActiveImageWellDirectoryFileList(String[] pathParts) {
		String path = "";
		for (int i=0; i < pathParts.length - 2; i++) {
			path += pathParts[i] + File.separator;
		}
		File file = new File(path);
		return file.listFiles(Files.getDirectoryFilter());
	}
	
	private File getDestinationDirectory(File[] files, String direction, int i) {
		if (WindowMainActions.PREVIOUS.equals(direction)) {
			return getDirectoryPrevious(files, i);
		}
		if (WindowMainActions.NEXT.equals(direction)) {
			return getDirectoryNext(files, i);
		}
		return null;
	}
	
	private File getDirectoryPrevious(File[] files, int currentIndex) {
		if (currentIndex - 1 >= 0) {
			return files[currentIndex - 1];
		} else {
			new MessageDialog(new Frame(), "First File", "This is the first file in this series.");
			System.err.println("At file list beginning");
			return null;
		}
	}
	
	private File getDirectoryNext(File[] files, int currentIndex) {
		if (currentIndex + 1 < files.length) {
			return files[currentIndex + 1];
		} else {
			new MessageDialog(new Frame(), "Last File", "This is the last file in this series.");
			System.err.println("At file list end");
			return null;
		}
	}
	
	private void navigateTo(File directory, String filePath) {
		if (directory.isDirectory()) {
			File file = new File(filePath);
			if (file.exists()) {
				ImageWindow.centerNextImage();
				IJ.open(filePath);
			}
		} else {
			new MessageDialog(new Frame(), "Not Found", "Directory was not found");
			System.err.println("Cannot navigate here: File is not a directory");
		}
	}
}
