package org.requirementsascode.exception;

import java.io.Serializable;

/**
 * Exception that is thrown when an element should be in the model
 * because it is referenced from somewhere, but it can't be found.
 * 
 * @author b_muth
 *
 */
public class NoSuchElementInModel extends RuntimeException implements Serializable{
	private static final long serialVersionUID = -6636292150079241122L;

	public NoSuchElementInModel(String elementName) {
		super(exceptionMessage(elementName));
	}

	private static String exceptionMessage(String elementName) {
		return "Element does not exist in model:" + elementName;
	}
}