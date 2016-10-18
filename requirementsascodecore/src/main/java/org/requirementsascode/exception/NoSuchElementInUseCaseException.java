package org.requirementsascode.exception;

import org.requirementsascode.UseCase;

public class NoSuchElementInUseCaseException extends RuntimeException{
	private static final long serialVersionUID = -6636292150079241122L;

	public NoSuchElementInUseCaseException(UseCase useCase, String elementName) {
		super("Element does not exist in use case + " + useCase + ": " + elementName);
	}
}