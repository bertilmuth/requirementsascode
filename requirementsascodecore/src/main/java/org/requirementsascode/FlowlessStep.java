package org.requirementsascode;

import java.util.function.Predicate;

import org.requirementsascode.flowposition.Anytime;

public class FlowlessStep extends Step {
    private static final long serialVersionUID = -5290327128546502292L;

    FlowlessStep(String stepName, UseCase useCase) {
	super(stepName, useCase);
    }

    @Override
    public Predicate<UseCaseModelRunner> getCondition() {
	Predicate<UseCaseModelRunner> condition;
	Predicate<UseCaseModelRunner> reactWhile = getReactWhile();

	if (reactWhile != null) {
	    condition = reactWhile;
	} else {
	    condition = getFlowCondition();
	}

	return condition;
    }

    private Predicate<UseCaseModelRunner> getFlowCondition() {
	Anytime anytime = new Anytime();
	Predicate<UseCaseModelRunner> when = getWhen().orElse(anytime);
	return when;
    }
}
