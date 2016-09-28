package org.requirementsascode;

import java.util.Objects;
import java.util.function.Predicate;

public class UseCaseStepCondition {
	private UseCaseStepCondition(){};
	
	public static Predicate<UseCaseModelRun> isSystemInDifferentFlowThan(UseCaseFlow useCaseFlow) {
		Objects.requireNonNull(useCaseFlow);
		
		Predicate<UseCaseModelRun> isSystemInDifferentFlow = 
			useCaseModelRun -> !useCaseFlow.equals(useCaseModelRun.getLatestFlow());
		return isSystemInDifferentFlow;
	}
	
	public static Predicate<UseCaseModelRun> atFirstStep() {
		return afterStep(null);
	}
	
	public static Predicate<UseCaseModelRun> afterStep(UseCaseStep afterThatStep) {		
		return useCaseModelRun -> {
			UseCaseStep stepRunLastBySystem = useCaseModelRun.getLatestStep();
			boolean isSystemAtRightStep = Objects.equals(stepRunLastBySystem, afterThatStep);
			return isSystemAtRightStep;
		};
	}
}
