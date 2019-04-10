package org.requirementsascode;

import java.util.function.Predicate;

public class InterruptingFlowStep extends FlowStep {
    private static final long serialVersionUID = 7204738737376844201L;

    InterruptingFlowStep(String stepName, Flow useCaseFlow) {
	super(stepName, useCaseFlow);
    }

    public Predicate<ModelRunner> getPredicate() {
	Predicate<ModelRunner> predicate;
	Condition reactWhile = getReactWhile();

	predicate = isFlowConditionTrueAndRunnerInDifferentFlow();
	if (reactWhile != null) {
	    predicate = predicate.and(toPredicate(reactWhile));
	}

	return predicate;
    }

    private Predicate<ModelRunner> isFlowConditionTrueAndRunnerInDifferentFlow() {
	Predicate<ModelRunner> flowPosition = getFlowPosition();
	Condition conditionOrElseTrue = getCondition().orElse(() -> true);

	Predicate<ModelRunner> flowCondition = isRunnerInDifferentFlow().and(flowPosition).and(toPredicate(conditionOrElseTrue));
	return flowCondition;
    }

    private Predicate<ModelRunner> isRunnerInDifferentFlow() {
	Predicate<ModelRunner> isRunnerInDifferentFlow = runner -> runner.getLatestFlow()
		.map(runnerFlow -> !runnerFlow.equals(getFlow())).orElse(true);
	return isRunnerInDifferentFlow;
    }
}
