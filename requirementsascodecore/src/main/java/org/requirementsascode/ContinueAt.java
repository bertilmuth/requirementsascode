package org.requirementsascode;

public class ContinueAt extends AbstractContinue{
	public ContinueAt(UseCase useCase, String stepName) {
		super(stepName, useCase.findStepOrThrow(stepName).previousStepInFlow());
	}
}
