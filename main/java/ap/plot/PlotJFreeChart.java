package ap.plot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotJFreeChart implements IPlot
{

	@SuppressWarnings("serial")
	private class MyRenderer extends XYLineAndShapeRenderer
	{

		private XYSeries series;
		private double max_y;


		public MyRenderer(XYSeries series)
		{
			super(false, true);
			this.series = series;
			this.max_y = series.getMaxY();
		}


		@Override
		public Paint getItemPaint(int row, int column)
		{
			double y = this.series.getY(column).floatValue() / this.max_y;
			double input = 0.15 - (0.3 * y);
			double result = 0.15 + ((1.5 * input) / Math.sqrt(1 + Math.pow(input, 2d)));
			// System.out.println(" result: " + result);
			return Color.getHSBColor((float) (result), 1, 1);
		}
	}

	private String label_plot;
	private String label_x;
	private String label_y;
	private int limit_x_lower;
	private int limit_x_upper;
	private int limit_y_lower;
	private int limit_y_upper;
	private int width;
	private int heigth;
	private double[] data_y;
	private double[] data_x;

	private JFreeChart chart;


	@Override
	public void plot()
	{
		Color darkGray = new Color(64, 64, 64);

		XYSeriesCollection dataSet = new XYSeriesCollection();
		XYSeries series = new XYSeries("Random");
		for (int i = 0; i < this.data_x.length; i++)
		{
			series.add(this.data_x[i], this.data_y[i]);
		}
		dataSet.addSeries(series);
		JFreeChart chart = ChartFactory.createScatterPlot("", this.label_x, this.label_y, dataSet, PlotOrientation.VERTICAL, false, false, false);
		chart.setBackgroundPaint(darkGray);

		XYPlot plot = chart.getXYPlot();

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
		TickUnits units = new TickUnits();
		units.add(new NumberTickUnit(1.0));

		plot.setOutlinePaint(Color.GRAY);
		ValueAxis domain = plot.getDomainAxis();
		domain.setLabelPaint(Color.LIGHT_GRAY);
		domain.setTickLabelPaint(Color.GRAY);
		domain.setLabelFont(font);
		domain.setStandardTickUnits(units);

		ValueAxis range = plot.getRangeAxis();
		range.setLabelPaint(Color.LIGHT_GRAY);
		range.setTickLabelPaint(Color.GRAY);
		range.setLabelFont(font);
		range.setStandardTickUnits(units);

		MyRenderer renderer = new MyRenderer(series);
		Shape shape = new Ellipse2D.Double(-3, -3, 6, 6);
		renderer.setSeriesShape(0, shape);
		renderer.setSeriesLinesVisible(0, false);
		renderer.setDefaultLinesVisible(false);
		renderer.setDrawOutlines(false);

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(darkGray);

		this.chart = chart;
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
		ChartFrame frame = new ChartFrame(this.label_plot, this.chart);
		frame.pack();
		frame.setVisible(true);
	}

}
