package org.requirementsascode;

import java.util.function.Predicate;

public class InterruptingFlowStep extends FlowStep {
    private static final long serialVersionUID = 7204738737376844201L;

    InterruptingFlowStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
    }

    public Predicate<UseCaseModelRunner> getCondition() {
	Predicate<UseCaseModelRunner> condition;
	Predicate<UseCaseModelRunner> reactWhile = getReactWhile();

	if (reactWhile != null) {
	    condition = reactWhile;
	} else {
	    condition = isFlowConditionTrueAndRunnerInDifferentFlow();
	}

	return condition;
    }

    private Predicate<UseCaseModelRunner> isFlowConditionTrueAndRunnerInDifferentFlow() {
	Predicate<UseCaseModelRunner> flowPosition = getFlowPosition();
	Predicate<UseCaseModelRunner> when = getWhen().orElse(r -> true);

	Predicate<UseCaseModelRunner> flowCondition = isRunnerInDifferentFlow().and(flowPosition).and(when);
	return flowCondition;
    }

    private Predicate<UseCaseModelRunner> isRunnerInDifferentFlow() {
	Predicate<UseCaseModelRunner> isRunnerInDifferentFlow = runner -> runner.getLatestFlow()
		.map(runnerFlow -> !runnerFlow.equals(getFlow())).orElse(true);
	return isRunnerInDifferentFlow;
    }
}
