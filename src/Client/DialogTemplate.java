package Client;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

public class DialogTemplate extends JDialog {
	protected MyFrame frame;
	
	protected final JLabel FIO = new JLabel("  FIO");
	protected final JLabel NUMBERGRUP = new JLabel("  Grup");
	
	protected JTextField fio = new JTextField(40);
	protected JTextField numberGrup = new JTextField(6);
	
	protected Client client;
	
	DialogTemplate(String str, MyFrame frame) {
		super(frame, str, true);
		this.frame = frame;
		client = frame.getClient();
	}

	public String getFio() {
		return fio.getText();
	}

	public String getNumberGrup() {
		return numberGrup.getText();
	}

	public MyFrame getFrame() {
		return frame;
	}
	
	public JTextField getTextFieldFio() {
		return fio;
	}
	
	public JTextField getTextFieldNumberGrup() {
		return numberGrup ;
	}
}
