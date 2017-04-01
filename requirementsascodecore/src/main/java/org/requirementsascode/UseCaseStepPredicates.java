package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Class that defines several common predicates, especially for handling
 * alternative flows and step conditions.
 * 
 * @author b_muth
 *
 */
public class UseCaseStepPredicates {	
	public static Predicate<UseCaseRunner> isRunnerInDifferentFlowThan(UseCaseFlow useCaseFlow) {
		Objects.requireNonNull(useCaseFlow);
		
		Predicate<UseCaseRunner> isRunnerInDifferentFlow = 
			useCaseRunner -> useCaseRunner.latestFlow().map(
				runnerFlow -> !useCaseFlow.equals(runnerFlow)).orElse(true);
		return isRunnerInDifferentFlow;
	}
	
	public static Predicate<UseCaseRunner> afterPreviousStepUnlessOtherStepCouldReact(UseCaseStep currentStep) {
		Optional<UseCaseStep> previousStepInFlow = currentStep.previousStepInFlow();
		Predicate<UseCaseRunner> afterPreviousStep = new After(previousStepInFlow);
		return afterPreviousStep.and(noOtherStepCouldReactThan(currentStep));
	}
	private static Predicate<UseCaseRunner> noOtherStepCouldReactThan(UseCaseStep theStep) {
		return useCaseRunner -> {
			Class<?> theStepsEventClass = theStep.getEventClass();
			UseCaseModel useCaseModel = theStep.useCaseModel();
			
			Stream<UseCaseStep> otherStepsStream = 
				useCaseModel.steps().stream()
					.filter(step -> !step.equals(theStep));
			
			Set<UseCaseStep> otherStepsThatCouldReact = useCaseRunner.stepsInStreamThatCanReactTo(theStepsEventClass, otherStepsStream);
			return otherStepsThatCouldReact.size() == 0;
		};
	}
}
