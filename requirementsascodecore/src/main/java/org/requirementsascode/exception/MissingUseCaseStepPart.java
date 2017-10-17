package org.requirementsascode.exception;

import java.io.Serializable;

import org.requirementsascode.Step;

/**
 * Exception that is thrown when the use case model runner tries to to access a certain part
 * of a use case step, but that part does not exist.
 * 
 * @author b_muth
 *
 */
public class MissingUseCaseStepPart extends RuntimeException implements Serializable{
	private static final long serialVersionUID = 1154053717206525045L;
	
	public MissingUseCaseStepPart(Step useCaseStep, String partName) {
		super(exceptionMessage(useCaseStep, partName));
	}

	private static String exceptionMessage(Step useCaseStep, String partName) {
		String message = "Use Case Step \"" + useCaseStep + "\" has no defined " + partName + 
			" part! Please have a look and update your Use Case Model for this step!";
		return message;
	}
}
