package org.requirementsascode.exception;

public class NoSuchElementInUseCaseException extends RuntimeException{
	private static final long serialVersionUID = -6636292150079241122L;

	public NoSuchElementInUseCaseException(String elementName) {
		super("Element does not exist in the use case: " + elementName);
	}
}