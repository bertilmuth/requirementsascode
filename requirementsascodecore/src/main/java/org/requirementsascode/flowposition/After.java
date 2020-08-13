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

  public After(String[] stepNames, UseCase useCase) {
    super(useCase);
    this.stepName = stepNames[0];
    afterRemainingSteps(stepNames);
  }

  private void afterRemainingSteps(String[] stepNames) {
    for (int i = 1; i < stepNames.length; i++) {
      orAfter(stepNames[i], getUseCase());
    }
  }

  public static After afterFlowStep(FlowStep flowStep) {
    UseCase useCase = flowStep == null ? null : flowStep.getUseCase();
    String stepName = flowStep == null ? null : flowStep.getName();
    After afterFlowStep = new After(new String[] {stepName}, useCase);
    return afterFlowStep;
  }

  @Override
  protected boolean isRunnerAtRightPositionFor(ModelRunner modelRunner) {
    Step latestStepRun = modelRunner.getLatestStep().orElse(null);
    boolean stepWasRunLast = Objects.equals(step, latestStepRun) || isAfterAnyOtherStep(modelRunner);
    return stepWasRunLast;
  }
  
  private boolean isAfterAnyOtherStep(ModelRunner modelRunner) {
    boolean isAfterStep = false;
    for (After afterOtherStep : getAfterOtherSteps()) {
      if (afterOtherStep.test(modelRunner)) {
        isAfterStep = true;
        break;
      }
    }
    return isAfterStep;
  }
  
  public void resolveStep() {
    if (step == null) {
      this.step = resolveStep(stepName);
    }
  }

  private FlowStep resolveStep(String stepName) {
    FlowStep resolvedStep = null;

    UseCase useCase = getUseCase();
    if (useCase != null && stepName != null) {
      resolvedStep = (FlowStep) useCase.findStep(stepName);
    }

    return resolvedStep;
  }
  
  public final String getStepName() {
    return stepName;
  }
}
