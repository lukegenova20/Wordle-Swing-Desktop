package controller;

import java.util.Set;

import model.WordleModel;
import utilities.Constants;
import utilities.Guess;
import utilities.IllegalGuessException;

/**
 * This class represents the controller component of the Wordle game
 * which uses the MVC architecture. The controller takes in user input 
 * from the view and determines if the word is valid. Once a word is
 * validated, it gives the input to the model. This class also determines 
 * when the game is over. Basically, the controller handles the game logistics.
 * 
 * 
 * @author Luke Genova
 *
 */
public class WordleController {
	
	private WordleModel model;
	
	/*
	 * Keeps track of how many attempts the user has. Used to index into the progress array.
	 */
	private int attempt;
	
	/*
	 * Determines if the user guessed the answer correctly or not.
	 */
	private boolean guessedCorrectly;
	
	/**
	 * WordleController constructor.
	 * @param model A model object that represents the model component of the MVC.
	 */
	public WordleController (WordleModel model) {
		this.model = model;
		this.attempt = 1;
		this.guessedCorrectly = false;
	} 
	
	/**
	 * A method that determines if the game is over or not. 
	 * 
	 * @return A boolean whether the game has ended or not.
	 */
	public boolean isGameOver() {
		if (guessedCorrectly) {
			return true;
		} else if (attempt > Constants.NUMBER_OF_GUESSES) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * A getter method that returns a string that represents the 
	 * answer of the game.
	 * 
	 * @return A string that represents the answer of the game.
	 */
	public String getAnswer() {
		return model.getAnswer();
	}
	
	/**
	 * This method checks for any specific exceptions the user's guess 
	 * contains. 
	 * 
	 * @param guess A string of the original guess.
	 * @return A string statement that shows the reason for the user's guess to be 
	 * invalid.  
	 */
	private String checkingExceptions(String guess) {
		String problem = "Guess is not a valid word in the dictionary.";
		if (guess.length() > 5) {
			problem = "Guess is invalid beacue its too long.";
			return problem;
		} else if (guess.length() < 5){
			problem = "Guess is invalid because its too short.";
			return problem;
		} else {
			for (int i = 0; i < guess.length(); i++) {
				char letter = guess.charAt(i);
				if (letter >= 48 && letter <= 57) {
					problem = "Guess is invalid because it contains digits.";
					return problem;
				} else if (!(letter >= 'a' && letter <= 'z') && !(letter >= 'A' && letter <= 'Z')) {
					problem = "Guess is invalid because it has characters that are not allowed.";
					return problem;
				} else {
					continue;
				}
			}
			
			return problem;
		}
		
	}
	
	
	/**
	 * This method checks if the user's guess is a word in the 
	 * dictionary.
	 * 
	 * @param guess A string that represents the user's guess.
	 * @return A boolean value (either true or false) on whether 
	 */
	private boolean isValidWord(String guess) {
		Set<String> dictionary = model.getDictionary();
		if (dictionary.contains(guess.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Performs any work necessary when a guess occurs. Checks if the 
	 * guess is the correct answer and notifies the model of the user's input.
	 * 
	 * @param guess A string that contains the user's guess to the game.
	 * @throws IllegalGuessException An exception where the user typed a guess
	 * that is invalid.
	 */
	public void makeGuess(String guess) throws IllegalGuessException {
		
		// Checks if the word is valid. If not, throw exception.
		if (!isValidWord(guess)) {
			String exception = checkingExceptions(guess);
			throw new IllegalGuessException(exception);
		}
		model.makeGuess(attempt-1, guess);
		
		// Check if the current guess was correct.
		Guess current_guess = model.getProgress()[attempt-1];
		this.attempt++;
		if (current_guess.getIsCorrect() == true){
			this.guessedCorrectly = true;
		}
	}
	
	/**
	 * A getter method that returns how many attempts the user 
	 * has made.
	 * 
	 * @return A integer that represents the number of attempts the user made.
	 */
	public int getAttempt() {
		return attempt;
	}

}
