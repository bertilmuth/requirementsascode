package org.requirementsascode.exception;

/**
 * Exception that is thrown when more than one step reacts to a certain event.
 * 
 * @author b_muth
 *
 */
public class MoreThanOneStepCouldReactException  extends RuntimeException {	
	private static final long serialVersionUID = 1773129287125843814L;

	public MoreThanOneStepCouldReactException(String message) {
		super(message);
	}
}
