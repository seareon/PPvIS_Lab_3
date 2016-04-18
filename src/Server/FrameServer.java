package Server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.log4j.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FrameServer extends JFrame {
	private JTextArea jta = new JTextArea();
	private Server s = null; 
	private static final Logger log = Logger.getLogger(FrameServer.class);
	
	FrameServer() {
		super("Server");
		
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		jta.setEditable(false); 
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		JPanel jp1 = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		
		JButton run = new JButton("Run the server");
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jta.append("Run the server\n");
				if(s == null) {
					s = new Server(jta, log);
					s.start();
				}
				else {
					s.myresume();
				}
			}
		}); 
		JButton stop = new JButton("Stop the server");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jta.append("Stop the server\n");
				s.mysuspend();
			}
		});
		
		jp1.add(run);
		jp2.add(stop);
		jp3.setLayout(new GridLayout(1,2));
		jp3.add(jp1);
		jp3.add(jp2);
		jp.add(jp3, BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(jta);
		jp.add(jsp, BorderLayout.CENTER);
		add(jp);
		setVisible(true);
	}
	
	static public void main(String args[]) {
		new FrameServer();
	}
}
