package ap;
import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;


public class Convolver extends ij.plugin.filter.Convolver {
	private static final String DIRECTION_X = "x";
	private static final String DIRECTION_Y = "y";
	
	// Note: ImageJ and MatLab convolve differently
	private static final float[] KERNEL_A = {0.037659f, 0.249153f, 0.426375f, 0.249153f, 0.037659f};
	private static final float[] KERNEL_B = {-0.109604f, -0.276691f, 0.000000f, 0.276691f, 0.109604f};
	
	private String path;
	private AutoPlot plotter;
	
	public Convolver(AutoPlot plotter) {
		this.plotter = plotter;
	}
	
	public void generateConvolvedImages(ImageProcessor imageProcessor, String name) {
		this.path = plotter.getMainDirectory() + "//" + name + "//" + name + Files.GENERIC_FILENAME_PART_CONVOLVED;
		createConvolvedImages(imageProcessor);
	}
	
	public ImageProcessor getConvolveImage(String wellName, String direction) {
		String path = plotter.getMainDirectory() + "//" + wellName + "//" + wellName + Files.GENERIC_FILENAME_PART_CONVOLVED + direction.toLowerCase() + Files.EXT_TIF;
		ImagePlus imagePlus = IJ.openImage(path);
		return imagePlus.getProcessor();
	}
	
	private void createConvolvedImages(ImageProcessor imageProcessor) {
		createConvolvedImage(imageProcessor, DIRECTION_X);
		createConvolvedImage(imageProcessor, DIRECTION_Y);
	}
	
	private void createConvolvedImage(ImageProcessor imageProcessor, String direction) {
		String name = "Convolve d" + direction.toUpperCase();
		ImageProcessor convolveImageProcessor = getConvolveImageProcessor(imageProcessor, name);
		directionalConvolve(convolveImageProcessor, direction);
		ImagePlus convolveImagePlusX = new ImagePlus(name, convolveImageProcessor);
		save(convolveImagePlusX, this.path + direction + ".tif");
	}
	
	private void directionalConvolve(ImageProcessor imageProcessor, String direction) {
		if (DIRECTION_X.equals(direction)) {
			convolve(imageProcessor, KERNEL_A, 1, KERNEL_A.length);
			convolve(imageProcessor, KERNEL_B, KERNEL_B.length, 1);
		}
		if (DIRECTION_Y.equals(direction)) {
			convolve(imageProcessor, KERNEL_A, KERNEL_A.length, 1);
			convolve(imageProcessor, KERNEL_B, 1, KERNEL_B.length);			
		}
	}
	
	private ImageProcessor getConvolveImageProcessor(ImageProcessor imageProcessor, String name) {
		ImageProcessor convolveImageProcessor = imageProcessor.duplicate();
		ImagePlus convolveImagePlus = new ImagePlus(name, convolveImageProcessor);
		ImageConverter imageConverterY = new ImageConverter(convolveImagePlus);
		imageConverterY.convertToGray32();
		return convolveImagePlus.getProcessor();
	}
	
	private void save(ImagePlus convolvedImagePlus, String path) {
		FileSaver fileSaveY = new FileSaver(convolvedImagePlus);
		fileSaveY.saveAsTiff(path);		
	}
}
