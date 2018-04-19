package org.requirementsascode;

import java.util.function.Predicate;

public class InterruptingFlowStep extends FlowStep {
    private static final long serialVersionUID = 7204738737376844201L;

    InterruptingFlowStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
    }

    public Predicate<UseCaseModelRunner> getPredicate() {
	Predicate<UseCaseModelRunner> predicate;
	Predicate<UseCaseModelRunner> reactWhile = getReactWhile();

	if (reactWhile != null) {
	    predicate = reactWhile;
	} else {
	    predicate = getFlowPredicate();
	}

	return predicate;
    }

    private Predicate<UseCaseModelRunner> getFlowPredicate() {
	Predicate<UseCaseModelRunner> flowPosition = getFlowPosition();
	Predicate<UseCaseModelRunner> when = getWhen().orElse(r -> true);

	Predicate<UseCaseModelRunner> flowPredicate = isRunnerInDifferentFlow().and(flowPosition).and(when);
	return flowPredicate;
    }

    private Predicate<UseCaseModelRunner> isRunnerInDifferentFlow() {
	Predicate<UseCaseModelRunner> isRunnerInDifferentFlow = runner -> runner.getLatestFlow()
		.map(runnerFlow -> !runnerFlow.equals(getFlow())).orElse(true);
	return isRunnerInDifferentFlow;
    }
}
