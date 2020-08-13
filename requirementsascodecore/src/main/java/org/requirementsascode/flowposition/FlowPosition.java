package org.requirementsascode.flowposition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public abstract class FlowPosition implements Predicate<ModelRunner> {
  private UseCase useCase;
  private FlowStep step;
  private String stepName;
  private List<After> afters;

  protected abstract boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner);

  public FlowPosition(String stepName, UseCase useCase) {
    this.stepName = stepName;
    this.useCase = useCase;
    this.afters = new ArrayList<>();
  }

  @Override
  public final boolean test(ModelRunner modelRunner) {
    if (step == null) {
      resolveStep();
    }

    boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step, modelRunner)
      || isAfterStep(modelRunner);
    return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
  }

  public void resolveStep() {
    FlowStep resolvedStep = null;
    
    UseCase useCase = getUseCase();
    String stepName = getStepName();
    if (useCase != null && stepName != null) {
      resolvedStep = (FlowStep) useCase.findStep(stepName);
    }
    
    this.step = resolvedStep;
  }

  private boolean isAfterStep(ModelRunner modelRunner) {
    boolean isAfterStep = false;
    for (After afterStep : afters) {
      if (afterStep.test(modelRunner)) {
        isAfterStep = true;
        break;
      }
    }
    return isAfterStep;
  }
  
  public final String getStepName() {
    return stepName;
  }

  public final UseCase getUseCase() {
    return useCase;
  }

  public FlowPosition after(String stepName, UseCase useCase) {
    After after = new After(stepName, useCase);
    afters.add(after);
    return this;
  }

  public List<After> getAfters() {
    return afters;
  }
}
