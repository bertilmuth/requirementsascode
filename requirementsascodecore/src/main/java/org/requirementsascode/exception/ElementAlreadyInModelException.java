package org.requirementsascode.exception;

public class ElementAlreadyInModelException extends RuntimeException {
	private static final long serialVersionUID = -510216736346192818L;

	public ElementAlreadyInModelException(String elementName) {		
		super("Element already exists in the model: " + elementName);
	}
}
