package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCase;

public class ContinuesAfter extends AbstractContinuesAfter{
	private UseCase useCase;

  public ContinuesAfter(String stepName, UseCase useCase) {
		super(stepName);
		this.useCase = useCase;
	}

  @Override
  public void resolvePreviousStep() {
    FlowStep previousStep = (FlowStep) useCase.findStep(getStepName());
    setPreviousStep(previousStep);
  }
}