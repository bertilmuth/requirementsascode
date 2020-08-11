package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public class ContinuesWithoutAlternativeAt extends AbstractContinues {
  private FlowStep currentStep;
  private FlowStep continueAtStep;

  public ContinuesWithoutAlternativeAt(String continueAtStepName, FlowStep currentStep) {
    super(continueAtStepName);
    this.currentStep = currentStep;

  }

  @Override
  public void accept(ModelRunner runner) {
    if(continueAtStep == null) {
      resolveContinueAtStep();
    }
  }

  public void resolveContinueAtStep() {
    continueAtStep = ((FlowStep) currentStep.getUseCase().findStep(getStepName()));
    continueAtStep.orAfter(currentStep);
  }
}