package ap;
import ap.data.DataWell;
import ap.plot.PlotData;
import ij.ImagePlus;
import ij.gui.HistogramWindow;
import ij.gui.NewImage;
import ij.process.ImageProcessor;


public class Histogram {
	private AutoPlot plot;
	private String data_source = "";
	private double min = 0.0;
	private double max = 0.0;
	
	public Histogram(AutoPlot plot) {
		this.plot = plot;
	}
	
	public void setDataSource(String dataSource) {
		this.data_source = dataSource;
	}
	
	public void setBoundries(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public void show() {
		DataWell wellData = this.plot.getPlateData().get(this.plot.getCurrentPlot());
		PlotData plotData = this.plot.getCurrentPlotData();
		if ("".equals(this.data_source)) {
			this.data_source = plotData.getSelectedAxis(PlotData.Y);
		}
		
		int[] histogram = new int[wellData.size()];
		for (int i=0; i<wellData.size(); i++) {
			histogram[i] = Float.floatToIntBits((new Float(plotData.getValue(i, this.data_source)).floatValue())); 
		}

		ImagePlus histogramIntermediateImage = NewImage.createFloatImage(wellData.getName(), histogram.length, 1, 1, NewImage.FILL_WHITE);
		ImageProcessor histogramIntermediateProcessor = histogramIntermediateImage.getProcessor();

		double maxValue = 0;
		for (int i=0; i<histogram.length; i++) {
			int value = histogram[i];
			if (value > maxValue) {
				maxValue = Float.intBitsToFloat(value);
			}
			histogramIntermediateProcessor.putPixel(i, 0, value);
		}
		if (this.max == 0.0) {
			this.max = Math.ceil(maxValue) + 1.0;
		}
		histogramIntermediateImage.updateAndDraw();
		new HistogramWindow(wellData.getName(), histogramIntermediateImage, (new Double(this.max)).intValue(), this.min, this.max);
	}
}
