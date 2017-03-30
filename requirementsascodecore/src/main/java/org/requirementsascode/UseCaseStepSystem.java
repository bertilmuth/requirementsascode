package org.requirementsascode;

import java.util.function.Consumer;

/**
 * The part of the step that contains a reference to the system reaction
 * that can be triggered (given an appropriate actor and event, and the 
 * step's predicate being true).
 * 
 * @author b_muth
 *
 */
public class UseCaseStepSystem<T>{
	private Consumer<T> systemReaction;

	public UseCaseStepSystem(UseCaseStep useCaseStep, Consumer<T> systemReaction) {
		this.systemReaction = systemReaction;
		useCaseStep.setSystem(this);
	}
	
	/**
	 * Returns the system reaction,
	 * meaning how the system will react when the step's predicate
	 * is true and an appropriate event object is received
	 * via {@link UseCaseRunner#reactTo(Object)}.
	 * 
	 * @return the system reaction
	 */
	public Consumer<T> systemReaction() {
		return systemReaction;
	}
}