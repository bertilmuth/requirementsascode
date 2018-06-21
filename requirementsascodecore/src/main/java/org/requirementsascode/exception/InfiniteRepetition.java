package org.requirementsascode.exception;

import java.io.Serializable;

import org.requirementsascode.Step;

/**
 * Exception that is thrown when internally, a StackOverflowException occurs.
 * The likely cause is that a condition is always true.
 * 
 * @author b_muth
 *
 */
public class InfiniteRepetition extends RuntimeException implements Serializable{
    public InfiniteRepetition(Step step) {
	super("Possible cause: " + step.getName() + " has an always true condition.");
    }

    private static final long serialVersionUID = 5249803987787358917L;

}
