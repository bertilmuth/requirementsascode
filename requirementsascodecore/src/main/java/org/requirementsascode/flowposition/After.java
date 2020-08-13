package org.requirementsascode.flowposition;

import java.util.Objects;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;
import org.requirementsascode.UseCase;

/**
 * Tests whether the specified step was the last step run.
 * 
 * @author b_muth
 *
 */
public class After extends FlowPosition {
  private String stepName;
  private FlowStep step;

  public After(String stepName, UseCase useCase) {
    super(useCase);
    this.stepName = stepName;
  }

  public After(String[] stepNames, UseCase useCase) {
    this(stepNames[0], useCase);
    afterRemainingSteps(stepNames);
  }

  private void afterRemainingSteps(String[] stepNames) {
    for (int i = 1; i < stepNames.length; i++) {
      orAfter(stepNames[i], getUseCase());
    }
  }

  public static After flowStep(FlowStep flowStep) {
    UseCase useCase = flowStep == null ? null : flowStep.getUseCase();
    String stepName = flowStep == null ? null : flowStep.getName();
    After afterFlowStep = new After(stepName, useCase);
    return afterFlowStep;
  }

  @Override
  protected boolean isRunnerAtRightPositionFor(ModelRunner modelRunner) {
    Step latestStepRun = modelRunner.getLatestStep().orElse(null);
    boolean stepWasRunLast = Objects.equals(step, latestStepRun);
    return stepWasRunLast;
  }

  public void resolveStep() {
    if (step == null) {
      FlowStep resolvedStep = null;

      UseCase useCase = getUseCase();
      String stepName = getStepName();
      if (useCase != null && stepName != null) {
        resolvedStep = (FlowStep) useCase.findStep(stepName);
      }

      this.step = resolvedStep;
    }
  }
  
  public final String getStepName() {
    return stepName;
  }
}
