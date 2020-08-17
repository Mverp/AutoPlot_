package ap;
import ij.measure.Measurements;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

public class AutoPlotImageProcessor {
	private ImageProcessor image_processor;
	private double original_mean;

	public AutoPlotImageProcessor(ImageProcessor imageProcessor) {
		this.image_processor = imageProcessor;
		this.original_mean = ImageStatistics.getStatistics(this.image_processor, Measurements.MEAN, null).mean;
	}
	
	public ImageProcessor getImageProcessor() {
		return this.image_processor;
	}
	
	public double getMean() {
		return this.original_mean;
	}
}