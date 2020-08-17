package ap.trellis;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class TrellisImage {
	private int[][]pixels;
	private ImagePlus image;
	
	public TrellisImage(int[][] pixels, TrellisNode[] contourNodes) {
		this.pixels = pixels;
		createImage();
	}
	
	public void show() {
		this.image.show();
	}
	
	private void createImage() {
		int stages = this.pixels.length;
		int nodes = this.pixels[0].length;
		createAndSetImageAndSetPixels(stages, nodes);
		finalizeImage();
	}
	
	private void createAndSetImageAndSetPixels(int stages, int nodes) {
		this.image = IJ.createImage("Trellis", "16-bit black", stages, nodes, 1);
		ImageProcessor imageProcessor = this.image.getProcessor();
		setImagePixels(imageProcessor, stages, nodes);
	}
	
	private void setImagePixels(ImageProcessor imageProcessor, int stages, int nodes) {
		for(int stage = 0; stage < stages; stage++) {
			for (int node = 0; node < nodes; node++) {
				imageProcessor.putPixel(stage, node, this.getPixel(stage, node));
			}
		}
	}
	
	private void finalizeImage() {
		this.image.draw();
		IJ.run(this.image, "Enhance Contrast", "saturated=0.35");		
	}
	
	private int getPixel(int stage, int node) {
		return this.pixels[stage][node];
	}
}
