package Client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import Library.OperationsAndConstants;
import Library.Student;

public class MyFrame extends JFrame {
	private JPanel jp = new JPanel();
	private Client client;
	private MyTable mtable;		// не нравиться мне это...
	
	private ActionListener open;
	private ActionListener save;
	private ActionListener add;
	private ActionListener search;
	private ActionListener delete;
	
	private AddDialog addD = null;
	private SearchDialog searchD = null;
	private DeleteDialog deleteD = null;
	
	public MyFrame() {
		super("Table for students");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				client.sendToServer(OperationsAndConstants.CLIENT_EXIT);
				event.getWindow().setVisible(false);
                System.exit(0);
			}
		});
		setSize(1100, 700);
		setLocationRelativeTo(null);
		
		readProperties();
		mtable = new MyTable(client, true);
		
		open = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.sendToServer(OperationsAndConstants.OPEN_FILE); 
				if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
					String [] files = (String []) client.receiveFromServer();
					String str = (String) JOptionPane.showInputDialog(null, "Choose file:", "File manager", 
							JOptionPane.PLAIN_MESSAGE, null, files, files[0]);
					if(str != null) {
						client.sendToServer(str);
						mtable.setMaxPage((Integer) client.receiveFromServer());
						client.sendToServer(mtable.getCurrentRecordsNumber());
						mtable.setList((List<Student>) client.receiveFromServer());
					}
				}
			}
		};
		
		save = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.sendToServer(OperationsAndConstants.SAVE_FILE);
				if(client.receiveFromServer().equals(OperationsAndConstants.COMMAND_IS_RECEIVED)) {
					String str = (String) JOptionPane.showInputDialog(null, "Choose file:", "File manager", 
							JOptionPane.PLAIN_MESSAGE, null, null, "test.mtxml");
					client.sendToServer(str);
				}
			}
		};
		
		add = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createAddDialog();
			}
		};
		
		search = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createSearchDialog();
			}
		};
		
		delete = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createDeleteDialog();
			}
		};
		
		
		setJMenuBar(createMenuBar());	
		
		jp.setLayout(new BorderLayout());
		jp.add(createToolBar(), BorderLayout.NORTH);
		jp.add(mtable, BorderLayout.CENTER);
		add(jp);
		this.setVisible(true);
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		Font f = new Font("Helvetica", Font.PLAIN, 14);
		
		JMenu menu = new JMenu("File");
		menu.setFont(f);
		
		JMenuItem menu_item = new JMenuItem("Open");
		setMenuBar(menu, menu_item, f, open);		
		
		menu_item = new JMenuItem("Save");
		setMenuBar(menu, menu_item, f, save);	
		
		menuBar.add(menu);
		
		menu = new JMenu("Edit");
		menu.setFont(f);
		
		menu_item = new JMenuItem("Search person");
		setMenuBar(menu, menu_item, f, search);		
		
		menu_item = new JMenuItem("Add person");
		setMenuBar(menu, menu_item, f, add);		
		
		menu_item = new JMenuItem("Remove person");
		setMenuBar(menu, menu_item, f, delete);		
		
		menuBar.add(menu);
		
		return menuBar;
	}
	
	private JToolBar createToolBar() {
		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);
		jtb.setRollover(true);
		
		jtb.add(OperationsAndConstants.makeButton("open.png", open));
		
		jtb.add(OperationsAndConstants.makeButton("save.png", save));
		
		jtb.addSeparator();
		
		jtb.add(OperationsAndConstants.makeButton("search.png", search)); 	
		
		jtb.add(OperationsAndConstants.makeButton("add.png", add)); 		
		
		jtb.add(OperationsAndConstants.makeButton("remove.png", delete)); 		
		
		return jtb;
	}
	
	private void createAddDialog() {
		if(addD == null) {
			addD = new AddDialog(this);
		}
		else {
			setText(addD.getTextFieldFio(), addD.getTextFieldNumberGrup(), 
					addD.getTextFieldSickness(),addD.getTextFieldOtherCauses(), addD.getTextFieldWithoutGoodReason());
			addD.setVisible(true); 
		}
	}
	
	private void createSearchDialog() {
		if(searchD == null) {
			searchD = new SearchDialog(this);
		}
		else {
			setText(searchD.getTextFieldFio(), searchD.getTextFieldNumberGrup(), 
					searchD.getTextFieldSicknessMin(), searchD.getTextFieldSicknessMax(), 
					searchD.getTextFieldOtherCausesMin(), searchD.getTextFieldOtherCausesMax(), 
					searchD.getTextFieldwithoutGoodReasonMin(), searchD.getTextFieldwithoutGoodReasonMax());
			searchD.getCombo().setSelectedIndex(0); 
			searchD.setListTable(null); 
			searchD.setVisible(true); 
		}
	}
	
	private void createDeleteDialog() {
		if(deleteD == null) {
			deleteD = new DeleteDialog(this);
		}
		else {
			setText(searchD.getTextFieldFio(), searchD.getTextFieldNumberGrup(), 
					searchD.getTextFieldSicknessMin(), searchD.getTextFieldSicknessMax(), 
					searchD.getTextFieldOtherCausesMin(), searchD.getTextFieldOtherCausesMax(), 
					searchD.getTextFieldwithoutGoodReasonMin(), searchD.getTextFieldwithoutGoodReasonMax());
			searchD.getCombo().setSelectedIndex(0);
			searchD.setVisible(true);
		}
	}
	
	private void readProperties() {
		String host = "";
		String port = "";
		File file = 
				new File("d:\\work\\работа\\JAVA\\workspace_PPvIS\\PPvIS_Lab_3\\src\\Client\\HostAndPort.properties");
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
	
	public void sendServerPageAndRecords() {
		mtable.sendCurrentPage();
		mtable.sendCurrentRecords();
	}
	
	public Client getClient() {
		return client;
	} 
	
	public void setMaxPage(int n) {
		mtable.setMaxPage(n); 
	}
	
	public void setTable(List<Student> ls) {
		mtable.setList(ls);
	}
	
	public void setMenuBar(JMenu jmb,JMenuItem jmi, Font f, ActionListener al) {	
		jmi.setFont(f);
		jmi.addActionListener(al); 
		jmb.add(jmi);
	}
	
	public void setText(JTextField ... jte) {
		for(int i = 0; i < jte.length; i++) {
			jte[i].setText(""); 
		}
	}
	
	public static void main(String[] args) {
		new MyFrame();
	}
}
