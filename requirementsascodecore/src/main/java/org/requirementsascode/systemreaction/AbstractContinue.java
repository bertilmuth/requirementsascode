package org.requirementsascode.systemreaction;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.Step;

public abstract class AbstractContinue implements Consumer<UseCaseModelRunner>{
	private String stepName;
	private Optional<Step> previousStep;
	
	public AbstractContinue(String stepName, Optional<Step> previousStep) {
		Objects.requireNonNull(stepName);
		Objects.requireNonNull(previousStep);
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
