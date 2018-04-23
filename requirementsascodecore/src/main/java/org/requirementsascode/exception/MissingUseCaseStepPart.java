package org.requirementsascode.exception;

import java.io.Serializable;

import org.requirementsascode.Step;

/**
 * Exception that is thrown when the model runner tries to to access a certain part
 * of a step, but that part does not exist.
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
		String message = "Step \"" + useCaseStep + "\" has no defined " + partName + 
			" part! Please have a look and update your model for this step!";
		return message;
	}
}
