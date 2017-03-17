package org.requirementsascode;

import java.util.Optional;

import org.requirementsascode.exception.NoSuchElementInUseCase;

/**
 * Class that defines standard system reactions.
 * 
 * @author b_muth
 *
 */
class SystemReaction {
	static Runnable continueAtStep(UseCase useCase, String stepName) {
		UseCaseStep continueAtStep = findStepOrThrow(useCase, stepName).get();
		Optional<UseCaseStep> continueAfterStep = continueAtStep.previousStepInFlow();
		
		return runnerContinueAfter(useCase, continueAfterStep);
	}
	
	static Runnable continueAfterStep(UseCase useCase, String stepName) {
		Optional<UseCaseStep> continueAfterStep = findStepOrThrow(useCase, stepName);
		return runnerContinueAfter(useCase, continueAfterStep);
	}
	
	private static Runnable runnerContinueAfter(UseCase useCase, Optional<UseCaseStep> continueAfterStep) {
		return () -> useCase.useCaseModel().useCaseRunner().setLatestStep(continueAfterStep);
	}

	private static Optional<UseCaseStep> findStepOrThrow(UseCase useCase, String stepName) {
		Optional<UseCaseStep> step = useCase.findStep(stepName);
		if(!step.isPresent()){
			throw new NoSuchElementInUseCase(useCase, stepName);
		}
		return step;
	}
}
