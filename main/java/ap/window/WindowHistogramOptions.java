package ap.window;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import ap.AutoPlot;
import ap.Histogram;
import ap.Panels;
import ap.plot.PlotData;
import ij.WindowManager;

public class WindowHistogramOptions extends Window implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Histogram Options";
	private static final String LABEL_COMBO = "Data Source";
	private static final String LABEL_MIN = "Min value";
	private static final String LABEL_MAX = "Max value";
	private static final String LABEL_BUTTON_CREATE_HISTOGRAM = "Create histogram";

	private Panel panel;
	private AutoPlot plot;
	private JComboBox<String> combobox_data_course = new JComboBox<String>();
	private JFormattedTextField min = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private JFormattedTextField max = new JFormattedTextField(NumberFormat.getIntegerInstance());


	public WindowHistogramOptions(AutoPlot aPlot)
	{
		super(TITLE);
		this.plot = aPlot;
		WindowManager.addWindow(this);
		createGui();
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if (command.equals(LABEL_BUTTON_CREATE_HISTOGRAM))
		{
			Histogram histogram = new Histogram(this.plot);
			System.out.println(this.combobox_data_course.getSelectedItem());
			histogram.setDataSource((String) this.combobox_data_course.getSelectedItem());
			long tmpMin = 0L;
			if (this.min.getValue() != null)
			{
				tmpMin = (Long) this.min.getValue();
			}
			long tmpMax = 0L;
			if (this.max.getValue() != null)
			{
				tmpMax = (Long) this.max.getValue();
			}
			int min = (int) tmpMin;
			int max = (int) tmpMax;

			histogram.setBoundries(min, max);
			histogram.show();
			this.close();
		}
	}


	private void createGui()
	{
		this.panel = new Panel();
		this.panel.setLayout(new GridLayout(4, 1));
		ArrayList<String> options = PlotData.getPlotableOptions(this.plot);
		for (int i = 0; i < options.size(); i++)
		{
			this.combobox_data_course.addItem(options.get(i));
		}
		Panels windowPanels = new Panels();
		JPanel comboPanel = windowPanels.getComboPanel(LABEL_COMBO, this.combobox_data_course);
		this.panel.add(comboPanel);

		this.min.setColumns(6);
		JPanel minPanel = windowPanels.getTextFieldPanel(LABEL_MIN, this.min);
		this.panel.add(minPanel);

		this.max.setColumns(6);
		JPanel maxPanel = windowPanels.getTextFieldPanel(LABEL_MAX, this.max);
		this.panel.add(maxPanel);

		JButton button = new JButton(LABEL_BUTTON_CREATE_HISTOGRAM);
		button.addActionListener(this);
		this.panel.add(button);
		this.add(this.panel);
		setup();
	}
}
