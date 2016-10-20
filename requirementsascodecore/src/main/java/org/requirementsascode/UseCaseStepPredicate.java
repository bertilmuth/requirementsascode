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
class UseCaseStepPredicate {
	private UseCaseStepPredicate(){};
	
	public static Predicate<UseCaseRunner> isRunnerInDifferentFlowThan(UseCaseFlow useCaseFlow) {
		Objects.requireNonNull(useCaseFlow);
		
		Predicate<UseCaseRunner> isRunnerInDifferentFlow = 
			useCaseRunner -> useCaseRunner.getLatestFlow().map(
				runnerFlow -> !useCaseFlow.equals(runnerFlow)).orElse(true);
		return isRunnerInDifferentFlow;
	}
	
	public static Predicate<UseCaseRunner> isRunnerAtStart() {
		return afterStep(Optional.empty());
	}
	
	public static Predicate<UseCaseRunner> afterStep(UseCaseStep afterThatStep) {
		return afterStep(Optional.of(afterThatStep));
	}
	
	private static Predicate<UseCaseRunner> afterStep(Optional<UseCaseStep> afterThatStepOrElseAtFirst) {		
		return useCaseRunner -> {
			Optional<UseCaseStep> stepRunLastBySystem = useCaseRunner.getLatestStep();
			boolean isSystemAtRightStep = 
				Objects.equals(stepRunLastBySystem, afterThatStepOrElseAtFirst);
			return isSystemAtRightStep;
		};
	}
	
	public static Predicate<UseCaseRunner> noOtherStepIsEnabledThan(UseCaseStep theStep) {
		return useCaseRunner -> {
			Class<?> theStepsEventClass = theStep.getEventPart().getEventClass();
			UseCaseModel useCaseModel = theStep.getUseCaseModel();
			
			Stream<UseCaseStep> otherStepsStream = 
				useCaseModel.getSteps().stream()
					.filter(step -> !step.equals(theStep));
			
			Set<UseCaseStep> enabledOtherSteps = useCaseRunner.getSubsetOfStepsThatCouldReact(theStepsEventClass, otherStepsStream);
			return enabledOtherSteps.size() == 0;
		};
	}
}
