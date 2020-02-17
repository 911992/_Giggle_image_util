/**
 * 
 */
package giggle.tribe.util.ex;

/**
 * @author Arash
 * @version 20200215 (Feb 15, 2020)
 *
 *          Thrown when given image processing setup instance has an invalid
 *          state
 */
public class Bad_Image_Processing_State_Exception extends Exception {
	/**
	 * generated serial id for serialization stuffs
	 */
	private static final long serialVersionUID = 394576401198581292L;
	/**
	 * Holds the local message about thrown instance reason
	 */
	private String message;

	/**
	 * Instance the exception, and set the related message by given arg
	 * 
	 * @param message should be verbosed
	 */
	public Bad_Image_Processing_State_Exception(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
