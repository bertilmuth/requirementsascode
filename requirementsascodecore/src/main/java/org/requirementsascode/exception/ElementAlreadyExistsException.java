package org.requirementsascode.exception;

public class ElementAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = -510216736346192818L;

	public ElementAlreadyExistsException(String elementName) {
		super("Element already exists in the model: " + elementName);
	}
}
