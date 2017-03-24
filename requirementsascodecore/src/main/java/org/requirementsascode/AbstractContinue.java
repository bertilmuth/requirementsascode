package org.requirementsascode;

import java.util.Optional;

public abstract class AbstractContinue implements Runnable{
	private UseCase useCase;
	private String stepName;
	private Optional<UseCaseStep> latestStepOfRunner;
	
	public AbstractContinue(UseCase useCase, String stepName, Optional<UseCaseStep> latestStepOfRunner) {
		this.useCase = useCase;
		this.stepName = stepName;
		this.latestStepOfRunner = latestStepOfRunner;
	}
	
	public void run() {
		useCase.useCaseModel().useCaseRunner().setLatestStep(latestStepOfRunner);
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
