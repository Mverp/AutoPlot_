package ap.data;
import ij.IJ;

import java.io.File;

import ap.AutoPlotHelper;
import ap.Files;


public class DataWellPlateProcessor {
	private DataWellPlate well_plate_data;
	private boolean debug = false;
	private File wells_directory;
	private boolean is_third_dye_image_present;
	private boolean is_third_dye_processed;

	public DataWellPlateProcessor(String directory) {
		this.wells_directory = new File(directory);
		this.well_plate_data = new DataWellPlate(this.wells_directory.getName());
	}
	
	public void process() {
		findAndProcessWellPaths();
	}
	
	public DataWellPlate getDataWellPlate() {
		return this.well_plate_data;
	}
	
	private void findAndProcessWellPaths() { 
		if (this.wells_directory.isDirectory()) {
			processWells();
		} else {
			IJ.log("Path is not a directory");
		}
	}
	
	private void processWells() {
		if (debug) {
			IJ.log("Wells dir: " + this.wells_directory.getName());
		}		
		File[] wellPaths = this.wells_directory.listFiles(Files.getDirectoryFilter());
		if (AutoPlotHelper.isThirdDyeImagePresent(wellPaths[0])) {
			setThirdDyeImagePresent();
		}
		if (AutoPlotHelper.isThirdDyeDataPresent(wellPaths[0])) {
			setThirdDyeProcessed();
		}
		if (wellPaths.length > 0) {
			for(int i=0; i<wellPaths.length; i++) {
				File wellDirectory = wellPaths[i];
				DataFileProcessor dataFileProcessor = new DataFileProcessor(wellDirectory);
				DataWell wellData = dataFileProcessor.getWellData();
				this.well_plate_data.add(wellData);
			}
		} else {
			IJ.log("No data found");
		}
	}

	public boolean isThirdDyeImagePresent() {
		return this.is_third_dye_image_present;
	}
	
	private void setThirdDyeImagePresent() {
		this.is_third_dye_image_present = true;
	}
	
	public boolean isThirdDyeProcessed() {
		return this.is_third_dye_processed;
	}
	
	private void setThirdDyeProcessed() {
		this.is_third_dye_processed = true;
	}
}
