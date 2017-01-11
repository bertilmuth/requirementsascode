package org.requirementsascode.exception;

/**
 * Exception that is thrown when an element should be in the model
 * because it is referenced from somewhere in the model, but it can't be found.
 * 
 * @author b_muth
 *
 */
public class NoSuchElementInModel extends RuntimeException{
	private static final long serialVersionUID = 8613549211925067343L;

	public NoSuchElementInModel(String elementName) {
		super(exceptionMessage(elementName));
	}

	private static String exceptionMessage(String elementName) {
		return "Element does not exist in the model " + elementName;
	}
}
