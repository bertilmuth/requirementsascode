package org.requirementsascode.flowposition;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public class InsteadOf extends FlowPosition{
	public InsteadOf(String stepName, UseCase useCase) {
	  super(stepName, useCase);
  }

  @Override
	protected boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner) {
		FlowStep previousStep = step.getPreviousStepInFlow().orElse(null);
		After afterPreviousStep = After.flowStep(previousStep);
		return afterPreviousStep.test(modelRunner);
	}
}
