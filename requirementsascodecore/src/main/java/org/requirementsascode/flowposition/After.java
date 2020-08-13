package org.requirementsascode.flowposition;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

/**
 * Tests whether the specified step was the last step run.
 * 
 * @author b_muth
 *
 */
public class After extends FlowPosition {
  public After(String[] stepNames, UseCase useCase) {
    super(useCase);
    afterSteps(stepNames);
  }

  private void afterSteps(String[] stepNames) {
    for (int i = 0; i < stepNames.length; i++) {
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
    boolean result = isAfterAnyStep(modelRunner);
    return result;
  }
  
  private boolean isAfterAnyStep(ModelRunner modelRunner) {
    boolean isAfterStep = false;
    for (AfterSingleStep afterSingleStep : getAfterForEachSingleStep()) {
      if (afterSingleStep.test(modelRunner)) {
        isAfterStep = true;
        break;
      }
    }
    return isAfterStep;
  }
  
  @Override
  public void resolveSteps() {
    for(AfterSingleStep afterSingleStep : getAfterForEachSingleStep()) {
      afterSingleStep.resolveStep();
    }
  }
}
