package org.requirementsascode;

public class ContinueAt extends AbstractContinue{
	ContinueAt(UseCase useCase, String stepName) {
		super(useCase, stepName, useCase.findStep(stepName).previousStepInFlow());
	}
}
