package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import library.OperationsAndConstants;
import library.Student;

public class DeleteDialog extends DialogTemplateSearchAndDelete {	
	JButton del;
	
	public DeleteDialog(MyFrame frame) {
		super("Delete person", frame);
		this.frame = frame;
		jp.setLayout(new GridLayout(6,2));		
		this.add(jp,BorderLayout.CENTER);
		jp = new JPanel();
		DeleteDialog dd = this;
		del = new JButton("delete");
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(search()) {
					c.chechDelete();
					dd.setVisible(false);
				}
			}
		});
		jp.add(del);
		
		this.add(jp,BorderLayout.SOUTH);
		this.setSize(600, 300);
		this.setLocationRelativeTo(frame);
		this.setVisible(false); 
	}
}
