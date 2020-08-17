package ap.window;
import ij.WindowManager;

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
import ap.Panels;
import ap.plot.PlotData;

public class WindowSelectAxis extends Window implements ActionListener {
	public static final String TITLE = "Axis Selection Window";
	
	private static final String LABEL_COMBO_X = "X-axis";
	private static final String LABEL_COMBO_Y = "Y-axis";
	private static final String LABEL_BUTTON_DRAW_PLOT = "Draw Plot";
	
	private static final long serialVersionUID = 1L;
	
	private JComboBox combobox_x_axis = new JComboBox();
	private JComboBox combobox_y_axis = new JComboBox();
	private JFormattedTextField max_x_axis = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private JFormattedTextField max_y_axis = new JFormattedTextField(NumberFormat.getIntegerInstance());
	
	private Panel panel;
	private AutoPlot plot;
	
	public WindowSelectAxis(AutoPlot plot) {
		super(TITLE);
		this.plot = plot;
		WindowManager.addWindow(this);
		createGui();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		//..
		if (command.equals(LABEL_BUTTON_DRAW_PLOT)) {
			this.close();
			String x = (String) this.combobox_x_axis.getSelectedItem();
			String y = (String) this.combobox_y_axis.getSelectedItem();
			
			int maxX = (int) getMaxValue(this.max_x_axis);
			int maxY = (int) getMaxValue(this.max_y_axis);
			
			this.plot.changeMaxValues(maxX, maxY);
			this.plot.changeAxes(x, y);
		}
		//..
	}
	
	private long getMaxValue(JFormattedTextField textField) {
		if (textField.getValue() != null) {
			return (Long) textField.getValue();
		}
		return 0L;		
	}
	
	private void createGui() {
		this.panel = new Panel();
		this.panel.setLayout(new GridLayout(3, 1));
		ArrayList<String> options = PlotData.getPlotableOptions(this.plot);
		for(int i = 0; i < options.size(); i++) {
			this.combobox_x_axis.addItem(options.get(i));
			this.combobox_y_axis.addItem(options.get(i));
		}
		max_x_axis.setColumns(6);
		max_y_axis.setColumns(6);
		
		Panels windowPanels = new Panels();
		JPanel comboPanelX = windowPanels.getComboPanel(LABEL_COMBO_X, this.combobox_x_axis);
		comboPanelX.add(max_x_axis);
		this.panel.add(comboPanelX);
		
		JPanel comboPanelY = windowPanels.getComboPanel(LABEL_COMBO_Y, this.combobox_y_axis);
		comboPanelY.add(max_y_axis);
		this.panel.add(comboPanelY);
		
		JButton button = new JButton(LABEL_BUTTON_DRAW_PLOT);
		button.addActionListener(this);
		this.panel.add(button);
		this.add(panel);
		setup();
	}
}