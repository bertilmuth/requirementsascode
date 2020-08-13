package org.requirementsascode.flowposition;

import org.requirementsascode.ModelRunner;

public class Anytime extends FlowPosition{
	public Anytime() {
		super(null);
	}

	@Override
	protected boolean isRunnerAtRightPositionFor(ModelRunner modelRunner) {
		return true;
	}

  @Override
  public void resolveSteps() {    
  }
}
