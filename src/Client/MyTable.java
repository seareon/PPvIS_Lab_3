package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import library.OperationsAndConstants;
import library.Student;

public class MyTable extends JPanel {
	private String table;
	private JLabel veiwTable = new JLabel();
	private List<Student> l = null;
//	private Client client;
	private Controller c;
	private boolean isFrame;		
	
	private int currentPageNumber = 1;
	private int maxPageNumber;
	private int currentRecordsNumber = 10;
	private int currentSizeRecords = 1;
	
	private JLabel allPage = new JLabel();
	
	private JTextField currentRecordsOnPage = new JTextField(3);
	private JTextField currentPage = new JTextField(3);
	
	private KeyListener kl;
	
	public MyTable(Controller c/*Client cl*/, boolean b) {
		super();
//		client = cl;
		this.c = c; 
		isFrame = b;
		this.setLayout(new BorderLayout());
		newTable();
		JPanel jp = new JPanel();
		jp.add(veiwTable);
		JScrollPane jsp = new JScrollPane(jp);
		add(jsp, BorderLayout.CENTER);
		kl = new MyKeyListener();
		add(createToolBar(), BorderLayout.SOUTH);
	}
	
	public void newTable() {
		createHead();
		if(l != null && l.size() != 0) {
			for(int indexTable = 0; indexTable < l.size(); indexTable++) {
					table += "<tr><td>" + l.get(indexTable).getFIO() + "</td><td>" + l.get(indexTable).getGrup() + 
					"</td><td>" + l.get(indexTable).getNumbertSickness() + "</td>" + "<td>" + 
					l.get(indexTable).getNumberOtherCauses() + "</td><td>" + 
					l.get(indexTable).getNumberWithoutGoodReason() + "</td>" + "<td>" + 
					(l.get(indexTable).getNumbertSickness() + l.get(indexTable).getNumberOtherCauses() 
							+ l.get(indexTable).getNumberWithoutGoodReason()) + "</td></tr>";
			}
		}
		table += "</table></html>";
		veiwTable.setText(table);
	}
	
	private void createHead() {
		table = "<html><table border=1 cellpadding=10 width=744px>" ;
		table += "<tr>";
		table += "<td rowspan=2 ALIGN=center width=75%><font face=ТverdanaТ size = 5>‘»ќ студента</td>" 
		+ "<td rowspan=2 ALIGN=center><font face=ТverdanaТ size = 5>√руппа</td>" 
		+ "<td colspan=4 ALIGN=center><font face=ТverdanaТ size = 5>„исло пропусков зан€тий за год</td></tr>";
		table += "<tr>";
		table += "<td ALIGN=center><font face=ТverdanaТ size = 5>ѕо болезни</td>" 
		+ "<td ALIGN=center><font face=ТverdanaТ size = 5>ѕо другим причинам</td>" 
		+ "<td ALIGN=center><font face=ТverdanaТ size = 5>Ѕез уважительной причины</td>" 
		+ "<td ALIGN=center><font face=ТverdanaТ size = 5>»того</td></tr>";
	}
	
	public void setList(List<Student> newL) {
		l = newL;
		newTable();
	}
	
	private JToolBar createToolBar() {
		JToolBar jtb = new JToolBar();
		
		JLabel page = new JLabel("Page: "); 
		JLabel records  = new JLabel("The number of records per page: ");
		
		Font f = new Font("Helvetica", Font.PLAIN, 14);
		
		jtb.setFloatable(false);
		jtb.setLayout(new FlowLayout());
		page.setFont(f); 
		jtb.add(page);
		currentPage.setFont(f);
		setMaxPageNumber();
		currentPage.setText("1");
		currentPage.addKeyListener(kl);
		jtb.add(currentPage);
		allPage.setFont(f);
		jtb.add(allPage);
		
		jtb.addSeparator();
		jtb.addSeparator();
		jtb.addSeparator();
		
		jtb.add(OperationsAndConstants.makeButton("arrow-left.png", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(l != null) {
					setCurrentPage(1);
					currentPage.setText("1"); 
/*				client.sendToServer(OperationsAndConstants.FIRST_PAGE);
				sendToServerAndReceiptTable(); */
					c.arrowLeft(isFrame);
				}
			}
		}));
		jtb.add(OperationsAndConstants.makeButton("previous.png", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentPageNumber != 1 ) {
					setCurrentPage(currentPageNumber - 1);
					currentPage.setText("" + currentPageNumber);
/*					client.sendToServer(OperationsAndConstants.PREV_PAGE);
					sendToServerAndReceiptTable();*/
				c.previous(isFrame);					
				} 
			}
		}));
		jtb.add(OperationsAndConstants.makeButton("next.png", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentPageNumber != maxPageNumber) {
					setCurrentPage(currentPageNumber + 1);
					currentPage.setText("" + currentPageNumber);
/*					client.sendToServer(OperationsAndConstants.NEXT_PAGE);
					sendToServerAndReceiptTable();*/
					c.next(isFrame);					
				} 
			}
		}));
		jtb.add(OperationsAndConstants.makeButton("arrow-right.png", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(l != null) {
					setCurrentPage(maxPageNumber);
					currentPage.setText("" + maxPageNumber); 
/*				client.sendToServer(OperationsAndConstants.LAST_PAGE);
				sendToServerAndReceiptTable();*/
					c.arrowRight(isFrame);
				}
			}
		}));
		
		jtb.addSeparator();
		jtb.addSeparator();
		jtb.addSeparator();
		
		records.setFont(f);
		jtb.add(records);
		currentRecordsOnPage.setFont(f);
		currentRecordsOnPage.setText("" + currentRecordsNumber); 
		jtb.add(currentRecordsOnPage);
		currentRecordsOnPage.addKeyListener(kl);
		return jtb;
	}
	
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	
/*	private void sendToServerAndReceiptTable() { 
		client.sendToServer(new Integer(currentPageNumber));
		client.sendToServer(new Integer(currentRecordsNumber));
		if(isFrame) {
			client.sendToServer(OperationsAndConstants.NOT_SEARCH_TABLE);
		} else {
			client.sendToServer(OperationsAndConstants.SEARCH_TABLE);
		}
		if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
			setList((List<Student>)client.receiveFromServer());
		}
	} */
	
	public boolean setCurrentPage(int n) {
		if(n > 0 && maxPageNumber >= n) {
			currentPageNumber = n;
			return true;
		}
		return false;
	}
	
	public void setAllPage(int n) {
		allPage.setText("/ " + n); 
	}
	
	public void setMaxPage(int n) {		
		currentSizeRecords = n;
		setMaxPageNumber();
	}
	
	private void setMaxPageNumber() {
		maxPageNumber = currentSizeRecords / currentRecordsNumber;
		if(currentSizeRecords % currentRecordsNumber > 0) {
			maxPageNumber++;
		}
		setAllPage(maxPageNumber);
		if(maxPageNumber < currentPageNumber) {
			currentPageNumber = maxPageNumber;
		}
	}
	
	public int getCurrentRecordsNumber() {
		return currentRecordsNumber;
	}
	
	public void sendCurrentPage() {
		c.sendCurrentPage(isFrame); 
	}
	
	public void sendCurrentRecords() {
		c.sendCurrentRecords(isFrame);
	} 
	
	class MyKeyListener implements KeyListener {			
		public void keyPressed(KeyEvent arg0) {

			if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			boolean p = false;
			boolean r = false; 
				if(OperationsAndConstants.isInt(currentRecordsOnPage.getText()) == 1 && 
					Integer.parseInt(currentRecordsOnPage.getText()) > 0 &&
					currentRecordsNumber != Integer.parseInt(currentRecordsOnPage.getText())) {
						currentRecordsNumber = Integer.parseInt(currentRecordsOnPage.getText());
						setMaxPageNumber();
						r = true;
				}
				else {
					currentRecordsOnPage.setText("" + currentRecordsNumber);
				}
				if(OperationsAndConstants.isInt(currentPage.getText()) == 1 &&
						setCurrentPage(Integer.parseInt(currentPage.getText()))) {
					p = true;
				}
				else {
					currentPage.setText("" + currentPageNumber);
				}
				if((p || r) && l != null) {
/*					client.sendToServer(
							OperationsAndConstants.CHANGE_CURRENT_PAGE_AND_NUMBER_RECORDS);
					sendToServerAndReceiptTable();*/
					c.changePageNumberRecords(isFrame);
				} 
			}
		}

		public void keyReleased(KeyEvent arg0) {
		}
		
		public void keyTyped(KeyEvent arg0) {
		}
	};
}
