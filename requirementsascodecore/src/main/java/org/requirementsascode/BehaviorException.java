package org.requirementsascode;

/**
 * Exception that is thrown by a behavior when it encounters an internal
 * problem.
 * 
 * @author b_muth
 *
 */
@SuppressWarnings("serial")
public class BehaviorException extends RuntimeException {
	BehaviorException(String message, Throwable cause) {
		super(message, cause);
	}
}
