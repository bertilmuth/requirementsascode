package org.requirementsascode;

import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.exception.NoSuchElementInUseCase;

/**
 * Class that defines standard system reactions.
 * 
 * @author b_muth
 *
 */
class SystemReaction {
	static Runnable continueExclusivelyAtStep(UseCase useCase, String stepName) {
		return () -> {
			UseCaseRunner useCaseRunner = useCase.useCaseModel().useCaseRunner();
			useCaseRunner.setExclusiveStepFilter(includeOnly(stepName));
			continueAtStep(useCase, stepName).run();
		};
	}
	private static Predicate<UseCaseStep> includeOnly(String stepName) {
		return step -> stepName.equals(step.name());
	}

	static Runnable continueAtStep(UseCase useCase, String stepName) {
		UseCaseStep continueAtStep = findStepOrThrow(useCase, stepName);
		Optional<UseCaseStep> continueAfterStep = continueAtStep.previousStepInFlow();
		
		return runnerContinueAfter(useCase, continueAfterStep);
	}
	
	static Runnable continueAfterStep(UseCase useCase, String stepName) {
		UseCaseStep continueAfterStep = findStepOrThrow(useCase, stepName);
		return runnerContinueAfter(useCase, Optional.of(continueAfterStep));
	}
	
	private static Runnable runnerContinueAfter(UseCase useCase, Optional<UseCaseStep> step) {
		return () -> {
			UseCaseRunner useCaseRunner = useCase.useCaseModel().useCaseRunner();
			useCaseRunner.setLatestStep(step);
		};
	}

	private static UseCaseStep findStepOrThrow(UseCase useCase, String stepName) {
		UseCaseStep step = useCase.findStep(stepName)
			.orElseThrow(() -> new NoSuchElementInUseCase(useCase, stepName));
		return step;
	}
}
