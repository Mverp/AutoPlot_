package ap;
import ij.gui.MessageDialog;

import java.awt.Frame;

import ap.window.WindowMainActions;


public class NavigatorPlot {
	private AutoPlot plot;
	
	public NavigatorPlot(AutoPlot plot) {
		this.plot = plot;
	}
	
	public void goTo(String direction) {
		if (!this.plot.getPlotWindow().isClosed()) {
			navigateCurrentPlotTo(direction);
		}
		this.plot.show();
	}
	
	private void navigateCurrentPlotTo(String direction) {
		if (WindowMainActions.PREVIOUS.equals(direction)) {
			shiftCurrentPlotPrevious();
		}
		if (WindowMainActions.NEXT.equals(direction)) {
			shiftCurrentPlotNext();
		}
	}
	
	private void shiftCurrentPlotPrevious() {
		if (this.plot.getCurrentPlot() != 0) {
			int plotNumber = this.plot.getCurrentPlot();
			this.plot.setCurrentPlot(plotNumber - 1);
		} else {
			new MessageDialog(new Frame(), "First Plot", "This is the fist plot");
		}
	}

	private void shiftCurrentPlotNext() {
		if (this.plot.getCurrentPlot() != (this.plot.getPlateData().size() - 1)) {
			int plotNumber = this.plot.getCurrentPlot();
			this.plot.setCurrentPlot(plotNumber + 1);
		} else {
			new MessageDialog(new Frame(), "Last Plot", "This is the last plot");
		}
	}
}
