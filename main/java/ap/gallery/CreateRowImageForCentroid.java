package ap.gallery;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.plugin.StackCombiner;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class CreateRowImageForCentroid {
	private Point centroid;
	private String rowLabel;
	private ArrayList<String> channelImages;
	private ImagePlus rowImage;
	private Color fontColor;
	private int textPositionY;
	private int textPositionX;
	private int textLabelWidth;
	private int centroidImagePadding;
	private int centroidImageHeight;
	private int centroidImageWidth;

	public CreateRowImageForCentroid(Point centroid, String rowLabel, ArrayList<String> channelImages) {
		this.centroid = centroid;
		this.rowLabel = rowLabel;
		this.channelImages = channelImages;
	}
	
	public void create() {
		for (String channelImage : this.channelImages) {
			ImagePlus imagePlus = IJ.openImage(channelImage);
			imagePlus = imagePlus.flatten();
			
			ImagePlus centroidImage = getCentroidImage(imagePlus);
			updateRowImage(centroidImage);
		}
		setLabel(this.rowLabel);
	}
	
	public ImagePlus get() {
		return this.rowImage;
	}

	public void setLabel(String name) {
		IJ.run("Colors...", "foreground=white background=white selection=red");
		IJ.run(this.rowImage, "Canvas Size...", "width=" + getRowImageWidth() + " height=" + this.centroidImageHeight + " position=Center-Right");
		IJ.run("Colors...", "foreground=white background=black selection=red");
		ImageProcessor imageProcessor = this.rowImage.getProcessor();
		imageProcessor.setColor(this.fontColor);
		imageProcessor.drawString(name, this.textPositionX, this.textPositionY);
	}
	
	private int getRowImageWidth() {
		return this.textLabelWidth + (this.channelImages.size() * this.centroidImageWidth);
	}

	private void updateRowImage(ImagePlus centroidImage) {
		if (this.rowImage != null) {
			combineHorizontally(centroidImage.getStack());
		} else {
			this.rowImage = centroidImage;
		}
	}
	
	public void combineHorizontally(ImageStack imageStack) {
		StackCombiner stackCombiner = new StackCombiner();
		ImageStack newImageStack = stackCombiner.combineHorizontally(this.rowImage.getStack(), imageStack);
		this.rowImage = new ImagePlus("", newImageStack);
	}

	private ImagePlus getCentroidImage(ImagePlus imagePlus) {
		ImagePlus croppedCentroidImagePlus = getCroppedCentroidImagePlus(imagePlus);
		PrepareCentroidImageForGalleryUse prepareCentroidImageForGalleryUse = new PrepareCentroidImageForGalleryUse(croppedCentroidImagePlus);
		prepareCentroidImageForGalleryUse.setDimensions(this.centroidImageWidth, this.centroidImageHeight, this.centroidImagePadding);
		prepareCentroidImageForGalleryUse.prepare();
		return prepareCentroidImageForGalleryUse.get();
	}

	// Crop
	private ImagePlus getCroppedCentroidImagePlus(ImagePlus imagePlus) {
		setRoiForCropping(imagePlus);
		ImageProcessor imageProcessor = imagePlus.getProcessor().crop();
		return new ImagePlus("", imageProcessor);
	}

	private void setRoiForCropping(ImagePlus imagePlus) {
		Roi roi = getRoi();
		imagePlus.setRoi(roi);
	}

	private Roi getRoi() {
		return new Roi(
			new Double(this.centroid.x).intValue() - ( this.centroidImageWidth / 2 ), 
			new Double(this.centroid.y).intValue() - ( this.centroidImageHeight / 2 ), 
			this.centroidImageHeight, 
			this.centroidImageWidth
		);
	}
	// end crop

	public void setCentroidImageDimensions(int centroidImageWidth, int centroidImageHeight) {
		this.centroidImageWidth = centroidImageWidth;
		this.centroidImageHeight = centroidImageHeight;
	}

	public void setCentroidImagePadding(int centroidImagePadding) {
		this.centroidImagePadding = centroidImagePadding;
	}

	public void setTextDimensions(int textLabelWidth, int textPositionX, int textPositionY) {
		this.textLabelWidth = textLabelWidth;
		this.textPositionX = textPositionX;
		this.textPositionY = textPositionY;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
}
