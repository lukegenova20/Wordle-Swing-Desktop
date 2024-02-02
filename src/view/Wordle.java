package view;

/**
 *
 * This is the main class of the entire Wordle program. It takes 
 * a command line argument through whatever means which determines what
 * UI will display when the game starts. 
 * 
 * The only two command arguments this program accepts:
 * 
 * First, -text which enables the text UI.
 * 
 * Second, -gui which enables the GUI.
 * 
 * ** If there is no command argument, the GUI version will be launched. **
 * 
 * @author Luke Genova
 *
 */

public class Wordle {
    private static final String TEXTUI = "-text";
	
	private static final String GRAPHICSUI = "-gui";

    public static void main(String[] args) {
        boolean commandFound = false;
    	for (String str: args) {
    		if (str.equals(TEXTUI)) {
    			commandFound = true;
    			WordleTextView textView = new WordleTextView();
    			textView.run();
    			break;
    		}
    		if (str.equals(GRAPHICSUI)) {
    			commandFound = true;
                new WordleGUIView();
    			break;
    		}
    	}
    	if (!commandFound) {
    		new WordleGUIView();
    	}
    }
    
}
