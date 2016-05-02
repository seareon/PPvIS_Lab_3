package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import library.OperationsAndConstants;
import library.Student;

class Controller {
	private StudentsData sd;
	private List<Student> searchList;
	private FileOperations fo; 
	private Server s;
	
	Controller(Server s, FileOperations fo, StudentsData sd) {
		this.s = s;
		this.fo = fo;
		this.sd = sd;
	}
	
	public boolean checkMethod(String str) throws ClassNotFoundException, IOException { 
		switch(str) {
		case OperationsAndConstants.CLIENT_EXIT:
			return false;
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
		default:
			pressToolBar();
		}
		return true;
	}
		
	private void add() throws ClassNotFoundException, IOException {
		s.sendToServer(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int currentPage = (Integer) s.receiveFromServer();
		int currentRecords = (Integer) s.receiveFromServer(); 
		Student st = (Student) s.receiveFromServer();
		sd.addStudent(st);
		if(checkTable(st, currentPage, currentRecords)) {
			s.sendToServer(OperationsAndConstants.CHANGE_TABLE);
			s.sendToServer((Integer)sd.getSize());
			s.sendToServer(arrayStudent(sd.allStudents(), currentPage, currentRecords));
		} 
		else {
			s.sendToServer(OperationsAndConstants.NOT_CHANGE_TABLE);
		}
	}
	
	private void search() throws IOException, ClassNotFoundException { 
		s.sendToServer(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int n = searchStudents();
		if(n > 0) {
			s.sendToServer(searchList);
		} else {
			s.sendToServer(null);
		}
	}
	
	private void remove() throws IOException, ClassNotFoundException {
		s.sendToServer(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int currentPage = (Integer) s.receiveFromServer();
		int currentRecords = (Integer) s.receiveFromServer();
		int n = searchStudents();
		if(n > 0) {
			boolean b = checkTable(searchList.get(0), currentPage, currentRecords);
			sd.removeAll(searchList);
			if(b) {
				s.sendToServer(OperationsAndConstants.CHANGE_TABLE);
				s.sendToServer(new Integer(n));
				s.sendToServer((Integer)sd.getSize());
				s.sendToServer(arrayStudent(sd.allStudents(), currentPage, currentRecords));
			} else {
				s.sendToServer(OperationsAndConstants.NOT_CHANGE_TABLE);
			}
			return ;
		}
		s.sendToServer(OperationsAndConstants.NOT_CHANGE_TABLE);
	}
	
	private void pressToolBar() throws ClassNotFoundException, IOException {
		s.sendToServer(OperationsAndConstants.COMMAND_IS_RECEIVED);
		int currentPage = (Integer) s.receiveFromServer();
		int currentRecords = (Integer) s.receiveFromServer();
		List<Student> ls;
		if(s.receiveFromServer().equals(OperationsAndConstants.NOT_SEARCH_TABLE)) {
			ls = arrayStudent(sd.allStudents(), currentPage, currentRecords);
			
		} else {
			ls = arrayStudent(searchList, currentPage, currentRecords);
		}
		s.sendToServer(ls);
	}
	
	private void open() throws IOException, ClassNotFoundException { 
		s.sendToServer(OperationsAndConstants.COMMAND_IS_RECEIVED);
		String str = "serverLib";
		File myFolder = new File(str);
		s.sendToServer(myFolder.list());
		str += "\\" + (String) s.receiveFromServer();
		fo.openFile(str);
		s.sendToServer((Integer) sd.allStudents().size());
		s.sendToServer(arrayStudent(sd.allStudents(), 1, (Integer) s.receiveFromServer()));
	}
	
	private void save() throws IOException, ClassNotFoundException {
		s.sendToServer(OperationsAndConstants.COMMAND_IS_RECEIVED);
		String str = (String) s.receiveFromServer();
		if(str != null) {
			fo.saveFile("serverLib\\" + str); 
		}
	}
	
	private int searchStudents() throws ClassNotFoundException, IOException {
		List<Student> temp = new ArrayList<>();
		String str = (String) s.receiveFromServer();
		for(Student s : sd.allStudents()) {
			if(s.getFIO().equals(str)) {
				temp.add(s);
			}
		}

		switch((Integer) s.receiveFromServer()) {
			case 1:
				str = (String) s.receiveFromServer();
				for(Iterator<Student> itr = sd.allStudents().iterator(); itr.hasNext(); ) {
					if(!itr.next().getGrup().equals(str)) {
						itr.remove();
					}
				}
				break;
			case 2:
				int [] num = new int[6];
				for(int indexNum = 0; indexNum < 6; indexNum++) {
					num[indexNum] = (Integer) s.receiveFromServer();
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
}
