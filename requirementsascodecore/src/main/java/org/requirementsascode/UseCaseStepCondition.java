package org.requirementsascode;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UseCaseStepCondition {
	private UseCaseStepCondition(){};
	
	public static Predicate<UseCaseRunner> isRunnerInDifferentFlowThan(UseCaseFlow useCaseFlow) {
		Objects.requireNonNull(useCaseFlow);
		
		Predicate<UseCaseRunner> isRunnerInDifferentFlow = 
			useCaseModelRunner -> !useCaseFlow.equals(useCaseModelRunner.getLatestFlow());
		return isRunnerInDifferentFlow;
	}
	
	public static Predicate<UseCaseRunner> atFirstStep() {
		return afterStep(null);
	}
	
	public static Predicate<UseCaseRunner> afterStep(UseCaseStep afterThatStep) {		
		return useCaseModelRunner -> {
			UseCaseStep stepRunLastBySystem = useCaseModelRunner.getLatestStep();
			boolean isSystemAtRightStep = Objects.equals(stepRunLastBySystem, afterThatStep);
			return isSystemAtRightStep;
		};
	}
	
	public static Predicate<UseCaseRunner> noOtherStepIsEnabledThan(UseCaseStep theStep) {
		return useCaseModelRunner -> {
			Class<?> theStepsEventClass = theStep.getActorPart().getEventClass();
			UseCaseModel useCaseModel = theStep.getModel();
			
			Stream<UseCaseStep> otherStepsStream = 
				useCaseModel.getUseCaseSteps().stream()
					.filter(step -> !step.equals(theStep));
			
			Set<UseCaseStep> enabledOtherSteps = useCaseModelRunner.getEnabledStepSubset(theStepsEventClass, otherStepsStream);
			return enabledOtherSteps.size() == 0;
		};
	}
}
