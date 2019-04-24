package Observer;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class FBView extends JFrame{
	
	private static int appIDTracker = 0;
	private final String newLine = "\n=============================\n";
	
	private int appID;
	private int eventNo;
	
	protected JButton btnClear = null;
	protected JTextPane paneFeed = null;
	
	protected StyleContext sc = null;
	protected DefaultStyledDocument doc = null;
	
	public FBView() {				
		appID = ++appIDTracker;
		eventNo = 0;
		
		setTitle("Notification #" + appID);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE); //set the behavior of the window when the X icon is clicked
		
		initScreen(); //add components into your window
		
		setSize(400, 600); //set the size of the window by giving the width and height respectively
		
		//setVisible(true); //makes the window visible
	}

	private void initScreen() {		
		sc = new StyleContext();
	    doc = new DefaultStyledDocument(sc);
		paneFeed = new JTextPane(doc);
	    
	    try {
			doc.insertString(0, new String(AppStrings.NOEVENTS+""), null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    getContentPane().add(new JScrollPane(paneFeed));
	}
	
	public void showNewEvent(String newEvent) {
		if(eventNo == 0)
			paneFeed.setText("");
		
		eventNo++;
	    Style eventStyle = sc.addStyle("designchallenge1.Event"+eventNo, null);
	    //eventStyle.addAttribute(StyleConstants.Foreground, eventColor);
	    eventStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
	    eventStyle.addAttribute(StyleConstants.FontFamily, "serif");
	    
	    String eventFormat = newEvent + "\n";
		
		try {
			doc.insertString(doc.getLength(), eventFormat+newLine, eventStyle);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
