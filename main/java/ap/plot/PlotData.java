package ap.plot;
import java.util.ArrayList;
import java.util.Arrays;

import ap.AutoPlot;
import ap.AutoPlotHelper;
import ap.AutoPlotPoint;
import ap.data.DataCell;
import ap.data.DataWell;

import ij.IJ;

public class PlotData {
	public static final double MAX_VALUE_MARGIN_PERCENTAGE = 10d;
	public static final String X = "x";
	public static final String Y = "y";
	public static final String[] PLOTABLE_OPTIONS = {PlotLabel.INT_DEN_DYE_ONE, PlotLabel.INT_DEN_DYE_TWO};
	
	private DataWell well_data;
	private String selected_x;
	private String selected_y;
	
	private int min_x_value = 0;
	private int max_x_value = 0;
	private int min_y_value = 0;
	private int max_y_value = 0;
	
	public PlotData(DataWell wellData) {
		this.well_data = wellData;
	}
	
	public PlotData(DataWell wellData, String x, String y) {
		this.well_data = wellData;
		this.selected_x = x;
		this.selected_y = y;
	}
	
	public DataWell getWellData() {
		return this.well_data;
	}
	
	public DataCell get(int index) {
		return well_data.get(index);
	}
	
	public String getName() {
		return well_data.getName();
	}
	
	public AutoPlotPoint[] getPlotCoordinates() {
		return getCoordinates(this.well_data);
	}
	
	public AutoPlotPoint[] getPlotCoordinates(DataWell wellData) {
		return getCoordinates(wellData);
	}
	
	private AutoPlotPoint[] getCoordinates(DataWell wellData) {
		AutoPlotPoint[] coordinates = new AutoPlotPoint[wellData.size()];
		for (int i=0; i<wellData.size(); i++) {
			double x = getValue(i, X);
			double y = getValue(i, Y);
			AutoPlotPoint dataPoint = new AutoPlotPoint(x, y);
			coordinates[i] = dataPoint;
		}
		return coordinates;
	}
 	
	public double[] getXValues() {
		double[] xValues = new double[well_data.size()];
		for (int i=0; i<well_data.size(); i++) {
			xValues[i] = getValue(i, X);
		}
		return xValues;
	}

	public double[] getYValues() {
		double[] yValues = new double[well_data.size()];
		for (int i=0; i<well_data.size(); i++) {
			yValues[i] = getValue(i, Y);
		}
		return yValues;
	}
	
	public int getAxisMinXValue() {
		return this.min_x_value;
	}
	
	public int getAxisMinYValue() {
		return this.min_y_value;
	}
	
	public double getAxisMaxXValue() {
		if (this.max_x_value != 0) {
			return this.max_x_value;
		}
		return getAxisMaxValue(X, MAX_VALUE_MARGIN_PERCENTAGE);
	}

	public double getAxisMaxYValue() {
		if (this.max_y_value != 0) {
			return this.max_y_value;
		}
		return getAxisMaxValue(Y, MAX_VALUE_MARGIN_PERCENTAGE);
	}
	
	public void setAxisMaxXValue(int x) {
		this.max_x_value = x;
	}
	
	public void setAxisMaxYValue(int y) {
		this.max_y_value = y;
	}
	
	public double getDataMaxValue(String axis) {
		double maxValue = 0;
		for (int i=0; i<this.well_data.size(); i++) {
			double value = getValue(i, axis);
			if(value > maxValue) {
				maxValue = value;
			}
		}
		return maxValue;
	}

	public String getSelectedAxis(String axis) {
		if (X.equals(axis)) {
			return selected_x;
		} else {
			return selected_y;
		}
	}
	
	public double getValue(int index, String axis) {
		float fValue = 0f;
		
		String selectedAxis = axis;
		if (X.equals(axis) || Y.equals(axis)) {
			selectedAxis = getSelectedAxis(axis);
		}
		
		if (PlotLabel.INT_DEN_DYE_ONE.equals(selectedAxis)) {
			fValue = this.well_data.get(index).getIntDenGfp();
		} else if (PlotLabel.INT_DEN_DYE_TWO.equals(selectedAxis)) {
			fValue = this.well_data.get(index).getIntDenPi();
		} else if (PlotLabel.TRELLIS_VALUE_DYE_THREE.equals(selectedAxis)) {
			fValue = this.well_data.get(index).getValueDyeThree();
		} else {
			IJ.log("No axis selected");
		}
		
		double value = (new Float(fValue)).doubleValue();
		value = AutoPlotHelper.getLn(value);
		return value;
	}
	
	public static ArrayList<String> getPlotableOptions(AutoPlot plot) {
		ArrayList<String> options = new ArrayList<String>(Arrays.asList(PLOTABLE_OPTIONS));
		if (plot.isDyeThree()) {
			options.add(PlotLabel.TRELLIS_VALUE_DYE_THREE);
		}
		return options;
	}

	private double getAxisMaxValue(String axis, double maxValueMarginPercentage) {
		double maxValue = getDataMaxValue(axis);
		double maxMultiplier = 1 + (maxValueMarginPercentage / 100.0);
		maxValue = maxValue * maxMultiplier;
		return maxValue;
	}
}