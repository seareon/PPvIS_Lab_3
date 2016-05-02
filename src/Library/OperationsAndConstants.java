package library;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class OperationsAndConstants {
	public static final String OPEN_FILE = "OPEN_FILE";
	public static final String SAVE_FILE = "SAVE_FILE";
	public static final String SEARCH = "SEARCH";
	public static final String ADD = "ADD";
	public static final String REMOVE = "REMOVE";
	public static final String CHANGE_CURRENT_PAGE_AND_NUMBER_RECORDS = "CHENGE_CURRENT_PAGE_AND_NUMBER_RECORDS";
	public static final String FIRST_PAGE = "FIRST_PAGE";
	public static final String LAST_PAGE = "LAST_PAGE";
	public static final String PREV_PAGE = "PREV_PAGE";
	public static final String NEXT_PAGE = "NEXT_PAGE";
	public static final String CHANGE_TABLE = "CHANGE_TABLE";
	public static final String NOT_CHANGE_TABLE = "NOT_CHANGE_TABLE";
/*	public static final String FIO = "FIO";
	public static final String NUMBER_GRUP = "NUMBER_GRUP";
	public static final String SICKNESS = "SICKNESS";
	public static final String OTHER_CAUSES = "OTHER_CAUSES";
	public static final String WITHOUT_GOOD_REASON = "WITHOUT_GOOD_REASON";
	public static final String CAUSE = "CAUSE";	*/
	public static final String CLIENT_EXIT = "CLIENT_EXIT";
	public static final String COMMAND_IS_RECEIVED = "COMMAND_IS_RECEIVED";
	public static final String SEARCH_TABLE = "SEARCH_TABLE";
	public static final String NOT_SEARCH_TABLE = "NOT_SEARCH_TABLE";
	
	static public JButton makeButton(String str, ActionListener al) {		
    	JButton but = new JButton();
    	String imgLocation = "image\\" + str;
    	ImageIcon img = new ImageIcon(imgLocation);
    	but.setIcon(img);
	    but.addActionListener(al); 
	    return but;
    }
	
	static public int isInt(String str) {
		if(str == null || str.length() == 0) {
			return 0;
		}
		boolean myNamber = false;
		boolean myChar = false;
		for(int indexChar = 0; indexChar < str.length(); indexChar++) {
			if(Character.isDigit(str.charAt(indexChar))) {
				myNamber= true;
			}
			else if(Character.isLetter(str.charAt(indexChar)) || str.charAt(indexChar) == ' ' || 
					str.charAt(indexChar) == '-'){
				myChar = true;
			}
			else {
				return 0;
			}
		}	
		if(myChar && myNamber) {
			return 0;
		}
		if(myChar) {
			return -1;
		}
		return 1;
	}
}
