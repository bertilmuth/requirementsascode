package org.requirementsascode.flowposition;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public class InsteadOf extends FlowPosition{
	public InsteadOf(FlowStep step) {
		super(step);
	}

	@Override
	protected boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner) {
		FlowStep previousStep = step.getPreviousStepInFlow().orElse(null);
		After after = new After(previousStep);
		return after.test(modelRunner);
	}
}
