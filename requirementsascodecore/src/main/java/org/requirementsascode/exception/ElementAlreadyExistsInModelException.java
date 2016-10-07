package org.requirementsascode.exception;

public class ElementAlreadyExistsInModelException extends RuntimeException {
	private static final long serialVersionUID = -510216736346192818L;

	public ElementAlreadyExistsInModelException(String elementName) {		
		super("Element already exists in the model: " + elementName);
	}
}
