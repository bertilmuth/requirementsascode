package org.requirementsascode;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractContinue implements Consumer<UseCaseRunner>{
	private UseCase useCase;
	private String stepName;
	private Optional<UseCaseStep> previousStep;
	
	public AbstractContinue(UseCase useCase, String stepName, Optional<UseCaseStep> previousStep) {
		this.useCase = useCase;
		this.stepName = stepName;
		this.previousStep = previousStep;
	}
	
	@Override
	public void accept(UseCaseRunner runner) {
		runner.setLatestStep(previousStep);
	}
	
	public UseCase useCase() {
		return useCase;
	}
	public String stepName() {
		return stepName;
	}
	public Optional<UseCaseStep> previousStep() {
		return previousStep;
	}
}
