import ij.*;
import ij.gui.*;
import ij.io.*;
import ij.plugin.*;

import java.awt.*;

import ap.AutoPlot;
import ap.data.DataWellPlate;
import ap.data.DataWellPlateProcessor;
import ap.window.WindowMainActions;
import ap.window.WindowTrellisOptions;

public class AutoPlot_ implements PlugIn {
	public static final String TOOL = "polygon";
	public static final Color COLOR = new Color(255,0,0);
	
	private boolean debug = false;

	private String main_directory = "";
	private DataWellPlate plate_data;
	
	public void run(String arg) {
		debug("+++++++++++++++++++++++");
		debug("Running...");
		
		DirectoryChooser dc = new DirectoryChooser("Please choose a directory with analyzed data");
		init(dc.getDirectory());
		
		debug("Finished."); 
	}
	
	private void init(String directory) {
		debug(directory);
		checkAndProcessDirectory(directory);
	}
	
	private void checkAndProcessDirectory(String directory) {
		if (null != directory) {
			this.main_directory = directory;
			processDirectoryAndCreatePlot();
		} else {
			IJ.log("No directory selected");
		}		
	}
	
	private void processDirectoryAndCreatePlot() {
		System.out.println("processDirectoryAndCreatePlot");
		DataWellPlateProcessor dataWellPlateProcessor = processDirectoryAndGetProcessor();
		AutoPlot plot = createAndGetPlot();
		createPlotAndShowTrellisOptionWindow(dataWellPlateProcessor, plot);
	}
	
	private void createPlotAndShowTrellisOptionWindow(DataWellPlateProcessor dataWellPlateProcessor, AutoPlot plot) {
		if (dataWellPlateProcessor.isThirdDyeImagePresent() && !dataWellPlateProcessor.isThirdDyeProcessed()) {
			WindowTrellisOptions trellisOptionsWindow = new WindowTrellisOptions(plot);
			trellisOptionsWindow.setVisible(true);
		}
		if (dataWellPlateProcessor.isThirdDyeProcessed()) {
			plot.setDyeThree();
		}
	}

	private DataWellPlateProcessor processDirectoryAndGetProcessor() {
		DataWellPlateProcessor dataWellPlateProcessor = new DataWellPlateProcessor(this.main_directory);
		dataWellPlateProcessor.process();
		this.plate_data = dataWellPlateProcessor.getDataWellPlate();
		return dataWellPlateProcessor;
	}
	
	private AutoPlot createAndGetPlot() {
		AutoPlot plot = new AutoPlot(this.plate_data, this.main_directory);
		WindowMainActions mainActionWindow = new WindowMainActions(plot);
		mainActionWindow.setVisible(true);
		setColorAndTool();
		return plot;
	}
	
	private void setColorAndTool() {
		Roi.setColor(COLOR);
		IJ.setTool(TOOL);
	}
	
	private void debug(String message) {
		if (debug) {
			IJ.log(message);
			System.err.println(message);
		}
	}
}