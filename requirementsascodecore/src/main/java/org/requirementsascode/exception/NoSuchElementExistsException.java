package org.requirementsascode.exception;

public class NoSuchElementExistsException extends RuntimeException {
	private static final long serialVersionUID = 8373632542139282433L;

	public NoSuchElementExistsException(String elementName) {
		super("Element does not exist in the model: " + elementName);
	}
}
