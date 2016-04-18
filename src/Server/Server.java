package Server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTextArea;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import Library.OperationsAndConstants;
import Library.Student;

public class Server extends Thread {
	final int PORT = 5016;	
	private ObjectInputStream inS;
	private ObjectOutputStream outS;
	private ServerSocket ss;
	private StudentsData sd;
	private boolean suspendFlag = false;
	private JTextArea jta;
	private List<Student> searchList;
	private FileOperations fo; 
	private Logger log;
	
	public Server(JTextArea jta, Logger log) {
		this.jta = jta;
		this.log = log;
		try {
			ss = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			jta.append("Error creating server\n");
			log.log(Level.ERROR, "Error creating server");
			System.exit(1); 
		}
	}
	
	public void run() {
		while(true) {
			createSocket();
			boolean clientIsConnected = true;
			sd = new StudentsData();
			fo = new FileOperations(sd, log);
			try {
				while(clientIsConnected) {
					String str = (String) inS.readObject();
					synchronized(this) {
						while(suspendFlag) {
							wait();
						}
					}
					switch(str) {
						case OperationsAndConstants.CLIENT_EXIT:
							clientIsConnected = false;
							break;
						case OperationsAndConstants.ADD:
							add();
							break;
						case OperationsAndConstants.SEARCH:
							search();
							break;
						case OperationsAndConstants.REMOVE:
							remove();
							break;
						case OperationsAndConstants.OPEN_FILE:
							open();
							break;
						case OperationsAndConstants.SAVE_FILE:
							save();
							break;
						case OperationsAndConstants.FIRST_PAGE:
						case OperationsAndConstants.LAST_PAGE:
						case OperationsAndConstants.NEXT_PAGE:
						case OperationsAndConstants.PREV_PAGE:
						case OperationsAndConstants.CHANGE_CURRENT_PAGE_AND_NUMBER_RECORDS:
							pressToolBar();
					}
				}
			} catch (InterruptedException e) {		// сделать один??
//				jta.append("Error with streams\n");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
//				jta.append("Error with streams\n");
				e.printStackTrace();
			} catch (IOException e) {
				jta.append("Error with streams\n");		
				log.log(Level.ERROR, "Error with streams");
//				e.printStackTrace();
			}
		}
	}
	
	private void createSocket() {
		try {
			Socket socket = ss.accept();
			jta.append("Add socket\n");
			outS = new ObjectOutputStream(socket.getOutputStream());
			outS.flush();
			inS = new ObjectInputStream(socket.getInputStream());
			jta.append("Add streams\n");
		} catch(Exception e) {
			jta.append("Error creating server\n");
			log.log(Level.ERROR, "Error creating server");
			e.printStackTrace();
			System.exit(1); 
		}
	}
	
	private void add() throws ClassNotFoundException, IOException {
		outS.writeObject(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int currentPage = (Integer) inS.readObject();
		int currentRecords = (Integer) inS.readObject(); 
		Student st = (Student) inS.readObject();
		sd.addStudent(st);
		if(checkTable(st, currentPage, currentRecords)) {
			outS.writeObject(OperationsAndConstants.CHANGE_TABLE);
			outS.writeObject((Integer)sd.getSize());
			outS.writeObject(arrayStudent(sd.allStudents(), currentPage, currentRecords));
		} 
		else {
			outS.writeObject(OperationsAndConstants.NOT_CHANGE_TABLE);
		}
		outS.flush();
	}
	
	private void search() throws IOException, ClassNotFoundException { 
		outS.writeObject(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int n = searchStudents();
		if(n > 0) {
			outS.writeObject(searchList);
		} else {
			outS.writeObject(null);
		}
	}
	
	private void remove() throws IOException, ClassNotFoundException {
		outS.writeObject(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int currentPage = (Integer) inS.readObject();
		int currentRecords = (Integer) inS.readObject();
		int n = searchStudents();
		if(n > 0) {
			boolean b = checkTable(searchList.get(0), currentPage, currentRecords);
			sd.removeAll(searchList);
			if(b) {
				outS.writeObject(OperationsAndConstants.CHANGE_TABLE);
				outS.writeObject(new Integer(n));
				outS.writeObject((Integer)sd.getSize());
				outS.writeObject(arrayStudent(sd.allStudents(), currentPage, currentRecords));
			} else {
				outS.writeObject(OperationsAndConstants.NOT_CHANGE_TABLE);
			}
			outS.flush();
		}
	}
	
	private void pressToolBar() throws ClassNotFoundException, IOException {
		outS.writeObject(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int currentPage = (Integer) inS.readObject();
		int currentRecords = (Integer) inS.readObject();
		List<Student> ls;
		if(inS.readObject().equals(OperationsAndConstants.NOT_SEARCH_TABLE)) {
			ls = arrayStudent(sd.allStudents(), currentPage, currentRecords);
			
		} else {
			ls = arrayStudent(searchList, currentPage, currentRecords);
		}
		outS.writeObject(ls);
		outS.flush();
	}
	
	private void open() throws IOException, ClassNotFoundException { 
		outS.writeObject(OperationsAndConstants.COMMAND_IS_RECEIVED);
		String str = "d:\\work\\работа\\JAVA\\workspace_PPvIS\\PPvIS_Lab_3\\serverLib";
		File myFolder = new File(str);
		outS.writeObject(myFolder.list());
		str += "\\" + (String) inS.readObject();
		fo.openFile(str);
		outS.writeObject((Integer) sd.allStudents().size());
		outS.writeObject(arrayStudent(sd.allStudents(), 1, (Integer) inS.readObject()));
		outS.flush();
	}
	
	private void save() throws IOException, ClassNotFoundException {
		outS.writeObject(OperationsAndConstants.COMMAND_IS_RECEIVED);
		String str = (String) inS.readObject();
		if(str != null) {
			fo.saveFile("d:\\work\\работа\\JAVA\\workspace_PPvIS\\PPvIS_Lab_3\\serverLib\\" + str); 
		}
		outS.flush();
	}
	
	private int searchStudents() throws ClassNotFoundException, IOException {
		List<Student> temp = new ArrayList<>();
		String str = (String) inS.readObject();
		for(Student s : sd.allStudents()) {
			if(s.getFIO().equals(str)) {
				temp.add(s);
			}
		}

		switch((Integer) inS.readObject()) {
			case 1:
				str = (String) inS.readObject();
				for(Iterator<Student> itr = sd.allStudents().iterator(); itr.hasNext(); ) {
					if(!itr.next().getGrup().equals(str)) {
						itr.remove();
					}
				}
				break;
			case 2:
				int [] num = new int[6];
				for(int indexNum = 0; indexNum < 6; indexNum++) {
					num[indexNum] = (Integer) inS.readObject();
				}
				for(Iterator<Student> itr = sd.allStudents().iterator(); itr.hasNext(); ) {
					if(itr.next().getNumbertSickness() < num[0] || itr.next().getNumbertSickness() > num[1] ||
							itr.next().getNumberOtherCauses() < num[2] || itr.next().getNumberOtherCauses() > num[3] ||
							itr.next().getNumberWithoutGoodReason() < num[4] ||
							itr.next().getNumberWithoutGoodReason() > num[5]) {
						itr.remove();
					}
				}
				break;
			case 3:
				for(Iterator<Student> itr = sd.allStudents().iterator(); itr.hasNext(); ) {
					if(itr.next().getNumberOtherCauses() == 0 && itr.next().getNumbertSickness() == 0 &&
							itr.next().getNumberWithoutGoodReason() == 0) {
						itr.remove();
					}
				}
		}
		searchList = temp;
		return temp.size();
	}
	
	private boolean checkTable(Student st, int currentPage, int currentRecords) {
		for(int indexStudents = (currentPage - 1)* currentRecords; indexStudents < sd.getSize(); indexStudents++) {
			if(sd.allStudents().get(indexStudents).equals(st)) {
				return true;
			}
		}
		return false;
	}
	
	private List<Student> arrayStudent(List<Student> ls, int currentPage, int currentRecords) {
		List<Student> temp = new ArrayList<Student>();
		for(int indexStudents = (currentPage - 1)* currentRecords; indexStudents < currentPage * currentRecords &&
				indexStudents < ls.size(); indexStudents++) {
			temp.add(ls.get(indexStudents));
		}
		return temp;
	}
	
	synchronized public void myresume() {
		suspendFlag = false;
		notify();
	}
	
	synchronized public void mysuspend() {
		suspendFlag = true;
	}
}
