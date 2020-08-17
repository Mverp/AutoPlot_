package ap.window;
import ij.WindowManager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ap.AutoPlot;
import ap.Panels;
import ap.trellis.TrellisProcessor;

public class WindowTrellisOptions extends Window implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Dye 3 processing options";
	private static final String LABEL_CONTOUR_ANALYSIS = "Perform contour analysis";
	private static final String LABEL_SAVE_IMAGES = "Save contour images";
	private static final String LABEL_OPEN_IMAGES = "Open contour images";
	private static final String LABEL_BUTTON_OK = "Ok";
	
	private static final String ACTION_COMMAND_CONTOUR = "contour";
	private static final String ACTION_COMMAND_SAVE = "save contour images";
	
	private JCheckBox checkbox_contour = new JCheckBox();
	private JCheckBox checkbox_save_contour_images = new JCheckBox();
	private JCheckBox checkbox_show_contour_images = new JCheckBox();
	
	private JPanel panel;
	private AutoPlot plot;
	
	public WindowTrellisOptions(AutoPlot plot) {
		super(TITLE);
		this.plot = plot;
		WindowManager.addWindow(this);
		createGui();
	}
	
	public void createGui() {
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(4, 1));
		this.panel.setBorder(new TitledBorder(TITLE));
		
		Panels windowPanels = new Panels();
		JPanel checkboxPanelAnalysis = windowPanels.getCheckboxPanel(LABEL_CONTOUR_ANALYSIS, this.checkbox_contour);
		this.checkbox_contour.addActionListener(this);
		this.checkbox_contour.setActionCommand(ACTION_COMMAND_CONTOUR);
		this.panel.add(checkboxPanelAnalysis);
		
		JPanel checkboxPanelSave = windowPanels.getCheckboxPanel(LABEL_SAVE_IMAGES, this.checkbox_save_contour_images);
		this.checkbox_save_contour_images.addActionListener(this);
		this.checkbox_save_contour_images.setActionCommand(ACTION_COMMAND_SAVE);
		this.checkbox_save_contour_images.setEnabled(false);
		this.panel.add(checkboxPanelSave);
		
		JPanel checkboxPanelOpenImages = windowPanels.getCheckboxPanel(LABEL_OPEN_IMAGES, this.checkbox_show_contour_images);
		this.checkbox_show_contour_images.setEnabled(false);
		this.panel.add(checkboxPanelOpenImages);
		
		
		JButton button = new JButton(LABEL_BUTTON_OK);
		button.addActionListener(this);
		this.panel.add(button);
		this.add(this.panel);
		setup();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(LABEL_BUTTON_OK)) {
			this.close();
			if (this.checkbox_contour.isSelected()) {
				this.plot.setDyeThree();
				TrellisProcessor trellisProcessor = new TrellisProcessor(this.plot);
				if (this.checkbox_save_contour_images.isSelected()) {
					trellisProcessor.setSave();
				}
				if (this.checkbox_show_contour_images.isSelected()) {
					trellisProcessor.setShow();
				}
				trellisProcessor.process();
			}
		}
		if (command.equals(ACTION_COMMAND_CONTOUR)) {
			this.checkbox_save_contour_images.setEnabled(true);
		}
		if (command.equals(ACTION_COMMAND_SAVE)) {
			this.checkbox_show_contour_images.setEnabled(true);
		}
	}
}
