package org.requirementsascode.predicate;

import java.io.Serializable;
import java.util.Objects;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class InsteadOf implements FlowPosition, Serializable{
  private static final long serialVersionUID = -3958653686352185075L;
  
  private Step step;
  private After after;

  public InsteadOf(Step step) {
    Objects.requireNonNull(step);
    this.step = step;
    this.after = new After(step.getPreviousStepInFlow().orElse(null));
  }
	
	@Override
	public boolean test(UseCaseModelRunner useCaseModelRunner) {
		return after.test(useCaseModelRunner);
  }

  public String getStepName() {
    return step.getName();
  }
}
