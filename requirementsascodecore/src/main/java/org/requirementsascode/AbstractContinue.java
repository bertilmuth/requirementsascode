package org.requirementsascode;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractContinue implements Consumer<UseCaseRunner>{
	private String stepName;
	private Optional<UseCaseStep> previousStep;
	
	public AbstractContinue(String stepName, Optional<UseCaseStep> previousStep) {
		this.stepName = stepName;
		this.previousStep = previousStep;
	}
	
	@Override
	public void accept(UseCaseRunner runner) {
		runner.setLatestStep(previousStep);
	}
	
	public String stepName() {
		return stepName;
	}
}
