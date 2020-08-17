package ap.gallery;
import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

public class GalleryImageWell extends GalleryImage {
	private static final int HEADER_HEIGHT = 30;
	private static final String GFP = "GFP";
	private static final String PI = "PI";
	
	public static final int AREA_LEFT = 120;
	public static final int MARGIN_LEFT = 15;
	
	public GalleryImageWell(String name, ImageStack imageStack) {
		super(name, imageStack);
	}
	
	public GalleryImageWell(ImagePlus imagePlus) {
		super(imagePlus);
	}

	public void finalize(String name) {
		createHeaderArea();
		getHeaderLabelsAndWriteLabelsToHeader(name);
	}
	
	public static int getWidth() {
		int width = GalleryImageWell.AREA_LEFT + (GalleryImageCentroidRowProcessor.FILTER_IMAGES.length * GalleryImageCentroid.WIDTH);
		return width;
	}
	
	private void createHeaderArea() {
		int height = image_plus.getHeight() + HEADER_HEIGHT;
		IJ.run("Colors...", "foreground=white background=white selection=red");
		IJ.run(image_plus, "Canvas Size...", "width=" + getWidth() + " height=" + height + " position=Bottom-Center");
		IJ.run("Colors...", "foreground=white background=black selection=red");
	}
	
	private void getHeaderLabelsAndWriteLabelsToHeader(String name) {
		String[] labels = {
				GFP, 
				PI, 
				GFP + " + " + PI
			};
		writeFilterLabelsToHeader(labels);
		writeWellLabelToHeader(name);
	}
	
	private void writeFilterLabelsToHeader(String[] labels) {
		ImageProcessor imageProcessor = this.image_plus.getProcessor();
		imageProcessor.setColor(Color.BLACK);
		for (int i=0; i<labels.length; i++) {
			int labelWidth = imageProcessor.getStringWidth(labels[i]);
			int xPos = getFilterImageLabelHorizontalPosition(i, labelWidth);
			imageProcessor.drawString(labels[i], xPos, HEADER_HEIGHT);
		}
	}
	
	private void writeWellLabelToHeader(String name) {
		ImageProcessor imageProcessor = this.image_plus.getProcessor();
		imageProcessor.drawString(name, MARGIN_LEFT, HEADER_HEIGHT);
	}
	
	private int getFilterImageLabelHorizontalPosition(int imgNr, int stringWidth) {
		int position = AREA_LEFT + (imgNr * GalleryImageCentroid.WIDTH) + ((GalleryImageCentroid.WIDTH - stringWidth) / 2);
		return position;
	}
}
