package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import controller.WordleController;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;
import utilities.IllegalGuessException;


/**
 *
 * This class represents the text based UI version of Wordle. When the 
 * textUI runs, it requests user input through STDIN(Standard input) and 
 * display any changes that resulted from the user input into STDOUT(Standard)
 * output). The view sends the user input through the controller and updates itself
 * after the model changed. The text UI also has a feature were it asks the user if they
 * want to play again and it will restart and play a new game if the user
 * requested it. 
 * 
 * 
 * @author Luke Genova
 *
 */
@SuppressWarnings("deprecation")
public class WordleTextView implements Observer{
	
	/**
	 * This method starts the textUI version of Wordle.
	 */
	public void run() {
		boolean keepPlaying  = true;
		
		// Keeps looping until the user doesn't want to play anymore.
		while (keepPlaying) {
			WordleModel model = new WordleModel();
			WordleController controller = new WordleController(model);
			
			model.addObserver(this);
			
			while (!controller.isGameOver()) {
				
				Scanner userInput = new Scanner(System.in);
				System.out.print("Enter a guess: ");
				String guess= userInput.nextLine();
				
				boolean exceptionCaught = checkingExceptions(guess, controller); 
				if (exceptionCaught) {
					continue;
				}
			}
			
			System.out.println("Good game! The word was " + controller.getAnswer().toUpperCase());
			
			// Ask the user if they want to play again, if not then the main loop would
			// stop which ends the program.
			boolean questionAnswered = false;
			while (!questionAnswered) {
				Scanner userInput = new Scanner(System.in);
				System.out.print("Would you like to play again?(yes/no) ");
				String play_again = userInput.nextLine().toLowerCase();
				questionAnswered = answeredQuestion(play_again);
				if (play_again.equals("no")) {
					keepPlaying = false;
				}
			}
			
		}
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
		System.out.println(printProgress(newModel.getProgress()));
		System.out.println(printGuessedCharacters(newModel.getGuessedCharacters()));
		System.out.println("");
		System.out.println("");
		
	}
	
	/**
	 * This function checks for any invalid input when the interface 
	 * asks the user if they want to play again.
	 * 
	 * @param answer A string the represents the answer the user gave to the 
	 * interface
	 * @return A boolean value that determines if the user answered the question of
	 * whether to continue playing or not.
	 */
	public static boolean answeredQuestion(String answer) {
		if (answer.equals("no")) {
			return true;
		} else if (!answer.equals("no") && !answer.equals("yes")){
			System.out.println("");
			System.out.println("You didn't answer with yes or no. Answer again.");
			System.out.println("");
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * This function generally checks for exceptions when calling the makeGuess
	 * method from the controller.
	 * 
	 * @param guess A string that contains the user's guess to the game.
	 * @param controller A controller object that represents the controller component of the MVC.
	 * @return A boolean value that determines if the function caught an exception from
	 * the makeGuess method in the Controller class.
	 */
	public static boolean checkingExceptions(String guess, WordleController controller) {
		
		// Pass the string guess to the controller makeGuess 
		try {
			controller.makeGuess(guess);
		} catch (IllegalGuessException e) {
			System.out.println(e.getMessage());	
			System.out.println("");
			return true;
		}
		return false;
	}
	
	
	/**
     * This function prints out the current results of a game at some point.
     * 
     * @param progress An array holding the total amount of guesses the user made.
     * @return a string that contains the result of the guess in the progress grid.
     * 
     */
	private static String printProgress(Guess[] progress) {
		String result = "";
		for (int i = 0; i < progress.length; i++) {
			if (progress[i] != null) {
				Guess guess = progress[i];
				INDEX_RESULT[] indices = guess.getIndices();
				String str_guess = guess.getGuess();
				for (int j = 0; j < str_guess.length(); j++) {
					char letter = str_guess.charAt(j);
					if (indices[j].getDescription().equals("Correct")) {
						result += Character.toUpperCase(letter) + " ";
						
					} else if (indices[j].getDescription().equals("Correct letter, wrong index")) {
						result += Character.toLowerCase(letter) + " "; 
					} else {
						result += "_ ";
					}
				}
				result += "\n";
				
			} else {
				for (int j = 0; j < progress.length-1; j++) {
					result += "_ ";
				}
				result += "\n";
			}
		}
		result += "\n";
		return result;
		
	}
	
	/**
	 * This private function prints out the usage result of each letter in the alphabet.
	 * 
	 * @param guessedCharacters An array describing the usage result for each letter in the alphabet.
	 */
	private static String printGuessedCharacters(INDEX_RESULT[] guessedCharacters) {
		String category = "";
		int categories = 4;
		String result = "";
		for (int i = 0; i < categories; i++) {
			if (i == 0) {
				category = "Unguessed";
			}
			if (i == 1) {
				category = "Incorrect";
			}
			if (i == 2) {
				category = "Correct";
			} 
			if (i == 3) {
				category = "Correct letter, wrong index";
			}
			List<Character> listOfLetters = iteratingGuessedCharacters(category, guessedCharacters);
			if (listOfLetters.size() == 0) {
				continue;
			} else {
				result += category + " [";
				result += listOfLetters.get(0);
				for (int j = 1; j < listOfLetters.size(); j++) {
					result += ", " + listOfLetters.get(j);
				}
				result += "]\n";
			}
		}
		return result;
	}
	
	/**
	 * This private function iterates through the array of letter results and creates a list 
	 * that contains all the letters that where in a specific category.
	 * 
	 * @param category A string that represents the name of a specific category of guessed letters.
	 * @param guessedCharacters An array describing the usage result for each letter in the alphabet.
	 * @return A list of characters where the characters represents letters in the alphabet.
	 */
	private static List<Character> iteratingGuessedCharacters(String category, INDEX_RESULT[] guessedCharacters) {
		List<Character> letters = new ArrayList<Character>();
		char letter = ' ';
		for (int i = 0; i < guessedCharacters.length; i++) {
			letter = (char) (i + 65);
			if (guessedCharacters[i] == null) {
				if (category.equals("Unguessed")) {
					letters.add(letter);
				} 
			} else {
				if (guessedCharacters[i].getDescription().equals(category)) {
					letters.add(letter);
				} 
			}
		}
		return letters;
	}

}
