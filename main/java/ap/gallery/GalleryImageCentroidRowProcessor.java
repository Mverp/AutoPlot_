package ap.gallery;

import java.awt.Point;
import java.io.File;

public class GalleryImageCentroidRowProcessor {
	public static String[] FILTER_IMAGES = {
		" -- Background  and Flatfield  Corrected DYE 1.tif", 
		" -- Background  and Flatfield  Corrected DYE 2.tif", 
		" -- Merged Channels.tif"
	};
	
	private static final String CENTROID_DIRECTORY = "centroids";
	private static final String CENTROID_ROW_DIRECTORY = "rows";
	
	private GalleryImageCentroidRow centroidRowImage;
	
	private String galleryPath;
	private String wellPath;
	private String wellName;
	
	private String cellName;
	private String rowPath;
	private String centroidPath;
	
	public GalleryImageCentroidRowProcessor(String galleryPath, String wellPath, String wellName) {
		this.galleryPath = galleryPath;
		this.wellPath = wellPath;
		this.wellName = wellName;
		this.centroidPath = this.galleryPath + File.separator + CENTROID_DIRECTORY;
		this.rowPath = this.galleryPath + File.separator + CENTROID_ROW_DIRECTORY;
	}
	
	public void process(Point centroid, int row) {
		fetchCellName(row, centroid);
		SubImageGallery.createDirectories(getPaths());
		for (int i=0; i<FILTER_IMAGES.length; i++) {
			GalleryImageFilter filterImage = getFilterImage(FILTER_IMAGES[i]);
			processCentroidInFilterImage(filterImage, centroid);
		}
		finalizeImage(row);
	}
	
	public void saveAndResetImage() {
		saveAndResetCentroidRowImage(rowPath + File.separator + cellName + ".tif");
	}
	
	public GalleryImageCentroidRow getCentroidRowImage() {
		return this.centroidRowImage;
	}
	
	private void finalizeImage(int row) {
		centroidRowImage.finalize(cellName);
	}

	private void fetchCellName(int well, Point centroid) {
		cellName = well + getWellIdentifier() + "X" + (new Double(centroid.getX())).intValue() + "Y" + (new Double(centroid.getY()).intValue());
	}
	
	private String getWellIdentifier() {
		String[] identifier = wellName.split("\\s", 2);
		return identifier[1];
	}
	
	private String[] getPaths() {
		String paths[] = {centroidPath, rowPath};
		return paths;
	}
	
	private GalleryImageFilter getFilterImage(String filterFilenamePart) {
		GalleryImageFilter filterImage = new GalleryImageFilter(wellPath + File.separator + wellName + filterFilenamePart);
		filterImage.flatten();
		return filterImage;
	}
	
	private void processCentroidInFilterImage(GalleryImageFilter filterImage, Point centroid) {
		GalleryImageCentroid centroidImage = filterImage.getCentroidImage(cellName, centroid);
		centroidImage.save(centroidPath + File.separator + cellName + ".tif");
		updateImage(centroidImage);
	}
	
	private void updateImage(GalleryImageCentroid centroidImage) {
		if (centroidRowImage != null) {
			centroidRowImage.combineHorizontally(centroidImage.getStack());
		} else {
			centroidRowImage = new GalleryImageCentroidRow(centroidImage.getImagePlus());
		}
	}
	
	private void saveAndResetCentroidRowImage(String path) {
		centroidRowImage.save(path);
		centroidRowImage = null;
	}
}
