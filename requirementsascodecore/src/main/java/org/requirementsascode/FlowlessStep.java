package org.requirementsascode;

import java.util.function.Predicate;

public class FlowlessStep extends Step {
    private static final long serialVersionUID = -5290327128546502292L;

    FlowlessStep(String stepName, UseCase useCase) {
	super(stepName, useCase);
    }

    @Override
    public Predicate<ModelRunner> getPredicate() {
	Predicate<ModelRunner> predicate = toPredicate(getConditionOrElseTrue());
	return predicate;
    }

    private Condition getConditionOrElseTrue() {
	Condition condition = getCondition().orElse(() -> true);
	return condition;
    }
}
