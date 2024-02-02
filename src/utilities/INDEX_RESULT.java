package utilities;

/**
 * This enum represents the result of a guess for a single index.
 * At any one index, that guess could take on one of three values. The letter at that 
 * index could be incorrect, as in not in the answer word, the letter could be in the answer word 
 * at the same exact index, or the letter could be in the answer word but in a different index.
 * The user of the class may use 'getDescription' to get a printable 
 * description of each enum for printing to the console.
 * 
 * @author Tyler Conklin
 *
 */
public enum INDEX_RESULT {
	
	INCORRECT("Incorrect"),
	CORRECT("Correct"),
	CORRECT_WRONG_INDEX("Correct letter, wrong index");
	
	private String description;
	
	/**
	 * INDEX_RESULT constructor
	 * 
	 * @param description A string that represents the description of one of the constants.
	 */
	private INDEX_RESULT(String description) {
		this.description = description;
	}
	
	/**
	 * Returns a description of the enum value.
	 * 
	 * @return A string containing the description of the enum value.
	 */
	public String getDescription() {
		return this.description;
	}
}