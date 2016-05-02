package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import library.OperationsAndConstants;
import library.Student;

public class AddDialog extends DialogTemplate {	
	protected final JLabel SICKNESS = new JLabel("  Пропуски по болезни: ");
	protected final JLabel OTHERCAUSES = new JLabel("  Пропуски по другим причинам: ");
	protected final JLabel WITHOUTGOODREASON = new JLabel("  Пропуски без уважительной причины: ");
	
	protected JTextField sickness = new JTextField(6);
	protected JTextField otherCauses = new JTextField(6);
	protected JTextField withoutGoodReason = new JTextField(6);
	
	public AddDialog(MyFrame frame) {
		super("Add person", frame);
		JPanel jp = new JPanel(); 
		jp.setLayout(new GridLayout(5,2));
		jp.add(FIO);
		jp.add(fio);
		jp.add(NUMBERGRUP);
		jp.add(numberGrup);
		jp.add(SICKNESS);
		jp.add(sickness);
		jp.add(OTHERCAUSES);
		jp.add(otherCauses);
		jp.add(WITHOUTGOODREASON);
		jp.add(withoutGoodReason);
		this.setLayout(new BorderLayout());
		this.add(jp,BorderLayout.CENTER);
		JButton add = new JButton("Add");
		AddDialog ad = this;
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.chechAdd();
				ad.setVisible(false); 
			}
		});
		jp = new JPanel();
		jp.add(add);
		this.add(jp, BorderLayout.SOUTH); 
		setResizable (false);
		this.setSize(600, 300);
		this.setLocationRelativeTo(frame);
		this.setVisible(false); 
	}

	public JTextField getTextFieldSickness() {
		return sickness;
	}
	
	public JTextField getTextFieldOtherCauses() {
		return otherCauses;
	}
	
	public JTextField getTextFieldWithoutGoodReason() {
		return withoutGoodReason;
	}
}
