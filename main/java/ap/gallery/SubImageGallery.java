package ap.gallery;
import ij.IJ;
import ij.ImagePlus;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import ap.AutoPlot;
import ap.data.DataWell;
import ap.data.DataWellPlate;

public class SubImageGallery {

	public static final String GALLERY = "Gallery";
	
	private AutoPlot plotter;
	private DataWellPlate plate_data;
	private String gallery_parent_directory;

	private String gallery_name;
	
	public SubImageGallery(AutoPlot plotter) {
		this.plotter = plotter;
	}
	
	public void create() {
		fetchPlotterProperties();
		createGalleryForEachWell();
	}
	
	public static void createDirectories(String[] paths) {		
		for (int i=0; i<paths.length; i++) {
			createDirectory(paths[i]);
		}
	}
	
	private void fetchPlotterProperties() {
		setPlotterRoi();
		this.plate_data = this.plotter.getPlateData();
		this.gallery_parent_directory = this.plotter.getMainDirectory();
	}
	
	private void setPlotterRoi() {
		ImagePlus currentPlotImagePlus = plotter.getImagePlus();
		this.plotter.setRoi(currentPlotImagePlus);
	}
	
	private void createGalleryForEachWell() {
		for (int i=0; i<this.plate_data.size(); i++) {
			createWellGallery(i);
		}
		IJ.showMessage("Done!");
	}
	
	private void createWellGallery(int row) {
		DataWell wellData = this.plate_data.get(row);
		//ArrayList<Object> centroids = this.plotter.getRoiCentroids(wellData);
		//processWell(wellData, centroids);
		ArrayList<Point> centroidList = this.plotter.getRoiCentroidList(wellData);
		processWellCentroids(wellData, centroidList);
	}
	
	private void processWellCentroids(DataWell wellData, ArrayList<Point> centroidList) {
		String wellName = wellData.getName();
		String wellPath = this.gallery_parent_directory + wellName;
		fetchGalleryName(wellPath);
		ArrayList<String> channelImages = new ArrayList<String>();
		for (String filterFilenamePart : GalleryImageCentroidRowProcessor.FILTER_IMAGES) {
			channelImages.add(wellPath + File.separator + wellName + filterFilenamePart);
		}
		CreateGalleryImageForWell createGalleryImageForWell = new CreateGalleryImageForWell(this.gallery_parent_directory, wellName, centroidList, channelImages);
		createGalleryImageForWell.create();
	}
	
	/*private void processWell(DataWell wellData, ArrayList<Object> centroids) {
		String wellName = wellData.getName();
		String wellPath = this.gallery_parent_directory + wellName;
		fetchGalleryName(wellPath);
		GalleryImageWellProcessor wellProcessor = new GalleryImageWellProcessor(wellPath, wellName, gallery_name);
		wellProcessor.process(centroids);
	}*/
	
	private void fetchGalleryName(String wellPath) {
		if (this.gallery_name == null) {
			File directory = new File(wellPath + File.separator + GALLERY);
			
			int i = 1;
			String currentName = "";
			while(directory.exists()) {
				currentName = GALLERY + "_" + i;
				directory = new File(wellPath + File.separator + currentName);
				i++;
			}
			gallery_name = currentName;
		}
	}
	
	private static void createDirectory(String path) {
		try{
		 	boolean success = (new File(path)).mkdir();
		 	if (success) {
			 	System.out.println("Directory: " + path + " created");
		 	}
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
}