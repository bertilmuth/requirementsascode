package org.requirementsascode;

public class ConditionalStep extends Step {
    private static final long serialVersionUID = 7204738737376844201L;

    ConditionalStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
    }
}
