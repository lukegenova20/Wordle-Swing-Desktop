package utilities;


/**
 * This a custom made exception class which is a subclass of
 * the Exception class. This exception class is used to check
 * for invalid user input in the Wordle game.
 * 
 * @author Luke Genova
 * 
 *
 */
public class IllegalGuessException extends Exception {
	
	/**
	 * IllegalGuessException constructor
	 * 
	 * @param message A string that represents the message that caused the exception.
	 */
	public IllegalGuessException(String message) {
		super(message);
	}
	
	/**
	 * This method is a getter method that returns the message that
	 * comes with the exception.
	 *
	 */
	public String getMessage() {
		return super.getMessage();
	}
	
}
