package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import library.Student;

public class FileOperations {
	 private static final String FIO = "FIO";
	 private static final String NUMBER_GRUP = "grup";
	 private static final String SICKNESS = "sickness";
	 private static final String OTHER_CAUSES = "otherCauses";
	 private static final String WITHOUTGOODREASON = "withoutGoodReason";
	 private static final String STUDENT = "student";
	 private static final String STUDENTS = "students";
	 private static final String FORMAT = "UTF-8";
	 private static final String VER = "1.0";
	 private StudentsData sd;
	 private Logger log;
	 
	 public FileOperations(StudentsData sd, Logger log) {
		this.sd = sd;
		this.log = log;
	 }
	 
	 public void saveFile(String path) {
		 if (path == null) {
				return ;
		 }
		 try {
			 XMLOutputFactory output = XMLOutputFactory.newInstance();
			 XMLStreamWriter xmlsw = output.createXMLStreamWriter(new FileOutputStream(path), "UTF-8");
		 	 xmlsw.writeStartDocument(FORMAT, VER);
		 	 xmlsw.writeStartElement(STUDENTS);
		 	 for(Student s : sd.allStudents()) {
		 		 xmlsw.writeStartElement(STUDENT);
		 		 xmlsw.writeAttribute(FIO, s.getFIO());
		 		 xmlsw.writeAttribute(NUMBER_GRUP, s.getGrup());
		 		 xmlsw.writeAttribute(SICKNESS, "" + s.getNumbertSickness());
		 		 xmlsw.writeAttribute(OTHER_CAUSES, "" + s.getNumberOtherCauses());
		 		 xmlsw.writeAttribute(WITHOUTGOODREASON, "" + s.getNumberWithoutGoodReason()); 
		 		 xmlsw.writeEndElement();
		 	 }
		 	 xmlsw.writeEndElement();
		 	 xmlsw.writeEndDocument();
		 	 xmlsw.close();
		 } catch(Exception e) {
			 JOptionPane.showMessageDialog
				(null, "Can't save file", "ERROR", JOptionPane.ERROR_MESSAGE|JOptionPane.OK_OPTION);
			 log.log(Level.ERROR, "Can't save file");
		 }
	 }
	 
	 public void openFile(String path) {
		 if(path == null) {
				return ;
		 }
		 if(sd.getSize() > 0) {
			 sd.clear(); 
		 }
		 try {
			 XMLStreamReader xmlsr = XMLInputFactory.newInstance().createXMLStreamReader(path, 
					 new FileInputStream(path));
			 while(xmlsr.hasNext()) {
				 xmlsr.next();
				 if(xmlsr.isStartElement()) {
					 if(xmlsr.getLocalName().equals(STUDENT)) {
						 sd.addStudent(xmlsr.getAttributeValue(null, FIO), 
								 xmlsr.getAttributeValue(null, NUMBER_GRUP), xmlsr.getAttributeValue(null, SICKNESS), 
								 xmlsr.getAttributeValue(null, OTHER_CAUSES), 
								 xmlsr.getAttributeValue(null, WITHOUTGOODREASON));
					 }
				 }
			 }
		 } catch(Exception e) {
			 JOptionPane.showMessageDialog
				(null, "Can't open file", "ERROR", JOptionPane.ERROR_MESSAGE|JOptionPane.OK_OPTION);
			 log.log(Level.ERROR, "Can't open file");
		 }
	 }
}
