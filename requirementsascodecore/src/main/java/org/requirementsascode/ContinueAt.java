package org.requirementsascode;

public class ContinueAt extends Continue{
	ContinueAt(UseCase useCase, String stepName) {
		super(useCase, stepName, useCase.findStep(stepName).previousStepInFlow());
	}
}
