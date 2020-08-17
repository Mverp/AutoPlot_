package ap.roi;
import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;

import ap.AutoPlot;
import ap.data.DataWell;
import ap.data.DataWellPlate;
import ap.plot.PlotData;

import ij.ImagePlus;
import ij.gui.MessageDialog;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;

public class RoiCounter {
	private AutoPlot plotter;
	
	public RoiCounter(AutoPlot plotter) {
		this.plotter = plotter;
	}
	
	public void showAllPlotCount() {
		getCountResultTable(null);
	}
	
	public void showCurrentPlotCount() {
		getCountResultTable(plotter.getCurrentPlotData());
	}
	
	private void getCountResultTable(PlotData plotData) {
		ImagePlus plotWindowImagePlus = plotter.getImagePlus();
		if (plotWindowImagePlus.getRoi() == null) {
			new MessageDialog(new Frame(), "No ROI found", "Please create a selection before trying to count");
		} else {
			createAndShowResultTable(plotData, plotWindowImagePlus);
		}					
	}
	
	private void createAndShowResultTable(PlotData plotData, ImagePlus imagePlus) {
		this.plotter.setRoiAndColor(imagePlus, new Color(0, 255, 0, 255));
		ResultsTable rt = getResultsTable();
		if (plotData == null) {
			addPlateDataCountToResultsTable(rt, plotter.getPlateData());
		} else {
			DataWell wellData = plotData.getWellData();
			addPlotDataCountToResultsTable(rt, wellData);
		}
		rt.show("Results");
	}
	
	private ResultsTable getResultsTable() {
		ResultsTable rt = Analyzer.getResultsTable();
		if (rt == null) {
		    rt = new ResultsTable();
		    Analyzer.setResultsTable(rt);
		}
		return rt;
	}
	
	private void addPlateDataCountToResultsTable(ResultsTable rt, DataWellPlate plateData) {
		for (int i=0; i<plateData.size(); i++) {
			DataWell wellData = plateData.get(i);
			addPlotDataCountToResultsTable(rt, wellData);
		}			
	}

	private void addPlotDataCountToResultsTable(ResultsTable rt, DataWell wellData) {
		String label = wellData.getName();
		int value = countDataPoints(wellData);
		addValueToResultsTable(rt, label, value);
	}
	
	private int countDataPoints(DataWell wellData) {
		ArrayList<Object> centroids = plotter.getRoiCentroids(wellData);
		return centroids.size();
	}
	
	private void addValueToResultsTable(ResultsTable rt, String label, int value) {
		rt.incrementCounter();
		rt.setLabel(label, rt.getCounter() - 1);
		rt.addValue("Data Points", value);
	}
}
