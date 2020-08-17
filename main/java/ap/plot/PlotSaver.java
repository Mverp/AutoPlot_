package ap.plot;

import java.awt.Frame;

import ap.AutoPlot;
import ap.Files;
import ap.data.DataWell;
import ij.ImagePlus;
import ij.gui.MessageDialog;
import ij.gui.Plot;
import ij.io.FileSaver;

public class PlotSaver {
	private AutoPlot plotter;
	
	public PlotSaver(AutoPlot plotter) {
		this.plotter = plotter;
	}
	
	public void saveAllPlots() {
		for (int i=0; i<plotter.getPlateData().size(); i++) {
			DataWell wellData = plotter.getPlateData().get(i);
			Plot plot = plotter.createPlot(wellData);
			ImagePlus imagePlus = plot.getImagePlus();
			imagePlus.setRoi(plotter.getRoi(), true);
			FileSaver fs = new FileSaver(imagePlus);
			fs.saveAsTiff(Files.getImageFilePathPlot(plotter.getMainDirectory(), wellData.getName()));
		}
		new MessageDialog(new Frame(), "Plots Saved", "All plots have been saved to their corresponding directories");
	}
}