package org.requirementsascode;

import java.util.function.Predicate;

public class InterruptingFlowStep extends FlowStep {
    private static final long serialVersionUID = 7204738737376844201L;

    InterruptingFlowStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
    }

    public Predicate<ModelRunner> getCondition() {
	Predicate<ModelRunner> condition;
	Predicate<ModelRunner> reactWhile = getReactWhile();

	if (reactWhile != null) {
	    condition = reactWhile;
	} else {
	    condition = isFlowConditionTrueAndRunnerInDifferentFlow();
	}

	return condition;
    }

    private Predicate<ModelRunner> isFlowConditionTrueAndRunnerInDifferentFlow() {
	Predicate<ModelRunner> flowPosition = getFlowPosition();
	Predicate<ModelRunner> when = getWhen().orElse(r -> true);

	Predicate<ModelRunner> flowCondition = isRunnerInDifferentFlow().and(flowPosition).and(when);
	return flowCondition;
    }

    private Predicate<ModelRunner> isRunnerInDifferentFlow() {
	Predicate<ModelRunner> isRunnerInDifferentFlow = runner -> runner.getLatestFlow()
		.map(runnerFlow -> !runnerFlow.equals(getFlow())).orElse(true);
	return isRunnerInDifferentFlow;
    }
}
