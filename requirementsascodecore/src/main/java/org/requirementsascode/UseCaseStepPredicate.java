package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UseCaseStepPredicate {
	private UseCaseStepPredicate(){};
	
	public static Predicate<UseCaseRunner> isRunnerInDifferentFlowThan(UseCaseFlow useCaseFlow) {
		Objects.requireNonNull(useCaseFlow);
		
		Predicate<UseCaseRunner> isRunnerInDifferentFlow = 
			useCaseRunner -> useCaseRunner.getLatestFlow().map(
				runnerFlow -> !useCaseFlow.equals(runnerFlow)).orElse(true);
		return isRunnerInDifferentFlow;
	}
	
	public static Predicate<UseCaseRunner> atFirstStep() {
		return afterStep(Optional.empty());
	}
	
	public static Predicate<UseCaseRunner> afterStep(UseCaseStep afterThatStep) {
		return afterStep(Optional.of(afterThatStep));
	}
	
	private static Predicate<UseCaseRunner> afterStep(Optional<UseCaseStep> afterThatStep) {		
		return useCaseRunner -> {
			Optional<UseCaseStep> stepRunLastBySystem = useCaseRunner.getLatestStep();
			boolean isSystemAtRightStep = 
				Objects.equals(stepRunLastBySystem, afterThatStep);
			return isSystemAtRightStep;
		};
	}
	
	public static Predicate<UseCaseRunner> noOtherStepIsEnabledThan(UseCaseStep theStep) {
		return useCaseRunner -> {
			Class<?> theStepsEventClass = theStep.getActorPart().getEventClass();
			UseCaseModel useCaseModel = theStep.getUseCaseModel();
			
			Stream<UseCaseStep> otherStepsStream = 
				useCaseModel.getUseCaseSteps().stream()
					.filter(step -> !step.equals(theStep));
			
			Set<UseCaseStep> enabledOtherSteps = useCaseRunner.getEnabledStepSubset(theStepsEventClass, otherStepsStream);
			return enabledOtherSteps.size() == 0;
		};
	}
}
