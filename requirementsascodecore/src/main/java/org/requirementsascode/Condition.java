package org.requirementsascode;

/**
 * A condition, as part of a model. A condition is used to evaluate when a model
 * runner triggers a system reaction, given that an event of a type defined in
 * the model is received.
 * 
 * @author b_muth
 *
 */
@FunctionalInterface
public interface Condition {
	boolean evaluate();
}
