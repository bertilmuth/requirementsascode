package org.requirementsascode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A use case flow defines a sequence of steps that lead the user through the use case.
 * 
 * A flow either ends with the user reaching her/his goal, or terminates before, usually
 * because of an exception that occurred.
 * 
 * @author b_muth
 *
 */
public class Flow extends UseCaseModelElement {
	private UseCase useCase;

	/**
	 * Creates a use case flow with the specified name that 
	 * belongs to the specified use case.
	 * 
	 * @param name the name of the flow to be created
	 * @param useCase the use case that will contain the new flow
	 */
	Flow(String name, UseCase useCase) {
		super(name, useCase.useCaseModel());
		this.useCase = useCase;
	}

	/**
	 * Returns the use case this flow is part of.
	 * 
	 * @return the containing use case
	 */
	public UseCase useCase() {
		return useCase;
	}
	
	/**
	 * Returns the steps contained in this flow.
	 * Do not modify the returned collection directly.
	 * 
	 * @return a collection of the steps
	 */
	public List<Step> steps() {
		return useCase().steps().stream()
			.filter(step -> step.flow().equals(this))
			.collect(Collectors.toList());
	}
}
