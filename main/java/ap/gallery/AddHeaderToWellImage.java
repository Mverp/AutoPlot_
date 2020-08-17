package ap.gallery;

import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class AddHeaderToWellImage {
	public static final int HEADER_HEIGHT = 30;
	public static final String GFP = "GFP";
	public static final String PI = "PI";
	public static final String[] LABELS = { GFP, PI, GFP + " + " + PI };
	
	private ImagePlus wellImage;
	private int numberOfChannelImages;
	private int centroidImageWidth;
	private int textLabelWidth;
	private String wellName;
	private int textLabelMarginLeft;
	private Color fontColor;

	public AddHeaderToWellImage(int numberOfChannelImages, String wellName, ImagePlus wellImage) {
		this.numberOfChannelImages = numberOfChannelImages;
		this.wellName = wellName;
		this.wellImage = wellImage;
	}

	public void addHeader() {
		int height = this.wellImage.getHeight() + HEADER_HEIGHT;
		int width = this.textLabelWidth + (this.numberOfChannelImages * this.centroidImageWidth);
		IJ.run("Colors...", "foreground=white background=white selection=red");
		IJ.run(this.wellImage, "Canvas Size...", "width=" + width + " height=" + height + " position=Bottom-Center");
		IJ.run("Colors...", "foreground=white background=black selection=red");
		
		writeFilterLabelsToHeader();
		addWellNameToHeader();
	}

	public ImagePlus get() {
		return this.wellImage;
	}

	public void setDimensions(int textLabelWidth, int textLabelMarginLeft, int centroidImageWidth) {
		this.textLabelWidth = textLabelWidth;
		this.textLabelMarginLeft = textLabelMarginLeft;
		this.centroidImageWidth = centroidImageWidth;
	}
	
	public void setFontColor(Color color) {
		this.fontColor = color;
	}
	
	private void writeFilterLabelsToHeader() {
		ImageProcessor imageProcessor = this.wellImage.getProcessor();
		imageProcessor.setColor(this.fontColor);
		for (int i=0; i<LABELS.length; i++) {
			int labelWidth = imageProcessor.getStringWidth(LABELS[i]);
			int xPos = getHorizontalPositionForChannelLabel(i, labelWidth);
			imageProcessor.drawString(LABELS[i], xPos, HEADER_HEIGHT);
		}
	}	
	
	private void addWellNameToHeader() {
		ImageProcessor imageProcessor = this.wellImage.getProcessor();
		imageProcessor.drawString(this.wellName, this.textLabelMarginLeft, HEADER_HEIGHT);
	}
	
	private int getHorizontalPositionForChannelLabel(int imgNr, int stringWidth) {
		int position = this.textLabelWidth + (imgNr * this.centroidImageWidth) + ((this.centroidImageWidth - stringWidth) / 2);
		return position;
	}
}
