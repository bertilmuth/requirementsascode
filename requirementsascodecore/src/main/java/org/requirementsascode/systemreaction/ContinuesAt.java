package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCase;

public class ContinuesAt extends AbstractContinuesAfter {
	private UseCase useCase;

  public ContinuesAt(String stepName, UseCase useCase) {
		super(stepName);
    this.useCase = useCase;
	}

  @Override
  public void resolvePreviousStep() {
    FlowStep step = (FlowStep) useCase.findStep(getStepName());
    FlowStep previousStep = step.getPreviousStepInFlow().orElse(null);
    setPreviousStep(previousStep);
  }
}
