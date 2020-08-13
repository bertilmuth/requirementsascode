package org.requirementsascode.flowposition;

import java.util.Objects;
import java.util.function.Predicate;

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
public class AfterSingleStep implements Predicate<ModelRunner>{
  private String stepName;
  private FlowStep step;
  private UseCase useCase;

  public AfterSingleStep(String stepName, UseCase useCase) {
    this.stepName = stepName;
    this.useCase = useCase;
  }
  
  public void resolveStep() {
    if (step == null) {
      this.step = resolveStep(stepName);
    }
  }

  private FlowStep resolveStep(String stepName) {
    FlowStep resolvedStep = null;

    if (useCase != null && stepName != null) {
      resolvedStep = (FlowStep) useCase.findStep(stepName);
    }

    return resolvedStep;
  }
  
  public final String getStepName() {
    return stepName;
  }

  @Override
  public boolean test(ModelRunner modelRunner) {
    Step latestStepRun = modelRunner.getLatestStep().orElse(null);
    boolean stepWasRunLast = Objects.equals(step, latestStepRun);
    return stepWasRunLast; 
  } 
}
