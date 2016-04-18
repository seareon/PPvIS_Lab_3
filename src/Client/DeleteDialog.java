package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Library.OperationsAndConstants;
import Library.Student;

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
					client.sendToServer(OperationsAndConstants.REMOVE);
					frame.sendServerPageAndRecords();
					sendServer();
					if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED) && 
							client.receiveFromServer().equals(OperationsAndConstants.CHANGE_TABLE)) {
						JOptionPane.showMessageDialog(null, "Remove" + (Integer) client.receiveFromServer() + 
								" items.");
						frame.setMaxPage((Integer) client.receiveFromServer());
						frame.setTable((List<Student>) client.receiveFromServer());
					} 
				}
				dd.setVisible(false); 
			}
		});
		jp.add(del);
		
		this.add(jp,BorderLayout.SOUTH);
		this.setSize(600, 300);
		this.setLocationRelativeTo(frame);
		this.setVisible(true); 
	}
}
