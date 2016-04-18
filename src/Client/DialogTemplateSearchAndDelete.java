package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Library.OperationsAndConstants;

public class DialogTemplateSearchAndDelete extends DialogTemplate {
	protected final String [] TYPESOFPERMITS = {"------", "По болезни", "По другим причинам", 
			"Без уважительной причины"};
	protected String cause;
	
	protected int happening = 0;

	protected JPanel jp = new JPanel();
	
	protected final JLabel KIND = new JLabel("  Kind");
	protected final JLabel SICKNESS = new JLabel("  Absences due to illness (min/max): ");
	protected final JLabel OTHERCAUSES = new JLabel("  Absences for other reasons (min/max): ");
	protected final JLabel WITHOUTGOODREASON = new JLabel("  Unexcused absence (min/max): ");

	protected JTextField sicknessMin = new JTextField(6);
	protected JTextField sicknessMax = new JTextField(6);
	protected JTextField otherCausesMin = new JTextField(6);
	protected JTextField otherCausesMax = new JTextField(6);
	protected JTextField withoutGoodReasonMin = new JTextField(6);
	protected JTextField withoutGoodReasonMax = new JTextField(6);
	
	JComboBox jcb = new JComboBox(TYPESOFPERMITS);
	
	DialogTemplateSearchAndDelete(String str, MyFrame mf) {
		super(str, mf);
		jp.add(FIO);
		jp.add(fio);
		jp.add(NUMBERGRUP);
		jp.add(numberGrup);
		jp.add(KIND);
		
		jcb.setMaximumSize(jcb.getPreferredSize());
		jcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox box = (JComboBox) arg0.getSource();
				try {
					cause = (String)box.getSelectedItem();
				} catch(Exception e) {
					System.err.println("Неверно выбрана болезнь!");
				}
			}
		});
		
		jp.add(jcb);
		
		JPanel jp2 = new JPanel();
		jp.add(SICKNESS);
		jp2.add(sicknessMin);
		jp2.add(sicknessMax);
		jp.add(jp2);
		jp2 = new JPanel();
		jp.add(OTHERCAUSES);
		jp2.add(otherCausesMin);
		jp2.add(otherCausesMax);
		jp.add(jp2);
		jp2 = new JPanel();
		jp.add(WITHOUTGOODREASON);
		jp2.add(withoutGoodReasonMin);
		jp2.add(withoutGoodReasonMax);
		jp.add(jp2);
	}
	
/*	public String getCause() {
		return cause;
	} */

	public JComboBox getCombo() {
		return jcb;
	}
	
	public JTextField getTextFieldSicknessMin() {
		return sicknessMin;
	}
	
	public JTextField getTextFieldSicknessMax() {
		return sicknessMax;
	}
	
	public JTextField getTextFieldOtherCausesMin() {
		return otherCausesMin;
	}
	
	public JTextField getTextFieldOtherCausesMax() {
		return otherCausesMax;
	}
	
	public JTextField getTextFieldwithoutGoodReasonMin() {
		return withoutGoodReasonMin;
	}
	
	public JTextField getTextFieldwithoutGoodReasonMax() {
		return withoutGoodReasonMax;
	}
	
	public boolean search() {					
		if(OperationsAndConstants.isInt(fio.getText()) == -1 && 
				OperationsAndConstants.isInt(numberGrup.getText()) == 1) {
			happening = 1;
			return true;
		} else if(OperationsAndConstants.isInt(fio.getText()) == -1 && 
				OperationsAndConstants.isInt(sicknessMin.getText()) == 1 && 
				OperationsAndConstants.isInt(sicknessMax.getText()) == 1 && 
				OperationsAndConstants.isInt(otherCausesMin.getText()) == 1 && 
				OperationsAndConstants.isInt(otherCausesMax.getText()) == 1 && 
				OperationsAndConstants.isInt(withoutGoodReasonMin.getText()) == 1 &&
				OperationsAndConstants.isInt(withoutGoodReasonMax.getText()) == 1) {
			happening = 2;
			return true;
		} else if(OperationsAndConstants.isInt(fio.getText()) == -1 && !cause.equals("------")) {
			happening = 3;
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Can't search student!\nCheck the entered data.", "ERROR", 
					JOptionPane.ERROR_MESSAGE|JOptionPane.OK_OPTION);
			return false;
		}
	}
	
	void sendServer() {
		client.sendToServer(fio.getText());
		switch(happening) {
		case 1:
			client.sendToServer(new Integer(1));
			client.sendToServer(numberGrup.getText());
			break;
		case 2:
			client.sendToServer(new Integer(2));
			client.sendToServer(sicknessMin.getText());
			client.sendToServer(sicknessMax.getText());
			client.sendToServer(otherCausesMin.getText());
			client.sendToServer(otherCausesMax.getText());
			client.sendToServer(withoutGoodReasonMin.getText());
			client.sendToServer(withoutGoodReasonMax.getText());
			break;
		case 3:
			client.sendToServer(new Integer(3));
		}
	}
}
