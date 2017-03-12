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
	static Runnable continueAfterStepAndCurrentFlowCantBeReentered(UseCase useCase, String stepName) {
		Optional<UseCaseStep> continueAfterStep = findStepOrThrow(useCase, stepName);
		
		return () -> {
			UseCaseRunner useCaseRunner = useCase.useCaseModel().useCaseRunner();
			useCaseRunner.setLatestStep(continueAfterStep);
		};
	}

	private static Optional<UseCaseStep> findStepOrThrow(UseCase useCase, String stepName) {
		Optional<UseCaseStep> step = useCase.findStep(stepName);
		if(!step.isPresent()){
			throw new NoSuchElementInUseCase(useCase, stepName);
		}
		return step;
	}
}
