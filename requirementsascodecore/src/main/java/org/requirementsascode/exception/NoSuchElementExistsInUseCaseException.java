package org.requirementsascode.exception;

public class NoSuchElementExistsInUseCaseException extends RuntimeException{
	private static final long serialVersionUID = -6636292150079241122L;

	public NoSuchElementExistsInUseCaseException(String elementName) {
		super("Element does not exist in the use case: " + elementName);
	}
}