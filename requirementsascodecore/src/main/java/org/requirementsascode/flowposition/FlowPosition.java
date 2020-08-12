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
  private List<After> afterOtherSteps;

  protected abstract boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner);

  public FlowPosition(String stepName, UseCase useCase) {
    this.stepName = stepName;
    this.useCase = useCase;
    this.afterOtherSteps = new ArrayList<>();
  }

  @Override
  public final boolean test(ModelRunner modelRunner) {
    if (step == null) {
      resolveSteps();
    }

    boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step, modelRunner)
      || isAfterAnyOtherStep(modelRunner);
    return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
  }

  public FlowStep resolveSteps() {
    FlowStep flowStep = null;
    this.step = resolveStep(this);
    getAfterOtherSteps().forEach(this::resolveStep);
    return flowStep;
  }

  private FlowStep resolveStep(FlowPosition fp) {
    FlowStep resolvedStep = null;
    
    UseCase fpUseCase = fp.getUseCase();
    String fpStepName = fp.getStepName();
    if (fpUseCase != null && fpStepName != null) {
      resolvedStep = (FlowStep) fpUseCase.findStep(fpStepName);
    }
    
    return resolvedStep;
  }

  private boolean isAfterAnyOtherStep(ModelRunner modelRunner) {
    boolean isAfterStep = false;
    for (After afterOtherStep : afterOtherSteps) {
      if (afterOtherStep.test(modelRunner)) {
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

  public FlowPosition orAfter(String stepName, UseCase useCase) {
    After afterOtherStep = new After(stepName, useCase);
    afterOtherSteps.add(afterOtherStep);
    return this;
  }

  public List<After> getAfterOtherSteps() {
    return afterOtherSteps;
  }
}
