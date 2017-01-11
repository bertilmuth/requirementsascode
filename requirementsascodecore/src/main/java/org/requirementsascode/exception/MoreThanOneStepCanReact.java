package org.requirementsascode.exception;

import java.util.Collection;
import java.util.stream.Collectors;

import org.requirementsascode.UseCaseStep;

/**
 * Exception that is thrown when more than one step could react to a certain event.
 * 
 * @author b_muth
 *
 */
public class MoreThanOneStepCanReact  extends RuntimeException {	
	private static final long serialVersionUID = 1773129287125843814L;

	public MoreThanOneStepCanReact(Collection<UseCaseStep> useCaseSteps) {
		super(exceptionMessage(useCaseSteps));
	}
	
	private static String exceptionMessage(Collection<UseCaseStep> useCaseSteps) {
		String message = "System could react to more than one step: ";
		String useCaseStepsClassNames = useCaseSteps.stream().map(useCaseStep -> useCaseStep.toString())
				.collect(Collectors.joining(",", message, ""));
		return useCaseStepsClassNames;
	}
}
