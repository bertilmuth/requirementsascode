package org.requirementsascode.exception;

import org.requirementsascode.UseCase;

/**
 * Exception that is thrown when an element should be in the use case
 * because it is referenced from somewhere in the same use case, but it can't be found.
 * 
 * @author b_muth
 *
 */
public class NoSuchElementInUseCase extends RuntimeException{
	private static final long serialVersionUID = -6636292150079241122L;

	public NoSuchElementInUseCase(UseCase useCase, String elementName) {
		super(exceptionMessage(useCase, elementName));
	}

	private static String exceptionMessage(UseCase useCase, String elementName) {
		return "Element does not exist in use case + " + useCase + ": " + elementName;
	}
}