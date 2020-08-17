package ap.plot;

public interface IPlot {
	public void setLabels(String labelPlot, String labelX, String labelY);
	public void setData(double[] dataX, double[] dataY);
	public void setAxisBoundaries(int xLower, int xUpper, int yLower, int yUpper);
	public void setDimensions(int width, int heigth);
	public void plot();
	public void show();
	public void save();
}
