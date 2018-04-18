package org.requirementsascode;

import java.util.function.Predicate;

import org.requirementsascode.predicate.Anytime;

public class FlowlessStep extends Step {
    private static final long serialVersionUID = -5290327128546502292L;

    FlowlessStep(String stepName, UseCase useCase) {
	super(stepName, useCase);
    }

    @Override
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
	Anytime anytime = new Anytime();
	Predicate<UseCaseModelRunner> when = getWhen().orElse(anytime);
	return when;
    }
}
