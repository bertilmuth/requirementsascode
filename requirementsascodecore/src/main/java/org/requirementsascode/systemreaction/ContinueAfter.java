package org.requirementsascode.systemreaction;

import org.requirementsascode.UseCase;

public class ContinueAfter extends AbstractContinue{
	public ContinueAfter(UseCase useCase, String stepName) {
		super(stepName, useCase.findStep(stepName));
	}
}