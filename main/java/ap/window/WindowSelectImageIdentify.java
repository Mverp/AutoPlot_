package ap.window;
import ij.WindowManager;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ap.AutoPlot;
import ap.Files;
import ap.roi.RoiIdentifier;

public class WindowSelectImageIdentify extends Window implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Select image";
	private static final String LABEL_IMAGES = "Available images";
	private static final String LABEL_BUTTON_IDENTIFY = "Identify Cells";
	
	private AutoPlot plotter;
	private JComboBox combobox = new JComboBox();
	private JPanel panel;
	
	public WindowSelectImageIdentify(AutoPlot plotter) {
		super(TITLE);
		this.plotter = plotter;
		WindowManager.addWindow(this);
		createGui();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (LABEL_BUTTON_IDENTIFY.equals(command)) {
			identifyCells();
		}
		this.close();
	}
	
	private void createGui() {
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(2, 1));
		
		String[] options = getOptions();
		for(int i = 0; i < options.length; i++) {
			this.combobox.addItem(options[i]);
		}
		
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new FlowLayout());
		comboPanel.setBorder(new TitledBorder(LABEL_IMAGES));
		comboPanel.add(this.combobox);
		this.panel.add(comboPanel);
		
		JButton button = new JButton(LABEL_BUTTON_IDENTIFY);
		button.addActionListener(this);
		button.setSize(40, 20);
		this.panel.add(button);
		this.add(this.panel);
		setup();
	}
	
	private void identifyCells() {
		String image = (String) this.combobox.getSelectedItem();
		RoiIdentifier identifier = new RoiIdentifier(this.plotter, image);
		identifier.identifyDataPoints();
	}
	
	private String[] getOptions() {
		String mainDirectory = this.plotter.getMainDirectory();
		String wellDirectory = this.plotter.getCurrentPlotData().getName();
		File well = new File(mainDirectory + File.separator + wellDirectory);
		String[] options = well.list(Files.getFileNameFilterTif());
		return options;
	}
}
