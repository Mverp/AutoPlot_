package ap.gallery;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ImageProcessor;

public class GalleryImage {
	protected ImagePlus image_plus;
	protected String name;
	
	public GalleryImage(String path) {
		this.image_plus = IJ.openImage(path);
	}
	
	public GalleryImage(String name, ImageStack imageStack) {
		this.image_plus = new ImagePlus(name, imageStack);
	}
	
	public GalleryImage(ImagePlus imagePlus) {
		this.image_plus = imagePlus;
	}
	
	public GalleryImage(String name, ImageProcessor imageProcessor) {
		this.name = name;
		this.image_plus = new ImagePlus(this.name, imageProcessor);
	}

	public ImageStack getStack() {
		return this.image_plus.getStack();
	}
	
	public void save(String path) {
		FileSaver fs = new FileSaver(this.image_plus);
		fs.saveAsTiff(path);
	}
	
	public ImagePlus getImagePlus() {
		return this.image_plus;
	}
}
