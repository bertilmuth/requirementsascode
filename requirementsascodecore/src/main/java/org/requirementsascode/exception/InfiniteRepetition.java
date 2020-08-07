package org.requirementsascode.exception;

import org.requirementsascode.Step;

/**
 * Exception that is thrown when internally, a StackOverflowException occurs.
 * The likely cause is that a condition is always true.
 * 
 * @author b_muth
 *
 */
public class InfiniteRepetition extends RuntimeException{
	private static final long serialVersionUID = 5249803987787358917L;
	
	public InfiniteRepetition(Step step) {
		super("Possible cause: " + step.getName() + " has an always true condition.");
	}
}
