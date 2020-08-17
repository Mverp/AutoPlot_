package ap.roi;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import ap.AutoPlot;
import ap.plot.PlotData;

public class RoiIdentifier {
	public static final String MERGED_CHANNELS_IMAGE_ID = " -- Merged Channels.tif";
	
	private AutoPlot plotter;
	private String identification_image;
	
	public RoiIdentifier(AutoPlot plotter) {
		this.plotter = plotter;
	}
	
	public RoiIdentifier(AutoPlot plotter, String identificationImage) {
		this.plotter = plotter;
		this.identification_image = identificationImage;
	}
	
	public void identifyDataPoints() {
		ImagePlus plotImagePlus = plotter.getImagePlus();
		plotter.setRoi(plotImagePlus);
		PlotData plotData = plotter.getCurrentPlotData();
		String identificationImage = getIdentificationImage(plotData);
		String mergedChannelsImage = 
			plotter.getMainDirectory() + plotData.getName() + File.separator + identificationImage;
		showCentroidsIn(mergedChannelsImage);
	}
	
	private String getIdentificationImage(PlotData plotData) {
		String image = plotData.getName() + MERGED_CHANNELS_IMAGE_ID;
		if (this.identification_image != null) {
			image = this.identification_image;
		}
		return image;
	}

	private void showCentroidsIn(String imagePath) {
		ImagePlus mergedChannelsImagePlus = IJ.openImage(imagePath);
		ImageProcessor mergedChannelsImageProcessor = mergedChannelsImagePlus.getProcessor();

		ArrayList<Object> centroids = plotter.getRoiCentroids(null);
		for(int i=0; i<centroids.size(); i++) {
			int diameter = 70;
			mergedChannelsImageProcessor.setColor(new Color(0, 255, 255, 0));
			Point point = new Point();
			if (centroids.get(i) instanceof Point) {
				point = (Point)centroids.get(i);
			}
			mergedChannelsImageProcessor.drawOval(
				(new Double(point.getX())).intValue() - (diameter/2), 
				(new Double(point.getY())).intValue() - (diameter/2),
				diameter, 
				diameter
			);
		}
		mergedChannelsImagePlus.updateAndDraw();
		mergedChannelsImagePlus.show();	
	}
}
