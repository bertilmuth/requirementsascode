package org.requirementsascode.systemreaction;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;

public abstract class AbstractContinuesAfter extends AbstractContinues {
	private FlowStep previousStep;

	public AbstractContinuesAfter(String stepName) {
		super(stepName);
	}

	@Override
  public void accept(ModelRunner runner) {
    if(previousStep == null) {
      previousStep = resolvePreviousStep();
    }
    runner.setLatestStep(previousStep);
  }

  public abstract FlowStep resolvePreviousStep();
}
