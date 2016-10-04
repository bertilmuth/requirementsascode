package org.requirementsascode;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UseCaseStepCondition {
	private UseCaseStepCondition(){};
	
	public static Predicate<UseCaseRunner> isSystemInDifferentFlowThan(UseCaseFlow useCaseFlow) {
		Objects.requireNonNull(useCaseFlow);
		
		Predicate<UseCaseRunner> isSystemInDifferentFlow = 
			useCaseModelRun -> !useCaseFlow.equals(useCaseModelRun.getLatestFlow());
		return isSystemInDifferentFlow;
	}
	
	public static Predicate<UseCaseRunner> atFirstStep() {
		return afterStep(null);
	}
	
	public static Predicate<UseCaseRunner> afterStep(UseCaseStep afterThatStep) {		
		return useCaseModelRun -> {
			UseCaseStep stepRunLastBySystem = useCaseModelRun.getLatestStep();
			boolean isSystemAtRightStep = Objects.equals(stepRunLastBySystem, afterThatStep);
			return isSystemAtRightStep;
		};
	}
	
	public static Predicate<UseCaseRunner> noOtherStepIsEnabledThan(UseCaseStep theStep) {
		return run -> {
			Class<?> theStepsEventClass = theStep.getActorPart().getEventClass();
			UseCaseModel useCaseModel = theStep.getModel();
			
			Stream<UseCaseStep> otherStepsStream = 
				useCaseModel.getUseCaseSteps().stream()
					.filter(step -> !step.equals(theStep));
			
			Set<UseCaseStep> enabledOtherSteps = run.getEnabledStepSubset(theStepsEventClass, otherStepsStream);
			return enabledOtherSteps.size() == 0;
		};
	}
}
