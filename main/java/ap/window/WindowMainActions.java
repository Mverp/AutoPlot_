package ap.window;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ap.AutoPlot;
import ap.NavigatorImage;
import ap.NavigatorPlot;
import ap.gallery.SubImageGallery;
import ap.plot.PlotSaver;
import ap.roi.RoiCounter;
import ap.trellis.TrellisProcessor;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.MessageDialog;
import ij.plugin.frame.PlugInFrame;

public class WindowMainActions extends PlugInFrame implements ActionListener
{
	public static final String TITLE = "Plot Action Window";

	private static final long serialVersionUID = 1L;
	private static final String COUNT = "Count";
	private static final String COUNT_ALL = "Count all";
	private static final String SAVE_ALL_PLOTS = "Save Plots";
	private static final String PLOT_SETTINGS = "Plot Settings";
	private static final String SUB_PLOT = "Create Sub Plot";
	private static final String HISTOGRAM = "Histogram";
	private static final String IDENTIFY_CELLS = "Identify Cells";
	private static final String SUBIMAGE_GALLERY = "Obtain Subimages";
	private static final String CALCULATE_TRELLIS = "(Re)calculate Dye 3";
	private static final String DRAW_CONTOURS = "Draw Annexin Contours";
	private static final String DEBUG_TRELLIS = "Debug Trellis";

	public static final String PREVIOUS = "Previous";
	public static final String NEXT = "Next";

//	private static Dimension preferred_size = new Dimension(new Dimension(185, 350));
	private static Dimension preferred_size = new Dimension(new Dimension(185, 290));

	private JPanel panel;
	private AutoPlot plot;


	public WindowMainActions(AutoPlot plot)
	{
		super(TITLE);
		this.plot = plot;
		WindowManager.addWindow(this);

		setPreferredSize(preferred_size);
		setResizable(true);
		createGui();
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if (command.equals(COUNT))
		{
			countCurrentPlot();
		}
		else if (command.equals(COUNT_ALL))
		{
			countAllPlots();
		}
		else if (command.equals(SAVE_ALL_PLOTS))
		{
			saveAllPlots();
		}
		else if (command.equals(PLOT_SETTINGS))
		{
			openPlotSettingsWindow();
		}
		else if (command.equals(SUB_PLOT))
		{
			createSubPlot();
		}
		else if (command.equals(HISTOGRAM))
		{
			createHistogram();
		}
		else if (command.equals(IDENTIFY_CELLS))
		{
			identifyRoiDataPoints();
		}
		else if (command.equals(SUBIMAGE_GALLERY))
		{
			generateSubimageGalleries();
		}
		else if (command.equals(CALCULATE_TRELLIS))
		{
			calculateTrellis();
		}
		else if (command.equals(DRAW_CONTOURS))
		{
			drawContours();
		}
		else if (command.equals(PREVIOUS))
		{
			navigate(PREVIOUS);
		}
		else if (command.equals(NEXT))
		{
			navigate(NEXT);
		}
		else if (command.equals(DEBUG_TRELLIS))
		{
			debugTrellis();
		}
	}


	private void calculateTrellis()
	{
		AutoPlot plot = getActivePlot();
		if (plot.isDyeThree())
		{
			WindowTrellisOptions windowTrellisOptions = new WindowTrellisOptions(plot);
			windowTrellisOptions.setVisible(true);
		}
		else
		{
			new MessageDialog(new Frame(), "No Third Dye", "No 'dye 3' information found.");
		}
	}


	private void countAllPlots()
	{
		RoiCounter counter = new RoiCounter(getActivePlot());
		counter.showAllPlotCount();
	}


	private void countCurrentPlot()
	{
		RoiCounter counter = new RoiCounter(getActivePlot());
		counter.showCurrentPlotCount();
	}


	public void createGui()
	{
//		String[] buttons = {COUNT, COUNT_ALL, SAVE_ALL_PLOTS, PLOT_SETTINGS, IDENTIFY_CELLS, SUB_PLOT, HISTOGRAM, SUBIMAGE_GALLERY, CALCULATE_TRELLIS, PREVIOUS, NEXT, DRAW_CONTOURS, DEBUG_TRELLIS};
		String[] buttons = { COUNT, COUNT_ALL, SAVE_ALL_PLOTS, PLOT_SETTINGS, IDENTIFY_CELLS, SUB_PLOT, HISTOGRAM, SUBIMAGE_GALLERY, CALCULATE_TRELLIS, PREVIOUS, NEXT, DRAW_CONTOURS };
//		String[] buttons = {COUNT, COUNT_ALL, SAVE_ALL_PLOTS, PLOT_SETTINGS, IDENTIFY_CELLS, SUB_PLOT, HISTOGRAM, SUBIMAGE_GALLERY, CALCULATE_TRELLIS, PREVIOUS, NEXT};
		this.panel = new JPanel();
		for (int i = 0; i < buttons.length; i++)
		{
			JButton button = new JButton(buttons[i]);
			button.addActionListener(this);
			this.panel.add(button);
		}
		this.add(this.panel);
		pack();
	}


	private void createHistogram()
	{
		WindowHistogramOptions windowHistogramOptions = new WindowHistogramOptions(getActivePlot());
		Dimension windowsDim = windowHistogramOptions.getSize();
		windowHistogramOptions.setVisible(true);
		// Reset window size to fix window staying minimized on some systems
		windowHistogramOptions.setExtendedState(WindowHistogramOptions.NORMAL);
		windowHistogramOptions.setSize(windowsDim);
	}


	private void createSubPlot()
	{
		getActivePlot().createSubPlot();
	}


	private void debugTrellis()
	{
		TrellisProcessor trellisProcessor = new TrellisProcessor(getActivePlot());
		trellisProcessor.debugTrellis();
	}


	private void drawContours()
	{
		TrellisProcessor trellisProcessor = new TrellisProcessor(getActivePlot());
		trellisProcessor.setSave();
		trellisProcessor.setShow();
		trellisProcessor.process();
	}


	private void generateSubimageGalleries()
	{
		SubImageGallery gallery = new SubImageGallery(getActivePlot());
		gallery.create();
	}


	private AutoPlot getActivePlot()
	{
		ImagePlus activeImagePlus = WindowManager.getCurrentImage();
		String title = activeImagePlus.getTitle();
		AutoPlot plotter = this.plot;
		if (title.substring(1, 4).equals(AutoPlot.SUB))
		{
			plotter = plotter.getSubPlot(new Integer(title.substring(5, 6)).intValue());
		}
		return plotter;
	}


	private void identifyRoiDataPoints()
	{
		WindowSelectImageIdentify windowSelectImageIdentify = new WindowSelectImageIdentify(getActivePlot());
		Dimension windowSize = windowSelectImageIdentify.getSize();
		windowSelectImageIdentify.setVisible(true);
		// Reset window size to fix window staying minimized on some systems
		windowSelectImageIdentify.setExtendedState(WindowSelectImageIdentify.NORMAL);
		windowSelectImageIdentify.setSize(windowSize);
	}


	private void navigate(String direction)
	{
		String imageDirectory = IJ.getDirectory("image");
		if (imageDirectory != null)
		{
			NavigatorImage imageNavigator = new NavigatorImage(imageDirectory);
			imageNavigator.goTo(direction);
		}
		else
		{
			NavigatorPlot plotNavigator = new NavigatorPlot(getActivePlot());
			plotNavigator.goTo(direction);
		}
	}


	private void openPlotSettingsWindow()
	{
		WindowSelectAxis windowSelectAxis = new WindowSelectAxis(getActivePlot());
		Dimension windowSize = windowSelectAxis.getSize();
		windowSelectAxis.setVisible(true);
		// Reset window size to fix window staying minimized on some systems
		windowSelectAxis.setExtendedState(WindowSelectAxis.NORMAL);
		windowSelectAxis.setSize(windowSize);
	}


	private void saveAllPlots()
	{
		PlotSaver plotSaver = new PlotSaver(getActivePlot());
		plotSaver.saveAllPlots();
	}
}