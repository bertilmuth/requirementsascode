package org.requirementsascode;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractContinue implements Consumer<UseCaseRunner>{
	private UseCase useCase;
	private String stepName;
	private Optional<UseCaseStep> latestStepOfRunner;
	
	public AbstractContinue(UseCase useCase, String stepName, Optional<UseCaseStep> latestStepOfRunner) {
		this.useCase = useCase;
		this.stepName = stepName;
		this.latestStepOfRunner = latestStepOfRunner;
	}
	
	@Override
	public void accept(UseCaseRunner runner) {
		runner.setLatestStep(latestStepOfRunner);
	}
	
	public UseCase useCase() {
		return useCase;
	}
	public String stepName() {
		return stepName;
	}
	public Optional<UseCaseStep> latestStepOfRunner() {
		return latestStepOfRunner;
	}
}
