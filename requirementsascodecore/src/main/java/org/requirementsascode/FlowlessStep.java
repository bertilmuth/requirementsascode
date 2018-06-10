package org.requirementsascode;

import java.util.function.Predicate;

import org.requirementsascode.condition.Condition;

public class FlowlessStep extends Step {
    private static final long serialVersionUID = -5290327128546502292L;

    FlowlessStep(String stepName, UseCase useCase) {
	super(stepName, useCase);
    }

    @Override
    public Predicate<ModelRunner> getPredicate() {
	Predicate<ModelRunner> predicate = toPredicate(getFlowCondition());
	return predicate;
    }

    private Condition getFlowCondition() {
	Condition when = getWhen().orElse(() -> true);
	return when;
    }
}
