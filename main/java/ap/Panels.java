package ap;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class Panels {
	public CheckboxPanel getCheckboxPanel(String label, JComponent component) {
		return new CheckboxPanel(label, component);
	}
	
	public ComboPanel getComboPanel(String title, JComponent component) {
		return new ComboPanel(title, component);
	}
	
	public TextFieldPanel getTextFieldPanel(String label, JComponent component) {
		return new TextFieldPanel(label, component);
	}
	
	private class CheckboxPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		CheckboxPanel(String label, JComponent c) {
			setLayout(new FlowLayout());
			add(c);
			this.add(new JLabel(label));
		}
	}
	
	private class ComboPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		ComboPanel(String title, JComponent c) {
			setLayout(new FlowLayout());
			setBorder(new TitledBorder(title));
			add(c);
		}
	}
	
	private class TextFieldPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		TextFieldPanel(String label, JComponent c) {
			setLayout(new FlowLayout());
			add(new JLabel(label));
			add(c);
		}
	}
}
