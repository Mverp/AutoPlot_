package ap.gallery;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import ij.ImageStack;
import ij.plugin.StackCombiner;

public class GalleryImageWellProcessor {
	public static final String GALLERY = "Gallery";
	private static final int MAX_CENTROID_ROWS = 10;
	
	private GalleryImageWell galleryWellImage;
	
	private String wellPath;
	private String wellName;
	private String galleryPath;
	
	private int galleryImageNumber = 0;
	
	public GalleryImageWellProcessor(String wellPath, String wellName, String galleryName) {
		this.wellPath = wellPath;
		this.wellName = wellName;
		this.galleryPath = this.wellPath + File.separator + galleryName;
	}
	
	public void process(ArrayList<Object> centroids) {
		for(int i=0; i<centroids.size(); i++) {
			if (centroids.get(i) instanceof Point) {
				processCentroid((Point)centroids.get(i), i);
			}
			saveGalleryImageOnMaxRowLimit(i);
		}
		saveGalleryImage();
	}
	
	public String getWellName() {
		return this.wellName;
	}
	
	private void processCentroid(Point centroid, int row) {
		SubImageGallery.createDirectories(getPaths());
		GalleryImageCentroidRowProcessor centroidRowProcessor = new GalleryImageCentroidRowProcessor(galleryPath, wellPath, wellName);
		centroidRowProcessor.process(centroid, row);
		this.updateWellImageWithMostRecentRowImage(centroidRowProcessor.getCentroidRowImage());
		centroidRowProcessor.saveAndResetImage();
	}
	
	private void saveGalleryImageOnMaxRowLimit(int row) {
		if (row % MAX_CENTROID_ROWS == MAX_CENTROID_ROWS - 1) {
			finalizeAndSaveAndResetWellImage();
			galleryImageNumber++;
		}
	}
	
	private void saveGalleryImage() {
		if (galleryWellImage != null) {
			finalizeAndSaveAndResetWellImage();
		}
	}
	
	private void updateWellImageWithMostRecentRowImage(GalleryImageCentroidRow centroidRowImage) {
		if (galleryWellImage != null) {
			this.combineVertically(centroidRowImage.getStack());
		} else {
			galleryWellImage = new GalleryImageWell(centroidRowImage.getImagePlus());
		}
	}
	
	private void combineVertically(ImageStack imageStack) {
		StackCombiner stackCombiner = new StackCombiner();
		ImageStack newImageStack = stackCombiner.combineVertically(galleryWellImage.getStack(), imageStack);
		galleryWellImage = new GalleryImageWell("", newImageStack);
	}
	
	private String[] getPaths() {
		String[] paths = {galleryPath};
		return paths;
	}

	private void finalizeAndSaveAndResetWellImage() {
		galleryWellImage.finalize(wellName);
		galleryWellImage.save(getGalleryFilePath());
		galleryWellImage = null;
	}
	
	private String getGalleryFilePath() {
		String filename = wellName + "_" + GALLERY + "_" + galleryImageNumber + ".tiff";
		return this.galleryPath + File.separator + filename;
	}
}
