package org.requirementsascode.exception;

public class NoSuchElementInModelException extends RuntimeException{
	private static final long serialVersionUID = 8613549211925067343L;

	public NoSuchElementInModelException(String elementName) {
		super("Element does not exist in the model " + elementName);
	}
}
