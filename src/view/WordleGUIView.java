package view;

import java.util.Observable;
import java.util.Observer;
import java.util.List;
import java.util.ArrayList;
import controller.WordleController;
import model.WordleModel;
import utilities.Constants;
import utilities.Guess;
import utilities.INDEX_RESULT;
import utilities.IllegalGuessException;
import javax.swing.*;
import java.awt.*;  
import java.awt.event.*; 

/**
 *
 * This class represents the GUI or graphical user interface version of the Wordle
 * game program. When the program runs, it displays a window of the game on the 
 * user's screen. This view takes in input differently, this view holds a field 
 * that keeps track of how many characters the user has inputed after every key
 * press. The view sends the user input through the controller and updates itself
 * after the model changed. 
 * 
 * @author Luke Genova
 *
 */
@SuppressWarnings("deprecation")
public class WordleGUIView extends JFrame implements Observer {

    /* Constants for the scene */
	private static final int FRAME_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 8;

	/* Constants for letters in progress grid */
	private static final int LETTER_FONT_SIZE = 40;
	private static final int LETTER_SQUARE_SIZE = 60;

    /* Constant arrays for unique RGB values */
	private static final int[] UNIQUE_YELLOW = {201, 179, 95};
	private static final int[] UNIQUE_GREEN = {108, 169, 103};
	private static final int[] UNIQUE_GRAY = {120, 124, 126};
	
	/* Constants for letters in keyboard grid */
	private static final int CHARACTER_FONT_SIZE = 20;
	private static final int CHARACTER_SQUARE_HEIGHT = 50;
	private static final int CHARACTER_SQUARE_WIDTH = 30;
	private static final int CHARACTER_GRID_GAP = 10;
	
	/* Constants containing letters for the keyboard grid */
	private static final char[] KEYBOARD_FIRST_ROW = {'Q','W','E','R','T','Y','U','I','O','P'};
	private static final char[] KEYBOARD_SECOND_ROW = {'A','S','D','F','G','H','J','K','L'};
	private static final char[] KEYBOARD_THIRD_ROW = {'Z','X','C','V','B','N','M'};
	private static final int KEYBOARD_ROWS = 3;

	private static final int WHITE_VAL = 255;

	private WordleController controller;
	
	/* Keeps track of the user's input */
	private String curGuess;
	
	/* Used to index into the progress grid*/
	private int blocksUsed;
	private int curRow;

	/* References to labels in grid that stores guesses */
	private JLabel[][] progressGrid;

	/* Reference to labels in the keyboard grid */
	private List<List<JLabel>> keyboardGrid;

    public WordleGUIView() {
		start();
	}

	/**
	 * This private method is called by the constructor of the Frame and it 
	 * sets up the GUI.
	 * 
	 */
	private void start(){
		WordleModel model = new WordleModel();
        model.addObserver(this);
		controller = new WordleController(model);
		curGuess = "";
		blocksUsed = 0;
		curRow = 0;
		progressGrid = new JLabel[Constants.NUMBER_OF_GUESSES][Constants.WORD_LENGTH];
		keyboardGrid = new ArrayList<List<JLabel>>();
        setSize(FRAME_SIZE, FRAME_SIZE);
		setTitle("Wordle");
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
  		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


        // Set up progress grid
        JPanel topGrid = new JPanel();
        topGrid.setLayout(new GridLayout(Constants.NUMBER_OF_GUESSES, 1, 0, 0));
        creatingProgressGrid(topGrid);
        mainPanel.add(topGrid);
		
        // Set up keyboard grid
        JPanel bottomGrid = new JPanel();
        bottomGrid.setLayout(new GridLayout(KEYBOARD_ROWS, 1, 0 ,0));
		bottomGrid.setName("Keyboard");
        creatingKeyboardGrid(bottomGrid);
        mainPanel.add(bottomGrid);

		mainPanel.setBackground(Color.GRAY);
		this.add(mainPanel);

        // Event listener for when the window has closed.
        this.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent windowEvent){
            	System.exit(0);
        	 }        
      	});

		// Event Listener for Keyboard inputs.
		this.addKeyListener(new KeyListener() {
                @Override
                public void keyReleased(KeyEvent key) {
                    // When the game is over. No more text should be processed.
					if (controller.isGameOver()) {
						return;
					}

					JPanel board = (JPanel) mainPanel.getComponent(0);
					int row = curRow;
					int col = blocksUsed % Constants.WORD_LENGTH;

					// Checks if the user pressed delete or backspace (in order to delete a character for a guess)
					if (key.getKeyCode() == KeyEvent.VK_DELETE || key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						if (curGuess.length() != 0) {
							curGuess = curGuess.substring(0, curGuess.length()-1);
							if (blocksUsed > 0) {
								blocksUsed--;
								col = blocksUsed % Constants.WORD_LENGTH;
							}
							JPanel curPanel = (JPanel) board.getComponent(row);
							JLabel prevLabel = (JLabel) curPanel.getComponent(col);
							prevLabel.setText("");
							prevLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
						}
						
					// Checks if the user pressed enter (in order to enter a guess)
					} else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
						try {
							controller.makeGuess(curGuess);
							curGuess = "";
							curRow++;
							if (controller.isGameOver()) {
								String message = "Good game! The word was " + 
												controller.getAnswer().toUpperCase();
								String header = "Game Over";
								showAlert(header, message);
							}
						} catch (IllegalGuessException e) {
							String message = e.getMessage();
							String header = "Error";
							showAlert(header, message);
							
						}
					} else {
						char letter = (char) key.getKeyCode();
						
						// Makes sure that the current guess isn't longer than 5 words and 
						// that the input isn't anything other than a letter. 
						if (curGuess.length() < Constants.WORD_LENGTH) {
							if ((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z')) {
								curGuess += letter;
								JPanel panel = (JPanel) board.getComponent(row);
								JLabel label = (JLabel) panel.getComponent(col);
								label.setForeground(Color.BLACK);
								label.setText("" + letter);
								label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
								blocksUsed++;
							} else{
								return;
							}
						}
					}
    			}
				
				@Override
                public void keyPressed(KeyEvent e) {
					return;
                }

                @Override
                public void keyTyped(KeyEvent e) {
					return;	
                }
		});

		setVisible(true);

	}

    /**
	 * This method is called when the Observed object has changed. 
	 * 
	 * @param o the observable object.
	 * @param arg this argument contains the changed object.
	 *
	 */
	@Override
	public void update(Observable o, Object arg) {
		WordleModel newModel = (WordleModel) o;
		updateProgressGrid(newModel.getProgress());
		updateKeyboardGrid(newModel.getGuessedCharacters());
	}

	/**
	 * This is a private helper method that sets 30 labels as children of
	 * a grid panel. Each label is created and added onto the grid panel.
	 * 
	 * @param grid A panel that would be used to create the guess attempt
	 * progress grid.
	 */
	private void creatingProgressGrid(JPanel grid){
		for (int i = 0; i < Constants.NUMBER_OF_GUESSES; i++) {
			JPanel row = new JPanel();
			row.setLayout(new FlowLayout(FlowLayout.CENTER, GRID_GAP, GRID_GAP));
        	for (int j = 0; j < Constants.WORD_LENGTH; j++) {
        		// Creates a label that represents a single letter
        		// in the progress grid.
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(new Font("Times New Roman", Font.PLAIN, LETTER_FONT_SIZE));
                label.setForeground(Color.BLACK);
				label.setPreferredSize(new Dimension(LETTER_SQUARE_SIZE, LETTER_SQUARE_SIZE));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                label.setBackground(Color.WHITE);
				label.setOpaque(true);
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				progressGrid[i][j] = label;
                row.add(label);
        	}
			grid.add(row);
        }
	}

	/**
	 * This is a private helper method that sets up the keyboard grid on the bottom
	 * of the window. The keyboard grid is basically a panel with a grid layout that
	 * containing 3 children, each of them are panels with grid layouts, 
	 * these panels would have several children which are labels.
	 * 
	 * @param grid A panel that would be used to create the guess attempt
	 * progress grid.
	 */
	private void creatingKeyboardGrid(JPanel grid){

		for (int i = 0; i < KEYBOARD_ROWS; i++) {

			char[] keys;
			JPanel row = new JPanel();
			row.setLayout(new FlowLayout(FlowLayout.CENTER, CHARACTER_GRID_GAP, 0));
			List<JLabel> uiRow = new ArrayList<JLabel>();

			// Determines what row of letters on the keyboard 
        	// should be used to create labels.
        	if (i == 0) {
        		keys = KEYBOARD_FIRST_ROW;
        	} else if (i == 1) {
        		keys = KEYBOARD_SECOND_ROW;
        	} else {
        		keys = KEYBOARD_THIRD_ROW;
        	}

        	for (int j = 0; j < keys.length; j++) {

        		// Creates a label that represents a single letter
        		// in the keyboard grid and is added to a panel.
        		JLabel label = new JLabel(Character.toString(keys[j]), SwingConstants.CENTER);
        		label.setFont(new Font("Times New Roman", Font.PLAIN, CHARACTER_FONT_SIZE));
        		label.setForeground(Color.BLACK);
				label.setPreferredSize(new Dimension(CHARACTER_SQUARE_WIDTH, CHARACTER_SQUARE_HEIGHT));
        		label.setAlignmentX(Component.CENTER_ALIGNMENT);
        		label.setBackground(new Color(220, 220, 220));
				label.setOpaque(true);
        		label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
				uiRow.add(label);
        		row.add(label);
        	}
			keyboardGrid.add(uiRow);
        	grid.add(row);

		}
	}

	/**
	 * This private method updates the progress grid which is represented by
	 * a grid pane with labels as children and changes the color of the boxes
	 * in the grid.
	 * 
	 * @param progress An array holding the total amount of guesses the user made.
	 */
	private void updateProgressGrid(Guess[] progress) {
		int row = blocksUsed / Constants.NUMBER_OF_GUESSES;
		Guess guess = progress[controller.getAttempt()-1];
		INDEX_RESULT[] indices = guess.getIndices();
		String str_guess = guess.getGuess();
		for (int i = 0; i < str_guess.length(); i++) {
			JLabel curLabel = progressGrid[row][i];
			curLabel.setForeground(Color.WHITE);
			curLabel.setBorder(null);
			
			// Change the color of a label 
			String description = indices[i].getDescription();
			if (description.equals("Correct")) {
				changeBoxColor(curLabel, "Green");
			} else if (description.equals("Correct letter, wrong index")) {
				changeBoxColor(curLabel, "Yellow");
			} else {
				changeBoxColor(curLabel, "Gray");
			}
		}
		
	}
	
	/**
	 * This private method updates the keyboard grid which is represented by
	 * a grid pane with HBoxs and labels as children and changes the color of the boxes
	 * in the grid.
	 * 
	 * @param guessedCharacters an array describing the usage result for each letter in the alphabet.
	 */
	private void updateKeyboardGrid(INDEX_RESULT[] guessedCharacters) {
		for (int i = 0; i < guessedCharacters.length; i++) {
			if (guessedCharacters[i] != null) {
				char letter = (char) (i+65);
				
				// Get the index of the HBox and get the label representing the specific letter.
				List<JLabel> row;
				JLabel label;
				int first_index = inArray(letter, KEYBOARD_FIRST_ROW);
				int second_index = inArray(letter, KEYBOARD_SECOND_ROW);
				int third_index = inArray(letter, KEYBOARD_THIRD_ROW);
				if (first_index != -1) {
					row = keyboardGrid.get(0);
					label = row.get(first_index);
				} else if (second_index != -1) {
					row = keyboardGrid.get(1);
					label = row.get(second_index);
				} else {
					row = keyboardGrid.get(2);
					label = row.get(third_index);
				}
				label.setForeground(Color.WHITE);
				label.setBorder(null);
				
				// Change the color of a label 
				String category = guessedCharacters[i].getDescription();
				if (category.equals("Correct")) {
					changeBoxColor(label, "Green");
				} else if (category.equals("Correct letter, wrong index")) {
					changeBoxColor(label, "Yellow");
				} else {
					changeBoxColor(label, "Gray");
				}
						
			}
		}
	}

	/**
	 * This private method creates an alert based on what the header 
	 * states and shows it to the user.
	 * 
	 * @param header a string representing what the header text would look like in the alert.
	 * @param content a string representing that the content text would look like in the alert.
	 */
	private void showAlert(String header, String content) {
		if (header.equals("Error")) {
			JOptionPane.showMessageDialog(this, content, header, JOptionPane.ERROR_MESSAGE);
		} else {
			String[] options = {"Restart", "Exit Game"};
			int result = JOptionPane.showOptionDialog(this, content, header, 
									JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, 
									null, options, options[0]);
			if (result == JOptionPane.YES_OPTION){
				// Play a new game
				new WordleGUIView();
				this.setVisible(false);

			}

			if (result == JOptionPane.NO_OPTION){
				System.exit(0);
			}
		}
		return;
		
	}

	/**
	 * This private method changes the background color of the boxes
	 * in the progress grid which are represented by labels. Determines 
	 * which custom rgb color values which are stored as constant integer arrays to
	 * use.
	 * 
	 * @param label the label that will have its background changed to a specific
	 * color.
	 * @param color a string that is used to determine which custom rgb to use.
	 * 
	 */
	private static void changeBoxColor(JLabel label, String color) {
		final int red;
		final int green;
		final int blue;
		
		if (color.equals("Green")) {
			red = UNIQUE_GREEN[0];
			green = UNIQUE_GREEN[1];
			blue = UNIQUE_GREEN[2];
			
		} else if (color.equals("Yellow")) {
			red = UNIQUE_YELLOW[0];
			green = UNIQUE_YELLOW[1];
			blue = UNIQUE_YELLOW[2];
		} else {
			red = UNIQUE_GRAY[0];
			green = UNIQUE_GRAY[1];
			blue = UNIQUE_GRAY[2];
		}

		label.setForeground(new Color(WHITE_VAL, WHITE_VAL, WHITE_VAL));
		label.setBackground(new Color(red, green, blue));
		
	}

	/** 
	 * This private helper method searches through an array of characters and 
	 * returns the index of a character if its found. Used to index into the
	 * panel and the label inside the panel which represents the guessed 
	 * character grid.
	 * 
	 * @param letter a character representing a letter of the alphabet. 
	 * @param array an array of letters.
	 * @return an integer representing the the index position of the letter Returns
	 * -1 if the letter isn't in the array
	 */
	private int inArray(char letter, char[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == letter) {
				return i;
			}
		}
		return -1;
	}	
}
