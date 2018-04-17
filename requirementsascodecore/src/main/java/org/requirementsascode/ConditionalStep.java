package org.requirementsascode;

import java.util.function.Predicate;

public class ConditionalStep extends Step {
    private static final long serialVersionUID = 7204738737376844201L;

    ConditionalStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
    }
    
    public Predicate<UseCaseModelRunner> getPredicate() {
	Predicate<UseCaseModelRunner> predicate;

	if (reactWhile != null) {
	    predicate = reactWhile;
	} else{ 
	    predicate = getFlowPredicate().get();
	}

	return predicate;
    }
}
