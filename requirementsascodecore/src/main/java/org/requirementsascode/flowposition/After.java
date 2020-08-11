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
  public After(String stepName, UseCase useCase) {
    super(stepName, useCase);
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
  protected boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner) {
    Step latestStepRun = modelRunner.getLatestStep().orElse(null);
    boolean stepWasRunLast = Objects.equals(step, latestStepRun);
    return stepWasRunLast;
  }
}
