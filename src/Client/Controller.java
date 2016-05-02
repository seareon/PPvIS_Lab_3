package client;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import library.OperationsAndConstants;
import library.Student;

class Controller {
	private MyFrame mf;
	private AddDialog ad = null;
	private DeleteDialog dd = null;
	private SearchDialog sd = null;
	private MyTable onFrame;
	private MyTable notOnFrame;
	private Client client;
	
	Controller(MyFrame mf) {
		this.mf = mf;
		readProperties();
	}
	
	void open() {
		client.sendToServer(OperationsAndConstants.OPEN_FILE); 
		if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
			String [] files = (String []) client.receiveFromServer();
			String str = (String) JOptionPane.showInputDialog(null, "Choose file:", 
					"File manager", JOptionPane.PLAIN_MESSAGE, null, files, files[0]);
			if(str != null) {
				client.sendToServer(str);
				mf.setMaxPage((Integer) client.receiveFromServer());
				client.sendToServer(mf.getCurrentTableRecordsNumber());
				mf.setTable((List<Student>) client.receiveFromServer());
			}
		}
	} 
	
	void save() {
		client.sendToServer(OperationsAndConstants.SAVE_FILE);
		if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
			String str = (String) JOptionPane.showInputDialog(null, "Choose file:", 
					"File manager",	JOptionPane.PLAIN_MESSAGE, null, null, "test.mtxml");
			client.sendToServer(str);
		}
	} 
	
	void createAddDialog() {
		if(ad == null) {
			ad = new AddDialog(mf);
			ad.setVisible(true);
		}
		else {
			setText(ad.getTextFieldFio(), ad.getTextFieldNumberGrup(), 
					ad.getTextFieldSickness(),ad.getTextFieldOtherCauses(), 
					ad.getTextFieldWithoutGoodReason());
			ad.setVisible(true); 
		}
	}
	
	void createSearchDialog() {
		if(sd == null) {
			sd = new SearchDialog(mf);
			notOnFrame = sd.getTable();
			sd.setVisible(true);
		}
		else {
			setText(sd.getTextFieldFio(), sd.getTextFieldNumberGrup(), 
					sd.getTextFieldSicknessMin(), sd.getTextFieldSicknessMax(), 
					sd.getTextFieldOtherCausesMin(), sd.getTextFieldOtherCausesMax(), 
					sd.getTextFieldwithoutGoodReasonMin(), 
					sd.getTextFieldwithoutGoodReasonMax());
			sd.getCombo().setSelectedIndex(0); 
			sd.setListTable(null); 
			sd.getTable().setCurrentPage(1);
			sd.getTable().setMaxPage(1);
			sd.setVisible(true); 
		}
	}
	
	void createDeleteDialog() {
		if(dd == null) {
			dd = new DeleteDialog(mf);
			dd.setVisible(true);
		}
		else {
			setText(dd.getTextFieldFio(), dd.getTextFieldNumberGrup(), 
					dd.getTextFieldSicknessMin(), dd.getTextFieldSicknessMax(), 
					dd.getTextFieldOtherCausesMin(), dd.getTextFieldOtherCausesMax(), 
					dd.getTextFieldwithoutGoodReasonMin(), 
					dd.getTextFieldwithoutGoodReasonMax());
			dd.getCombo().setSelectedIndex(0);
			dd.setVisible(true);
		}
	}
	
	public void chechAdd() {
		if(OperationsAndConstants.isInt(ad.getFio()) == -1 && 
				OperationsAndConstants.isInt(ad.getNumberGrup()) == 1 && 
				OperationsAndConstants.isInt(ad.getTextFieldSickness().getText()) == 1 && 
				OperationsAndConstants.isInt(ad.getTextFieldOtherCauses().getText()) == 1 && 
				OperationsAndConstants.isInt(ad.getTextFieldWithoutGoodReason().getText()) == 1) {
			client.sendToServer(OperationsAndConstants.ADD);
			if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
				mf.sendServerPageAndRecords();		
				client.sendToServer(new Student(ad.getFio(), ad.getNumberGrup(), 
						Integer.parseInt(ad.getTextFieldSickness().getText()), 
						Integer.parseInt(ad.getTextFieldOtherCauses().getText()),
						Integer.parseInt(ad.getTextFieldWithoutGoodReason().getText())));
				if(client.receiveFromServer().equals(OperationsAndConstants.CHANGE_TABLE)) {
					onFrame.setMaxPage((Integer) client.receiveFromServer()); 
					onFrame.setList((List<Student>) client.receiveFromServer()); 
				}
			}
		}
		ad.setVisible(false);
	}
	
	public void checkSearech() {
		client.sendToServer(OperationsAndConstants.SEARCH);
		sendServerTemplate(sd);
		if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
			List<Student> ls = (List<Student>) client.receiveFromServer();
			sd.getTable().setMaxPage(ls.size());
			sd.getTable().setList(ls);
		}
	}
	
	public void chechDelete() {
		client.sendToServer(OperationsAndConstants.REMOVE);
		mf.sendServerPageAndRecords();
		sendServerTemplate(dd);
		if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED) && 
				client.receiveFromServer().equals(OperationsAndConstants.CHANGE_TABLE)) {
			JOptionPane.showMessageDialog(null, "Remove" + (Integer) client.receiveFromServer() + 
					" items.");
			mf.setMaxPage((Integer) client.receiveFromServer());
			mf.setTable((List<Student>) client.receiveFromServer());
		} 
		else {
			JOptionPane.showMessageDialog(null, "Remove" + 0 + " items.");
		}
	}
	
	void sendServerTemplate(DialogTemplateSearchAndDelete dtsad) {
		client.sendToServer(dtsad.getFio());
		switch(dtsad.getOption()) {
		case 1:
			client.sendToServer(new Integer(1));
			client.sendToServer(dtsad.getNumberGrup());
			break;
		case 2:
			client.sendToServer(new Integer(2));
			client.sendToServer(dtsad.sicknessMin.getText());
			client.sendToServer(dtsad.sicknessMax.getText());
			client.sendToServer(dtsad.otherCausesMin.getText());
			client.sendToServer(dtsad.otherCausesMax.getText());
			client.sendToServer(dtsad.withoutGoodReasonMin.getText());
			client.sendToServer(dtsad.withoutGoodReasonMax.getText());
			break;
		case 3:
			client.sendToServer(new Integer(3));
		}
	}
	
	public void clientExit(WindowEvent event) {
		client.sendToServer(OperationsAndConstants.CLIENT_EXIT);
		event.getWindow().setVisible(false);
        System.exit(0);
	}
	
	private void setText(JTextField ... jte) {
		for(int i = 0; i < jte.length; i++) {
			jte[i].setText(""); 
		}
	}
	
	public Client getClient() {
		return client;
	}
	
	private void readProperties() {
		String host = "";
		String port = "";
		File file = 
				new File("HostAndPort.properties");
		if (!file.exists()){
			JOptionPane.showMessageDialog
            (null, "File HostAndPort.properties not founded.", "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		try {
			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsoluteFile()));
			for(int indexFile = 0; indexFile < 2; indexFile++) {
				if(indexFile == 0) {
					host = bf.readLine();
				}
				else {
					port = bf.readLine();
				}
			}
		}catch(Exception e) {
			JOptionPane.showMessageDialog
            (null, "Ups!The problem with reading the file HostAndPort.properties.", "ERROR", 
            		JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		client = new Client(host, Integer.parseInt(port));
	}
	
	public void arrowLeft(boolean b) {
		client.sendToServer(OperationsAndConstants.FIRST_PAGE);
		sendToServerAndReceiptTable(b);
	}
	
	public void previous(boolean b) {
		client.sendToServer(OperationsAndConstants.PREV_PAGE);
		sendToServerAndReceiptTable(b);
	}
	
	public void next(boolean b) {
		client.sendToServer(OperationsAndConstants.NEXT_PAGE);
		sendToServerAndReceiptTable(b);
	}
	
	public void arrowRight(boolean b) {
		client.sendToServer(OperationsAndConstants.LAST_PAGE);
		sendToServerAndReceiptTable(b);
	}
	
	public void changePageNumberRecords(boolean b) {
		client.sendToServer(
				OperationsAndConstants.CHANGE_CURRENT_PAGE_AND_NUMBER_RECORDS);
		sendToServerAndReceiptTable(b);
	}
	
	public void sendCurrentPage(boolean b) {
		if(onFrame == null) {
			onFrame = mf.getTable();
		}
		if(b) {
			client.sendToServer(new Integer(onFrame.getCurrentPageNumber()));
		}
		else {
			client.sendToServer(new Integer(notOnFrame.getCurrentPageNumber()));
		}
	}
	
	public void sendCurrentRecords(boolean b) {
		if(onFrame == null) {
			onFrame = mf.getTable();
		}
		if(b) {
			client.sendToServer(new Integer(onFrame.getCurrentRecordsNumber()));
		}
		else {
			client.sendToServer(new Integer(notOnFrame.getCurrentRecordsNumber()));
		}
	} 
	
	private void sendToServerAndReceiptTable(boolean b) { 
		if(onFrame == null) {
			onFrame = mf.getTable();
		}
		if(b) {
			client.sendToServer(new Integer(onFrame.getCurrentPageNumber()));
			client.sendToServer(new Integer(onFrame.getCurrentRecordsNumber()));
			client.sendToServer(OperationsAndConstants.NOT_SEARCH_TABLE);
		} else {
			client.sendToServer(new Integer(notOnFrame.getCurrentPageNumber()));
			client.sendToServer(new Integer(notOnFrame.getCurrentRecordsNumber()));
			client.sendToServer(OperationsAndConstants.SEARCH_TABLE);
		}
		if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
			if(b) {
				onFrame.setList((List<Student>)client.receiveFromServer());
			}
			else {
				notOnFrame.setList((List<Student>)client.receiveFromServer());
			}
		}
	}	
}
