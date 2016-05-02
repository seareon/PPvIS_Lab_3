package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class Client {
	private String host;
	private int port;
	private Socket socket = null;
	
	private ObjectInputStream inS;
	private ObjectOutputStream outS;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		createSocket();
		try {
			inS = new ObjectInputStream(socket.getInputStream());
			outS = new ObjectOutputStream(socket.getOutputStream());
		}catch(IOException e) {
			JOptionPane.showMessageDialog
            (null, "Not can install connection", "ERROR", JOptionPane.ERROR_MESSAGE);
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
	
	private void createSocket() {
		try {
			 socket = new Socket(host, port);
			 JOptionPane.showMessageDialog
			 (null, "You connect to " + host + ":" + port, "INFO", JOptionPane.INFORMATION_MESSAGE);
		}catch(UnknownHostException e) {
            JOptionPane.showMessageDialog
            (null, "Unknown host " + host, "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
		} catch (IOException e) {
			JOptionPane.showMessageDialog
            (null, "I/O Error creating socket " + host + ":" + port, "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(1); 
}
	}
}
