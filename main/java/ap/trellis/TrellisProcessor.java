package ap.trellis;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import ap.AutoPlot;
import ap.AutoPlotImageProcessor;
import ap.Convolver;
import ap.Files;
import ap.data.DataCell;
import ap.data.DataFile;
import ap.data.DataWell;
import ap.data.DataWellPlate;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;

public class TrellisProcessor {
	private AutoPlot plot;
	private ImageProcessor convolved_dx;
	private ImageProcessor convolved_dy;
	
	private boolean show = false;
	private boolean save = false;
	private int stages = 32;
	private int stage_nodes = 10;
	
	public TrellisProcessor(AutoPlot plot) {
		this.plot = plot;
	}
	
	public void process() {
		DataWellPlate wellPlateData = this.plot.getPlateData();
		DataWell currentPlotWellData = wellPlateData.get(this.plot.getCurrentPlot());
		String currentPlotWellDataName = currentPlotWellData.getName();
		double progress = 0.0;
		for(int i = 0; i<wellPlateData.size(); i++) {
			progress = ((new Double(i)) / (wellPlateData.size())) * 100;
			IJ.showStatus("Progress: " + new Integer((int)progress).toString() + "%");
			
			DataWell wellData = wellPlateData.get(i);
			String path = Files.getImageFilePathDyeThree(plot.getMainDirectory(), wellData.getName());
			ImagePlus imagePlus = IJ.openImage(path);
			AutoPlotImageProcessor imageProcessor = new AutoPlotImageProcessor(imagePlus.getProcessor());
			
			String name = wellData.getName();
			generateConvolvedImages(imageProcessor.getImageProcessor(), name);
			for (int j = 0; j<wellData.size(); j++) {
				DataCell cellData = wellData.get(j);
				Trellis trellis = createTrellis(imageProcessor, cellData.getCentroid(), "");
				cellData.setValueDyeThree(trellis.getMeanPixelValue());
			}
			
			// Save Trellis results to result file
			DataFile dataFile = new DataFile(wellData, this.plot.getMainDirectory());
			dataFile.create();
			
			if (isSave()) {
				String[] pathParts = path.split("\\.");
				String contourPath = pathParts[0] + Files.GENERIC_FILENAME_PART_CONTOUR + Files.EXT_TIF;
				save(contourPath, imagePlus);
				
				if (isShow() && currentPlotWellDataName.equals(name)) {
					IJ.open(contourPath);
				}
			}
		}
	}
	
	public void debugTrellis() {
		ImagePlus imagePlus = IJ.getImage();
		String name = imagePlus.getTitle().substring(0, 8);
		AutoPlotImageProcessor imageProcessor = new AutoPlotImageProcessor(imagePlus.getProcessor());
		
		DataWellPlate plateData = plot.getPlateData();
		DataWell wellData = plateData.get(0);
		for(int i=0; i < plateData.size(); i++) {
			if (name.equals(plateData.get(i).getName())) {
				wellData = plateData.get(i);				
			}
		}
		
		String wellName = wellData.getName();
		Rectangle rectangle = imageProcessor.getImageProcessor().getRoi();
		generateConvolvedImages(imageProcessor.getImageProcessor(), wellName);
		if (name.equals(wellData.getName())) {
			System.out.println("naam: " + wellName);
			ArrayList<Point> centroids = new ArrayList<Point>();
			for (int j=0; j < wellData.size(); j++) {
				DataCell cellData = wellData.get(j);
				Point point = cellData.getCentroid();
				if (rectangle.contains(point)) {
					centroids.add(point);
				}
			}
			
			if (centroids.size() > 0) {
				String debugCsvFilePath = plot.getMainDirectory() + File.separator + name + File.separator + name + Files.EXT_CSV;
				createTrellis(imageProcessor, centroids.get(0), debugCsvFilePath);
			} else {
				System.err.println("No centroids in ROI, or no ROI drawn");
			}
		} else {
			System.err.println("Wellnames don't match");
		}
	}
	
	public void setShow() {
		this.show = true;
	}
	
	public void unsetShow() {
		this.show = false;
	}
	
	public boolean isShow() {
		return this.show;
	}
	
	public void setSave() {
		this.save = true;
	}
	
	public void unsetSave() {
		this.save = false;
	}
	
	public boolean isSave() {
		return this.save;
	}
	
	private Trellis createTrellis(AutoPlotImageProcessor imageProcessor, Point point, String debugCsvFilePath) {
		Trellis trellis = new Trellis(this.stages, this.stage_nodes);
		trellis.setConvolvedImages(this.convolved_dx, this.convolved_dy);
		if (isShow() || isSave()) {
			trellis.setDraw();
		}
		trellis.create(point, imageProcessor);
		trellis.findContour(debugCsvFilePath);
		return trellis;
	}
	
	private void generateConvolvedImages(ImageProcessor imageProcessor, String name) {
		Convolver convolver = new Convolver(this.plot);
		convolver.generateConvolvedImages(imageProcessor, name);
		this.convolved_dx = convolver.getConvolveImage(name, "x");
		this.convolved_dy = convolver.getConvolveImage(name, "y");
	}
	
	private void save(String path, ImagePlus imagePlus) {
		FileSaver fs = new FileSaver(imagePlus);
		fs.saveAsTiff(path);
	}
}
