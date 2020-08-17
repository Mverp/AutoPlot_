package ap.gallery;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.plugin.StackCombiner;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

public class CreateGalleryImageForWell {
	public static final int CENTROID_IMAGE_WIDTH = 70;
	public static final int CENTROID_IMAGE_HEIGHT = 50;
	public static final int CENTROID_IMAGE_PADDING = 5;
	public static final int CENTROID_ROWS_MAXIMUM = 10;
	public static final int TEXT_LABEL_WIDTH = 120;
	public static final int TEXT_POSITION_X = 5;
	public static final int TEXT_POSITION_Y = 33;
	public static final Color FONT_COLOR = Color.BLACK;
	public static final String GALLERY_FILENAME_INFIX = "Gallery";
	
	private int galleryImageNumber;
	private String wellName;
	private String galleryPath;
	private ArrayList<Point> centroids;
	private ArrayList<String> channelImages;
	private ImagePlus wellImage;

	public CreateGalleryImageForWell(String galleryPath, String wellName, ArrayList<Point> centroids, ArrayList<String> channelImages) {
		this.galleryImageNumber = 0;
		this.galleryPath = galleryPath;
		this.wellName = wellName;
		this.centroids = centroids;
		this.channelImages = channelImages;
	}
	
	public void create() {
		for (int galleryImageRowNumber = 0; galleryImageRowNumber < this.centroids.size(); galleryImageRowNumber++) {
			// SubImageGallery.createDirectories(getPaths()); doen op het moment dat je wilt opslaan
			
			Point centroid = this.centroids.get(galleryImageRowNumber);
			String rowLabel = getRowLabel(galleryImageRowNumber, centroid, this.wellName);
			
			CreateRowImageForCentroid createRowImageForCentroid = new CreateRowImageForCentroid(centroid, rowLabel, this.channelImages);
			createRowImageForCentroid.setCentroidImageDimensions(CENTROID_IMAGE_WIDTH, CENTROID_IMAGE_HEIGHT);
			createRowImageForCentroid.setCentroidImagePadding(CENTROID_IMAGE_PADDING);
			createRowImageForCentroid.setTextDimensions(TEXT_LABEL_WIDTH, TEXT_POSITION_X, TEXT_POSITION_Y);
			createRowImageForCentroid.setFontColor(FONT_COLOR);
			createRowImageForCentroid.create();
			ImagePlus rowImage = createRowImageForCentroid.get();
			
			updateWellImage(rowImage);
			
			finishGalleryImageOnRowLimit(galleryImageRowNumber);
			
			//GalleryImageCentroidRowProcessor centroidRowProcessor = new GalleryImageCentroidRowProcessor(galleryPath, wellPath, wellName);
			//centroidRowProcessor.process(centroid, row);
			
			//this.updateWellImageWithMostRecentRowImage(centroidRowProcessor.getCentroidRowImage());
			//centroidRowProcessor.saveAndResetImage();
		}
		finishGalleryImage();
	}
	
	private void finishGalleryImage() {
		if (this.wellImage != null) {
			addHeaderAndSaveAndResetWellImage();
		}
	}
	
	private void updateWellImage(ImagePlus rowImage) {
		if (this.wellImage != null) {
			combineVertically(rowImage.getStack());
		} else {
			this.wellImage = rowImage;
		}
	}
	
	private void combineVertically(ImageStack imageStack) {
		StackCombiner stackCombiner = new StackCombiner();
		ImageStack newImageStack = stackCombiner.combineVertically(this.wellImage.getStack(), imageStack);
		this.wellImage = new ImagePlus("", newImageStack);
	}

	private void finishGalleryImageOnRowLimit(int rowNumber) {
		if (rowNumber % CENTROID_ROWS_MAXIMUM == CENTROID_ROWS_MAXIMUM - 1) {
			addHeaderAndSaveAndResetWellImage();
			galleryImageNumber++;
		}		
	}
	
	private void addHeaderAndSaveAndResetWellImage() {
		addHeaderToWellImage();
		saveWellImage();
		this.wellImage = null;
	}

	private void addHeaderToWellImage() {
		AddHeaderToWellImage addHeaderToWellImage = new AddHeaderToWellImage(this.channelImages.size(), this.wellName, this.wellImage);
		addHeaderToWellImage.setDimensions(TEXT_LABEL_WIDTH, TEXT_POSITION_X, CENTROID_IMAGE_WIDTH);
		addHeaderToWellImage.setFontColor(FONT_COLOR);
		addHeaderToWellImage.addHeader();
		this.wellImage = addHeaderToWellImage.get();
	}

	private void saveWellImage() {
		String path = getGalleryFilePath();
		FileSaver fileSaver = new FileSaver(this.wellImage);
		fileSaver.saveAsTiff(path);
	}
	
	private String getGalleryFilePath() {
		String filename = this.wellName + "_" + GALLERY_FILENAME_INFIX + "_" + this.galleryImageNumber + ".tiff";
		return this.galleryPath + File.separator + this.wellName + File.separator + filename;
	}

	private String getRowLabel(int imageRowNumber, Point centroid, String well) {
		return 
				well + 
				imageRowNumber + 
				"X" + (new Double(centroid.getX())).intValue() + 
				"Y" + (new Double(centroid.getY()).intValue());
	}
}
