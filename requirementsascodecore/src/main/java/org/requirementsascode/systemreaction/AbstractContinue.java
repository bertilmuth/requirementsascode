package org.requirementsascode.systemreaction;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.FlowStep;
import org.requirementsascode.UseCaseModelRunner;

public abstract class AbstractContinue implements Consumer<UseCaseModelRunner>{
	private String stepName;
	private FlowStep previousStep;
	
	public AbstractContinue(String stepName, FlowStep previousStep) {
		Objects.requireNonNull(stepName);
		this.stepName = stepName;
		this.previousStep = previousStep;
	}
	
	@Override
	public void accept(UseCaseModelRunner runner) {
		runner.setLatestStep(previousStep);
	}
	
	public String getStepName() {
		return stepName;
	}
}
