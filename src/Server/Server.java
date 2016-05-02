package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Server extends Thread {
	final int PORT = 5016;	
	private ObjectInputStream inS;
	private ObjectOutputStream outS;
	private ServerSocket ss;
	private StudentsData sd;
	private boolean suspendFlag = false;
	private JTextArea jta;
	private FileOperations fo; 
	private Controller c;
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
	
	public void sendToServer(Object obj) {
		try {
			outS.writeObject(obj);
			outS.flush();
		}catch(IOException e) {
	            JOptionPane.showMessageDialog
	                    (null, "Not send date", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Object receiveFromServer()  {
		Object ls = null;
		try{
			ls = inS.readObject();
		}catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Not read date", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return ls;
	}
	
	public void run() {
		while(true) {
			createSocket();
			boolean clientIsConnected = true;
			sd = new StudentsData();
			fo = new FileOperations(sd, log);
			c = new Controller(this, fo, sd);
			try {
				while(clientIsConnected) {
					String str = (String) inS.readObject();
					synchronized(this) {
						while(suspendFlag) {
							wait();
						}
					}
					clientIsConnected = c.checkMethod(str);
				}
			} catch (Exception e) {
				jta.append("Error with streams\n");		
				log.log(Level.ERROR, "Error with streams");
				System.exit(1); 
			}
		}
	}
	
	private void createSocket() {
		log.info("Run the server");
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
	
	synchronized public void myresume() {
		suspendFlag = false;
		notify();
	}
	
	synchronized public void mysuspend() {
		log.info("Stop the server");
		suspendFlag = true;
	}
}
