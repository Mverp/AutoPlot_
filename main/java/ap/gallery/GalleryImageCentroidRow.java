package ap.gallery;
import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.StackCombiner;
import ij.process.ImageProcessor;

public class GalleryImageCentroidRow extends GalleryImage {
	
	public GalleryImageCentroidRow(String name, ImageStack imageStack) {
		super(name, imageStack);
	}
	
	public GalleryImageCentroidRow(ImagePlus imagePlus) {
		super(imagePlus);
	}

	public void combineHorizontally(ImageStack imageStack) {
		StackCombiner stackCombiner = new StackCombiner();
		ImageStack newImageStack = stackCombiner.combineHorizontally(this.image_plus.getStack(), imageStack);
		this.image_plus = new ImagePlus("", newImageStack);
	}

	public void finalize(String name) {
		IJ.run("Colors...", "foreground=white background=white selection=red");
		IJ.run(this.image_plus, "Canvas Size...", "width=" + GalleryImageWell.getWidth() + " height=" + GalleryImageCentroid.HEIGHT + " position=Center-Right");
		IJ.run("Colors...", "foreground=white background=black selection=red");
		ImageProcessor imageProcessor = this.image_plus.getProcessor();
		imageProcessor.setColor(Color.BLACK);
		imageProcessor.drawString(name, GalleryImageWell.MARGIN_LEFT, 33);
	}
}
