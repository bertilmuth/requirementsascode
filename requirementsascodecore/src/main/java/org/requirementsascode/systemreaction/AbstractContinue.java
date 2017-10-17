package org.requirementsascode.systemreaction;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public abstract class AbstractContinue implements Consumer<UseCaseModelRunner>{
	private String stepName;
	private Step previousStep;
	
	public AbstractContinue(String stepName, Step previousStep) {
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
