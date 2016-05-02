package client;

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
import library.OperationsAndConstants;
import library.Student;

public class MyFrame extends JFrame {
	private JPanel jp = new JPanel();
	private Controller c;
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
				c.clientExit(event); 
			}
		});
		setSize(1100, 700);
		setLocationRelativeTo(null);
		
		c = new Controller(this);
		mtable = new MyTable(c, true);
		
		
		open = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.open();
			}
		};
		
		save = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.save();
			}
		};
		
		add = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.createAddDialog();
			}
		};
		
		search = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.createSearchDialog();
			}
		};
		
		delete = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.createDeleteDialog();
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
	
	public void sendServerPageAndRecords() {
		mtable.sendCurrentPage();
		mtable.sendCurrentRecords();
	}
	
	public Controller getController() {
		return c;
	}
	
	public void setMaxPage(int n) {
		mtable.setMaxPage(n); 
	}
	
	public int getCurrentTableRecordsNumber() {
		return mtable.getCurrentRecordsNumber();
	}
	
	public void setTable(List<Student> ls) {
		mtable.setList(ls);
	}
	
	public MyTable getTable() {
		return mtable;
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
