package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCase;

public class ContinuesAt extends AbstractContinuesAfter {
	public ContinuesAt(String stepName, UseCase useCase) {
		super(stepName, ((FlowStep) useCase.findStep(stepName)).getPreviousStepInFlow().orElse(null));
	}
}
