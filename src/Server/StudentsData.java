package Server;

import java.util.ArrayList;
import java.util.List;

import Library.Student;

public class StudentsData {
	private List<Student> list;
	
	public StudentsData() {
		list = new ArrayList<>();
	}
	
	public void addStudent(String fio, String numberGrup, int sickness, int otherCauses, int withoutGoodReason) {
		list.add(new Student(fio, numberGrup, sickness, otherCauses, withoutGoodReason));
	}		
	
	public void addStudent(String fio, String numberGrup, String sickness, String otherCauses, 	
			String withoutGoodReason) {
		list.add(new Student(fio, numberGrup, Integer.parseInt(sickness), Integer.parseInt(otherCauses),
				Integer.parseInt(withoutGoodReason)));
	} 
	
	public void addStudent(Student st) {
		list.add(st);
	}
	
	public Student getStudent(int n) {
		if(n >= 0 && n < list.size()) {
			return list.get(n);
		}
		return null;
	}
	
	public List<Student> allStudents() {
		return list;
	}
	
	public int getSize() {
		return list.size();
	}
	
	public void clear() {
		list.clear();
	}
	
	public void removeAll(List<Student> ls) {
		list.removeAll(ls);
	}
}
