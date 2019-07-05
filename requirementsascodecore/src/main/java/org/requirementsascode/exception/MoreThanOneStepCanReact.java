package org.requirementsascode.exception;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.requirementsascode.Step;

/**
 * Exception that is thrown when more than one step could react to a certain
 * message.
 * 
 * @author b_muth
 *
 */
public class MoreThanOneStepCanReact extends RuntimeException implements Serializable {
	private static final long serialVersionUID = 1773129287125843814L;

	public MoreThanOneStepCanReact(Collection<Step> useCaseSteps) {
		super(exceptionMessage(useCaseSteps));
	}

	private static String exceptionMessage(Collection<Step> useCaseSteps) {
		String message = "System can react to more than one step: ";
		String useCaseStepsClassNames = useCaseSteps.stream().map(useCaseStep -> useCaseStep.toString())
				.collect(Collectors.joining(",", message, ""));
		return useCaseStepsClassNames;
	}
}
