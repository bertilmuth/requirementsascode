package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public abstract class AbstractContinuesAfter extends AbstractContinues {
	private FlowStep previousStep;

	public AbstractContinuesAfter(String stepName, FlowStep previousStep) {
		super(stepName);
		this.previousStep = previousStep;
	}

	@Override
	public void accept(ModelRunner runner) {
		runner.setLatestStep(previousStep);
	}

}
