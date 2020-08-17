package ap.plot;

import ij.gui.Plot;

public class PlotImageJ implements IPlot
{

	private String label_y;
	private String label_x;
	private int width;
	private int heigth;
	private int limit_x_lower;
	private int limit_x_upper;
	private int limit_y_lower;
	private int limit_y_upper;
	private String label_plot;
	private double[] data_x;
	private double[] data_y;
	private Plot plot;


	@Override
	public void plot()
	{
		this.plot = new Plot(this.label_plot, this.label_x, this.label_y);
		this.plot.setSize(this.width, this.heigth);
		this.plot.setLimits(this.limit_x_lower, this.limit_x_upper, this.limit_y_lower, this.limit_y_upper);
		this.plot.addPoints(this.data_x, this.data_y, Plot.CIRCLE);
	}


	@Override
	public void save()
	{
		// TODO Auto-generated method stub
	}


	@Override
	public void setAxisBoundaries(int xLower, int xUpper, int yLower, int yUpper)
	{
		this.limit_x_lower = xLower;
		this.limit_x_upper = xUpper;
		this.limit_y_lower = yLower;
		this.limit_y_upper = yUpper;
	}


	@Override
	public void setData(double[] dataX, double[] dataY)
	{
		this.data_x = dataX;
		this.data_y = dataY;
	}


	@Override
	public void setDimensions(int width, int heigth)
	{
		this.width = width;
		this.heigth = heigth;
	}


	@Override
	public void setLabels(String labelPlot, String labelX, String labelY)
	{
		this.label_plot = labelPlot;
		this.label_x = labelX;
		this.label_y = labelY;
	}


	@Override
	public void show()
	{
		this.plot.show();
	}
}
