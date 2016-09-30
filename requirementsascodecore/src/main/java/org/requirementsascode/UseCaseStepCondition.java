package org.requirementsascode;

import java.util.Objects;
import java.util.function.Predicate;

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
}
