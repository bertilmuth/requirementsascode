package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public class ContinuesWithoutAlternativeAt extends AbstractContinues{
	public ContinuesWithoutAlternativeAt(String continueAtStepName, FlowStep currentStep) {
		super(continueAtStepName);
		FlowStep continueAtStep = ((FlowStep) currentStep.getUseCase().findStep(continueAtStepName));
		continueAtStep.orAfter(currentStep);
	}

	@Override
	public void accept(ModelRunner runner) {
	}
}