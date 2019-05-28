package org.requirementsascode.flowposition;

import java.io.Serializable;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public class Anytime extends FlowPosition implements Serializable {
	private static final long serialVersionUID = 7724607380865304333L;

	public Anytime() {
		super(null);
	}

	@Override
	protected boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner) {
		return true;
	}
}
