package org.requirementsascode;

import java.util.function.Predicate;

import org.requirementsascode.condition.Condition;

public class InterruptingFlowStep extends FlowStep {
    private static final long serialVersionUID = 7204738737376844201L;

    InterruptingFlowStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
    }

    public Predicate<ModelRunner> getPredicate() {
	Predicate<ModelRunner> predicate;
	Predicate<ModelRunner> reactWhile = getReactWhile();

	predicate = isFlowConditionTrueAndRunnerInDifferentFlow();
	if (reactWhile != null) {
	    predicate = predicate.and(reactWhile);
	}

	return predicate;
    }

    private Predicate<ModelRunner> isFlowConditionTrueAndRunnerInDifferentFlow() {
	Predicate<ModelRunner> flowPosition = getFlowPosition();
	Condition when = getWhen().orElse(() -> true);

	Predicate<ModelRunner> flowCondition = isRunnerInDifferentFlow().and(flowPosition).and(toPredicate(when));
	return flowCondition;
    }

    private Predicate<ModelRunner> isRunnerInDifferentFlow() {
	Predicate<ModelRunner> isRunnerInDifferentFlow = runner -> runner.getLatestFlow()
		.map(runnerFlow -> !runnerFlow.equals(getFlow())).orElse(true);
	return isRunnerInDifferentFlow;
    }
}
