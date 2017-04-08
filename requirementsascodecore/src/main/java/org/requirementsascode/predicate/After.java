package org.requirementsascode.predicate;

import java.util.Objects;
import java.util.Optional;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class After implements FlowPosition{
	private Optional<Step> step;

	public After(Optional<Step> step) {
		Objects.requireNonNull(step);
		this.step = step;
	}
	
	@Override
	public boolean test(UseCaseModelRunner useCaseModelRunner) {
		Optional<Step> latestStep = useCaseModelRunner.getLatestStep();
		boolean isSystemAtRightStep = 
			Objects.equals(step, latestStep);
		return isSystemAtRightStep;	
  }

  public String getStepName() {
    return step.map(s -> s.getName()).orElse("");
  }
}
