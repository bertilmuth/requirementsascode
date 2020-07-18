package org.requirementsascode.flowposition;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public class Anytime extends FlowPosition{
	public Anytime() {
		super(null);
	}

	@Override
	protected boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner) {
		return true;
	}
}
