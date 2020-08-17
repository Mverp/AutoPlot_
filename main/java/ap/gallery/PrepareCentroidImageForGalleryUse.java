package ap.gallery;

import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageProcessor;

public class PrepareCentroidImageForGalleryUse {
	private ImagePlus centroidImagePlus;
	private int width;
	private int height;
	private int padding;

	public PrepareCentroidImageForGalleryUse(ImagePlus rawCentroidImagePlus) {
		this.centroidImagePlus = rawCentroidImagePlus;
	}
	
	public void setDimensions(int width, int height, int padding) {
		this.width = width;
		this.height = height;
		this.padding = padding;
	}
	
	public void prepare() {
		resizeImagePlus();
		drawImageBorder();
		convertImageToRgb();
	}
	
	public ImagePlus get() {
		return this.centroidImagePlus;
	}
	
	private void resizeImagePlus() {
		if (this.centroidImagePlus.getHeight() < this.height || this.centroidImagePlus.getWidth() < this.width) {
		//if (this.centroidImagePlus.getHeight() < this.width || this.centroidImagePlus.getWidth() < this.height) {
			IJ.run(this.centroidImagePlus, "Canvas Size...", "width=" + this.width + " height=" + this.height + " position=Center");
		}
	}
	
	private void drawImageBorder() {
		ImageProcessor imageProcessor = this.centroidImagePlus.getProcessor();
		imageProcessor.setColor(Color.WHITE);
		imageProcessor.fillOutside(new Roi(
				this.padding,
				this.padding,
				(this.width - (2 * this.padding)),
				(this.height - (2 * this.padding)))
		);
	}
	
	private void convertImageToRgb() {
		if (this.centroidImagePlus.getType() != ImagePlus.COLOR_RGB) {
			ImageProcessor imageProcessor = this.centroidImagePlus.getProcessor().convertToRGB();
			this.centroidImagePlus = new ImagePlus("", imageProcessor);
		}
	}	
}
