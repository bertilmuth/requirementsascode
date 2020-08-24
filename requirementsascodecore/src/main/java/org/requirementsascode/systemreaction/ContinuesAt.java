package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;

public class ContinuesAt<T> extends AbstractContinues<T> {
  private FlowStep currentStep;
  private FlowStep continueAtStep;

  public ContinuesAt(String continueAtStepName, FlowStep currentStep) {
    super(continueAtStepName);
    this.currentStep = currentStep;

  }

  @Override
  public void accept(Object message) {
    if(continueAtStep == null) {
      resolveContinueAtStep();
    }
  }

  public void resolveContinueAtStep() {
    continueAtStep = ((FlowStep) currentStep.getUseCase().findStep(getStepName()));
    continueAtStep.orAfter(currentStep);
  }
}