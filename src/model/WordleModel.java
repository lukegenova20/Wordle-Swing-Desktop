package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import utilities.Constants;
import utilities.Guess;
import utilities.INDEX_RESULT;


/**
 *
 * This class represents the model component of the Wordle game 
 * which uses a MVC architecture. The model generally stores valuable information
 * in data structures such as the guess result of each letter of the alphabet, the 
 * game progression grid, and the list of valid words in the dictionary. The model can 
 * take the guess given and update information based on the result, reads words from a 
 * text file  which it stores in a set for the controller to use for exception checking, 
 * and determines the answer by randomly selecting a word from the dictionary. The model 
 * also notifies the View through the Observer/Observable pattern when data within the model 
 * has changed. 
 * 
 * @author Luke Genova
 * 
 */
@SuppressWarnings("deprecation")
public class WordleModel extends Observable {
	
	
	private static final String FILENAME = "./Dictionary.txt";
	
	private String answer;
	/* 
	 * Maintains an array of INDEX_RESULTs for the guessed characters. There
	 * should be 26 indices in this array, one for each character in the English
	 * alphabet. Before a character has been guessed, its position in the array
	 * should hold the value 'null'.
	 */
	private INDEX_RESULT[] guessedCharacters;
	
	private Set<String> dictionary;
	
	/* 
	 * Maintains the progress the user has made so far. This array should have
	 * as many indices as there are turns/guesses for the user. Indices for turns
	 * that the user has not had yet (that are in the future) should be indicated
	 * by a 'null' value.
	 */
	private Guess[] progress;

	/**
	 * WordleModel constructor.
	 */
	public WordleModel() { 
		// Gets the set of possible words from Dictionary.txt and randomly
		// chooses a word.
		this.dictionary = dictionaryFile(FILENAME);
		Random rand = new Random();
		int rand_num = rand.nextInt(dictionary.size());
		Object[] toArray = dictionary.toArray();
		this.answer = (String) toArray[rand_num];
		this.guessedCharacters = new INDEX_RESULT[Constants.LETTERS_IN_ALPHABET];
		this.progress = new Guess[Constants.NUMBER_OF_GUESSES];
	}
	
	/**
	 * Adds an observer to the set of observers for this object, 
	 * provided that it is not the same as some observer already in 
	 * the set.
	 * 
	 * @param o an observer to be added.
	 */
	public void addObserver(Observer o) {
		super.addObserver(o);
	}
	
	/**
     * Reads "dictionary.txt" file and collect information.
     * 
     * @param filename A string that contains the url of a file.
     * 
     * @return A set that contains every word in the dictionary.txt file.
     */
	private Set<String> dictionaryFile(String filename) {
		Scanner fileInput = null;

        try {
            fileInput = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Set<String> dictionary = new HashSet<String>();

        while (fileInput.hasNext()) {
        	String line = fileInput.nextLine().toLowerCase();
        	dictionary.add(line);
            
        }
        fileInput.close();
        return dictionary;
	}
	
	/**
	 * This private helper method creates a guess object and adds it to the progress
	 * array. This method also determines if there guess is correct or not.
	 * 
	 * @param guessNumber a integer that represents what attempt the user is on.
	 * @param guess a string that contains the user's guess to the game.
	 * @param indices An array describing the correctness of each individual index in the guess.
	 */
	private void addGuess(int guessNumber, String guess, INDEX_RESULT[] indices) {
		boolean isCorrect = false;
		String answer = getAnswer().toLowerCase();
		if (guess.toLowerCase().equals(answer)) {
			isCorrect = true;
		}
		Guess current_guess = new Guess(guess, indices, isCorrect);
		progress[guessNumber] = current_guess;
		
	}
	
	/**
	 * Performs any work necessary when a guess occurs. Updates what letters in the
	 * alphabet have been guessed. Determines if the guess is the right answer. Notifies
	 * the views when there has been a change. 
	 * 
	 * @param guessNumber a integer that represents what attempt the user is on.
	 * @param guess a string that contains the user's guess to the game.
	 */
	public void makeGuess(int guessNumber, String guess) {
		
		// Iterates through the guess and determine the correctness of each
		// letter.
		INDEX_RESULT[] indices = new INDEX_RESULT[Constants.WORD_LENGTH];
		for (int i = 0; i < guess.length(); i++) {
			char letter = Character.toLowerCase(guess.charAt(i));
			String guess_letter = "" + letter;
			String answer_letter = "" + Character.toLowerCase(answer.charAt(i));
			int alphabet_index = (int)letter % 97;
			if (guess_letter.equals(answer_letter)) {
				indices[i] = INDEX_RESULT.CORRECT;
				guessedCharacters[alphabet_index] = INDEX_RESULT.CORRECT;
			} else if (answer.contains(guess_letter)){
				indices[i] = INDEX_RESULT.CORRECT_WRONG_INDEX;
				if (guessedCharacters[alphabet_index] != null) {
					if (!guessedCharacters[alphabet_index].getDescription().equals("Correct")){
						guessedCharacters[alphabet_index] = INDEX_RESULT.CORRECT_WRONG_INDEX;
					}
				} else {
					guessedCharacters[alphabet_index] = INDEX_RESULT.CORRECT_WRONG_INDEX;
				}
			} else {
				indices[i] = INDEX_RESULT.INCORRECT;
				guessedCharacters[alphabet_index] = INDEX_RESULT.INCORRECT;
			}
		}
		
		addGuess(guessNumber, guess, indices);
		super.setChanged();
		super.notifyObservers();
		super.clearChanged();
		return;
	}
	

	/**
	 * Return the answer. Used to show the answer at the end of the game.
	 * 
	 * @return A string contain the answer of the game.
	 */
	public String getAnswer() {
		return this.answer;
	}
	
	/**
	 * Return the set of valid words that the user can input.
	 * 
	 * @return A set of every valid word in the dictionary.
	 */
	public Set<String> getDictionary(){
		return this.dictionary;
	}

	/** 
	 * Return the guessed characters. 
	 * 
	 * @return An array of the result of the guess on each letter in the alphabet
	 */
	public INDEX_RESULT[] getGuessedCharacters() {
		return this.guessedCharacters;
	}
	
	/**
	 * A getter method that returns the array of Guess objects.
	 * 
	 * @return An array of guess objects.
	 */
	public Guess[] getProgress() {
		return this.progress;
	}

}
