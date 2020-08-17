package ap;


import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import ap.data.DataCell;
import ap.data.DataWell;
import ap.data.DataWellPlate;
import ap.plot.PlotCreater;
import ap.plot.IPlot;
import ap.plot.PlotData;
import ap.plot.PlotImageJ;
import ap.plot.PlotJFreeChart;
import ap.plot.PlotLabel;

import ij.ImagePlus;
import ij.gui.MessageDialog;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.gui.Roi;

public class AutoPlot {
	public static final int IMAGE_WIDTH = 1024;
	public static final int IMAGE_HEIGHT = 768;
	public static final String SUB = "sub";
	
	private boolean dye_three = false;
	private int max_x = 0;
	private int max_y = 0;
	private String main_directory = "";
	private String axis_x = "";
	private String axis_y = "";
	private ArrayList<AutoPlot> sub_plots = new ArrayList<AutoPlot>();
	private DataWellPlate plate_data;
	private PlotData plot_data;
	private PlotWindow plot_window;
	private Roi roi;
	private Configuration configuration;
	
	private int current_plot = 0;
	private PlotLabel plot_label_utils;

	public void show() {
		createAndShowPlot();
	}
	
	public void changeMaxValues(int x, int y) {
		if (x > 0) { 
			this.max_x = x;
		}
		if (y > 0) {
			this.max_y = y;
		}
	}
	
	public void changeAxes(String x, String y) {
		setAxes(x, y);
		createAndShowPlot();
	}
	
	public AutoPlot(DataWellPlate plateData, String mainDirectory) {
		this.main_directory = mainDirectory;
		this.configuration = new Configuration(this.main_directory);
		this.plate_data = plateData;
		createAndShowPlot();
	}

	public Plot createPlot(DataWell wellData) {
		boolean setPlot = false;
		if (wellData == null) {
			wellData = plate_data.get(this.current_plot);
			setPlot = true;
		}
		
		if ("".equals(this.axis_x)) {
			this.axis_x = PlotLabel.INT_DEN_DYE_ONE;
		}
		
		if ("".equals(this.axis_y)) {
			this.axis_y = PlotLabel.INT_DEN_DYE_TWO;
		}
		
		PlotData plotData = new PlotData(wellData, this.axis_x, this.axis_y);
		if (setPlot) {
			/**
			 * Be aware that the pixel calculations depend on the set plotData
			 */
			this.plot_data = plotData;
		}
		PlotLabel plotLabel = new PlotLabel(this.configuration);
		Plot plot = new Plot(
			this.plate_data.getName() + " (" + plotData.getName() + ")",
			plotLabel.getAxisLabel(this.axis_x),
			plotLabel.getAxisLabel(this.axis_y),
			new float[1],
			new float[1]);
		setPlotPropertiesAndValues(plot, plotData);
		return plot;
	}
	
	public void addSubPlot(AutoPlot subPlot) {
		this.sub_plots.add(subPlot);
	}
	
	public void setPlotPropertiesAndValues(Plot plot, PlotData plotData) {
		if (this.max_x != 0) {
			plotData.setAxisMaxXValue(this.max_x);
		}
		if (this.max_y != 0) {
			plotData.setAxisMaxYValue(this.max_y);
		}
		plot.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
    	plot.setLimits(
    			plotData.getAxisMinXValue(), 
    			plotData.getAxisMaxXValue(), 
    			plotData.getAxisMinYValue(), 
    			plotData.getAxisMaxYValue());
		plot.addPoints(plotData.getXValues(), plotData.getYValues(), Plot.CIRCLE);
	}
	
	public DataWellPlate getPlateData() {
		return this.plate_data;
	}

	public PlotData getCurrentPlotData() {
		return this.plot_data;
	}
	
	public int getCurrentPlot() {
		return this.current_plot;
	}
	
	public void setCurrentPlot(int plot) {
		this.current_plot = plot;
	}
	
	public PlotWindow getPlotWindow() {
		return this.plot_window;
	}
	
	public ImagePlus getImagePlus() {
		return this.plot_window.getImagePlus();
	}
	
	public Roi getRoi() {
		return this.roi;
	}
	
	public void setRoi(Roi roi) {
		this.roi = roi;
	}
	
	public void setDyeThree() {
		this.dye_three = true;
	}
	
	public boolean isDyeThree() {
		return this.dye_three;
	}
	
	public String getMainDirectory() {
		return this.main_directory;
	}
	
	private DataWellPlate getRoiDataWellPlate() {
		setRoiAndColor(getImagePlus(), new Color(0, 255, 0, 255));
		int subCount = this.sub_plots.size();
		DataWellPlate newDataWellPlate = new DataWellPlate("[" + SUB + "_" + subCount + "] " + this.plate_data.getName());
		for (int i=0; i<this.plate_data.size(); i++) {
			DataWell wellData = this.plate_data.get(i);
			ArrayList<Object> roiCellData = getRoiObjects(wellData, DataCell.class.toString());
			DataWell newWellData = new DataWell(wellData.getName());
			for (int j=0; j<roiCellData.size(); j++) {
				if (roiCellData.get(j) instanceof DataCell) {
					newWellData.add((DataCell)roiCellData.get(j));
				}
			}
			newDataWellPlate.add(newWellData);
		}
		return newDataWellPlate;
	}
	
	public void createSubPlot() {
		this.sub_plots.add(createSubPlotAndSetDyeThree());
	}
	
	public AutoPlot getSubPlot(int plotNumber) {
		return this.sub_plots.get(plotNumber);
	}
	
	public void setRoiAndColor(ImagePlus imagePlus, Color color) {
		setRoi(imagePlus);
		AutoPlotHelper.setRoiColor(imagePlus, getRoi(), color);
	}

	public void setRoi(ImagePlus imagePlus) {
		this.roi = imagePlus.getRoi();	
	}
	
	public void setAxes(String x, String y) {
		this.axis_x = x;
		this.axis_y = y;
	}

	public ArrayList<Object> getRoiCentroids(DataWell wellData) {
		return getRoiObjects(wellData, Point.class.toString());
	}
	
	public ArrayList<Object> getRoiCellData(DataWell wellData) {
		return getRoiObjects(wellData, DataCell.class.toString());
	}
	
	private void createAndShowPlot() {
		if (this.plot_window != null && !this.plot_window.isClosed()) {
			this.plot_window.close();
		}
		Plot plot = createPlot(null);
		showPlot(plot);
		
		this.axis_x = PlotLabel.INT_DEN_DYE_ONE;
		this.axis_y = PlotLabel.INT_DEN_DYE_TWO;
		plot_label_utils = new PlotLabel(this.configuration);
		
		//DataWell wellData = plate_data.get(this.current_plot);
		//createScatterPlot(wellData);
	}
	
	private void createScatterPlot(DataWell dataWell) {
		//createPlotImageJ(dataWell, new PlotImageJ());
		createPlot(dataWell, new PlotJFreeChart());
	}

	private void createPlot(DataWell dataWell, IPlot plot) {
		PlotCreater plotCreater = new PlotCreater(plot);
		plotCreater.setDataWell(dataWell);
		plotCreater.setPlotLabels(
				plot_label_utils.createTitle(plate_data.getName(), dataWell.getName()), 
				plot_label_utils.getAxisLabel(axis_x), 
				plot_label_utils.getAxisLabel(axis_y));
		plotCreater.create();
		plotCreater.show();
	}

	private void showPlot(Plot plot) {
		this.plot_window = plot.show();
		ImagePlus ip = this.plot_window.getImagePlus();
		ip.setRoi(this.roi, true);
	}
	

	public ArrayList<Point> getRoiCentroidList(DataWell wellData) {
		ArrayList<Point> list = new ArrayList<Point>();
		Polygon polygon = getRoi().getPolygon();
		if (wellData == null) {
			wellData = this.plot_data.getWellData();
		}
		if (polygon != null) {
			PlotData plotData = new PlotData(
					wellData, 
					this.plot_data.getSelectedAxis(PlotData.X), 
					this.plot_data.getSelectedAxis(PlotData.Y));
			AutoPlotPoint[] coordinates = plotData.getPlotCoordinates();
			
			for (int i=0; i<coordinates.length; i++) {
				AutoPlotPoint dataPoint = coordinates[i];
				double x = getXPixelPosition(dataPoint.getX());
				double y = getYPixelPosition(dataPoint.getY());
				if (polygon.contains(x, y)) {
					DataCell cell = wellData.get(i);
					list.add(cell.getCentroid());
				}
			}
		} else {
			new MessageDialog(new Frame(), "No Selection Found", "Please create a selection first");
		}
	
		return list;
	}
	
	private ArrayList<Object> getRoiObjects(DataWell wellData, String classType) {
		Polygon polygon = getRoi().getPolygon();
		if (wellData == null) {
			wellData = this.plot_data.getWellData();
		}
	
		ArrayList<Object> list = new ArrayList<Object>();
		if (polygon != null) {
			PlotData plotData = new PlotData(
					wellData, 
					this.plot_data.getSelectedAxis(PlotData.X), 
					this.plot_data.getSelectedAxis(PlotData.Y));
			AutoPlotPoint[] coordinates = plotData.getPlotCoordinates();
			int count = 0;
			for (int i=0; i<coordinates.length; i++) {
				AutoPlotPoint dataPoint = coordinates[i];
				double x = getXPixelPosition(dataPoint.getX());
				double y = getYPixelPosition(dataPoint.getY());
				if (polygon.contains(x, y)) {
					DataCell cell = wellData.get(i);
					if ((Point.class.toString()).equals(classType)) {
						list.add(cell.getCentroid());
					}
					if ((DataCell.class.toString()).equals(classType)) {
						list.add(count, cell);
					}
					count++;
				}
			}
		} else {
			new MessageDialog(new Frame(), "No Selection Found", "Please create a selection first");
		}

		return list;
	}
	
	
	private AutoPlot createSubPlotAndSetDyeThree() {
		AutoPlot subPlot = new AutoPlot(getRoiDataWellPlate(), getMainDirectory());
		if (this.isDyeThree()) {
			subPlot.setDyeThree();
		}
		return subPlot;
	}
	
	/**
	 * getXPixelPosition
	 *
	 * See the manual for explanation
	 */
	private double getXPixelPosition(double value) {
		double plotWidth = (new Integer(
			IMAGE_WIDTH - (Plot.LEFT_MARGIN + Plot.RIGHT_MARGIN))
		).doubleValue();
		double xPixelPosition = (
			(plotWidth / this.plot_data.getAxisMaxXValue()) * value
		) + (new Integer(Plot.LEFT_MARGIN)).doubleValue();
		return xPixelPosition;
	}

	/**
	 * getYPixelPosition
	 *
	 * See the manual for explanation
	 */
	private double getYPixelPosition(double value) {
		double plotHeight = (new Integer(
			IMAGE_HEIGHT - (Plot.TOP_MARGIN + Plot.BOTTOM_MARGIN))
		).doubleValue();
		double yPixelPosition = (plotHeight - (
			(plotHeight / this.plot_data.getAxisMaxYValue()) * value
		)) + (new Integer(Plot.TOP_MARGIN)).doubleValue();
		return yPixelPosition;
	}
}