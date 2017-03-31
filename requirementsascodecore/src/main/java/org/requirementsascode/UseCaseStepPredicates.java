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
	
	public static Predicate<UseCaseRunner> afterStep(UseCaseStep afterThatStep) {
		return afterStep(Optional.of(afterThatStep));
	}
	
	public static Predicate<UseCaseRunner> afterStep(Optional<UseCaseStep> afterThatStepOrElseAtFirst) {		
		return useCaseRunner -> {
			Optional<UseCaseStep> stepRunLastBySystem = useCaseRunner.latestStep();
			boolean isSystemAtRightStep = 
				Objects.equals(afterThatStepOrElseAtFirst, stepRunLastBySystem);
			return isSystemAtRightStep;
		};
	}
	
	public static Predicate<UseCaseRunner> afterPreviousStepInFlowUnlessInterruptedByAlternativeTo(UseCaseStep currentStep) {
		Optional<UseCaseStep> previousStepInFlow = currentStep.previousStepInFlow();
		Predicate<UseCaseRunner> afterPreviousStep = afterStep(previousStepInFlow);
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
