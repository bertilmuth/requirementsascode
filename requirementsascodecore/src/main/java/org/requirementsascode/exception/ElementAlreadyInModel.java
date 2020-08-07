package org.requirementsascode.exception;

/**
 * Exception that is thrown when somebody tries to create a new model element,
 * and a model element with the same name is already in the model.
 * 
 * @author b_muth
 *
 */
public class ElementAlreadyInModel extends RuntimeException{
	private static final long serialVersionUID = -510216736346192818L;

	public ElementAlreadyInModel(String elementName) {
		super(exceptionMessage(elementName));
	}

	private static String exceptionMessage(String elementName) {
		return "Element already exists in model: " + elementName;
	}
}
