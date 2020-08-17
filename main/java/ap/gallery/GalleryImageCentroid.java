package ap.gallery;
import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageProcessor;

public class GalleryImageCentroid extends GalleryImage {
	public static final int WIDTH = 70;
	public static final int HEIGHT = 50;
	public static final int BORDER_PADDING = 5;
	
	public GalleryImageCentroid(String name, ImageProcessor imageProcessor) {
		super(name, imageProcessor);
		prepareForGalleryUse();
	}
	
	private void prepareForGalleryUse() {
		resizeImagePlus();
		drawBorder();
		convertToRgb();
	}
	
	private void resizeImagePlus() {
		if (this.image_plus.getHeight() < WIDTH || this.image_plus.getWidth() < HEIGHT) {
			IJ.run(this.image_plus, "Canvas Size...", "width=" + WIDTH + " height=" + HEIGHT + " position=Center");
		}
	}
	
	private void drawBorder() {
		ImageProcessor imageProcessor = this.image_plus.getProcessor();
		imageProcessor.setColor(Color.WHITE);
		imageProcessor.fillOutside(new Roi(
				BORDER_PADDING,
				BORDER_PADDING,
				(WIDTH - (2 * BORDER_PADDING)),
				(HEIGHT - (2 * BORDER_PADDING)))
		);
	}
	
	private void convertToRgb() {
		if (this.image_plus.getType() != ImagePlus.COLOR_RGB) {
			ImageProcessor imageProcessor = image_plus.getProcessor().convertToRGB();
			this.image_plus = new ImagePlus(this.name, imageProcessor);
		}
	}
}
