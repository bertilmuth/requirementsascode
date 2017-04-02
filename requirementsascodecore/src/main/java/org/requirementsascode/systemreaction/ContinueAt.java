package org.requirementsascode.systemreaction;

import org.requirementsascode.UseCase;

public class ContinueAt extends AbstractContinue{
	public ContinueAt(UseCase useCase, String stepName) {
		super(stepName, useCase.findStep(stepName).getPreviousStepInFlow());
	}
}
