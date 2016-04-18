package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Library.OperationsAndConstants;
import Library.Student;

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
				if(OperationsAndConstants.isInt(fio.getText()) == -1 && 
						OperationsAndConstants.isInt(numberGrup.getText()) == 1 && 
						OperationsAndConstants.isInt(sickness.getText()) == 1 && 
						OperationsAndConstants.isInt(otherCauses.getText()) == 1 && 
						OperationsAndConstants.isInt(withoutGoodReason.getText()) == 1) {
					client.sendToServer(OperationsAndConstants.ADD);
					if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
						frame.sendServerPageAndRecords();
						client.sendToServer(new Student(fio.getText(), numberGrup.getText(), 
								Integer.parseInt(sickness.getText()), Integer.parseInt(otherCauses.getText()),
								Integer.parseInt(withoutGoodReason.getText())));
						if(client.receiveFromServer().equals(OperationsAndConstants.CHANGE_TABLE)) {
							frame.setMaxPage((Integer) client.receiveFromServer());
							frame.setTable((List<Student>) client.receiveFromServer());
						}
					}
				}
				ad.setVisible(false); 
			}
		});
		jp = new JPanel();
		jp.add(add);
		this.add(jp, BorderLayout.SOUTH); 
		setResizable (false);
		this.setSize(600, 300);
		this.setLocationRelativeTo(frame);
		this.setVisible(true); 
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
