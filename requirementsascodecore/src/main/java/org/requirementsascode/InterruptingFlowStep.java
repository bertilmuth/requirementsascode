package org.requirementsascode;

import java.util.function.Predicate;

import org.requirementsascode.flowposition.FlowPosition;

public class InterruptingFlowStep extends FlowStep {
	InterruptingFlowStep(String stepName, Flow useCaseFlow, FlowPosition flowPosition, Condition condition) {
		super(stepName, useCaseFlow, condition);
		setFlowPosition(flowPosition);
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

		Predicate<ModelRunner> flowCondition = isRunnerInDifferentFlow().and(flowPosition)
				.and(isConditionTrue());
		return flowCondition;
	}

	private Predicate<ModelRunner> isRunnerInDifferentFlow() {
		Predicate<ModelRunner> isRunnerInDifferentFlow = runner -> runner.getLatestFlow()
				.map(runnerFlow -> !runnerFlow.equals(getFlow())).orElse(true);
		return isRunnerInDifferentFlow;
	}
}
