package ap.plot;

import ap.AutoPlot;
import ap.AutoPlotHelper;
import ap.data.DataWell;

public class PlotCreater {
	private IPlot plot;
	private DataWell well_data;
	private String label_x;
	private String label_y;
	private String label_plot;
	
	public PlotCreater(IPlot plot) {
		this.plot = plot;
	}
	
	public void setDataWell(DataWell wellData) {
		this.well_data = wellData;
	}
	
	public void setPlotLabels(String labelPlot, String labelX, String labelY) {
		this.label_plot = labelPlot;
		this.label_x = labelX;
		this.label_y = labelY;
	}
	
	public void create() {
		this.plot.setLabels(this.label_plot, this.label_x, this.label_y);
		this.plot.setDimensions(AutoPlot.IMAGE_WIDTH, AutoPlot.IMAGE_HEIGHT);
		this.plot.setData(getDataX(), getDataY());
		this.plot.setAxisBoundaries(0, getMaxValueX(), 0, getMaxValueY());
		this.plot.plot();
	}
	
	public void show() {
		this.plot.show();
	}
	
	private double[] getDataX() {
		double[] data = new double[this.well_data.size()];
		for (int i=0; i<this.well_data.size(); i++) {
			double value = AutoPlotHelper.getLn(new Float(this.well_data.get(i).getIntDenGfp()).doubleValue());
			data[i] = value;
		}
		return data;
	}
	
	private double[] getDataY() {
		double[] data = new double[this.well_data.size()];
		for (int i=0; i<this.well_data.size(); i++) {
			double value = AutoPlotHelper.getLn(new Float(this.well_data.get(i).getIntDenPi()).doubleValue());
			data[i] = value; 
		}
		return data;		
	}
	
	private int getMaxValueX() {
		double maxValue = 0;
		for (int i=0; i<this.well_data.size(); i++) {
			double value = this.well_data.get(i).getIntDenGfp();
			if(value > maxValue) {
				maxValue = value;
			}
		}
		return new Double(maxValue).intValue();
	}
	
	private int getMaxValueY() {
		double maxValue = 0;
		for (int i=0; i<this.well_data.size(); i++) {
			double value = this.well_data.get(i).getIntDenPi();
			if(value > maxValue) {
				maxValue = value;
			}
		}
		return new Double(maxValue).intValue();		
	}
}
