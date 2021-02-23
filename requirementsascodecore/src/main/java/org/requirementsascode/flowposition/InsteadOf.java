package org.requirementsascode.flowposition;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public class InsteadOf extends FlowPosition{
  private String stepName;
  private FlowStep step;
  
	public InsteadOf(String stepName, UseCase useCase) {
	  super(useCase);
	  this.stepName = stepName;
  }

  @Override
  protected boolean isRunnerAtRightPositionFor(ModelRunner modelRunner) {
    if(step == null) {
      throw new RuntimeException("step has not been resolved. Please call resolveSteps()!");
    }
    FlowPosition flowPosition = step.getFlowPosition();
    return flowPosition.test(modelRunner);
  }
  
  public void resolveSteps() {
    if(step == null) {
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
