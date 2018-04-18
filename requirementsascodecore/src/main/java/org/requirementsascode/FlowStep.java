package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.predicate.After;

/**
 * @author b_muth
 */
public abstract class FlowStep extends Step implements Serializable {
    private static final long serialVersionUID = -2926490717985964131L;

    private Flow flow;
    private Predicate<UseCaseModelRunner> flowPosition;

    private FlowStep previousStepInFlow;

    FlowStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase);
	this.flow = useCaseFlow;
    }

    public Flow getFlow() {
	return flow;
    }

    public Optional<FlowStep> getPreviousStepInFlow() {
	return Optional.ofNullable(previousStepInFlow);
    }

    protected void setPreviousStepInFlow(FlowStep previousStepInFlow) {
	this.previousStepInFlow = previousStepInFlow;
	setFlowPosition(new After(previousStepInFlow));
    }

    void setFlowPosition(Predicate<UseCaseModelRunner> flowPosition) {
	this.flowPosition = flowPosition;
    }

    public Optional<Predicate<UseCaseModelRunner>> getFlowPosition() {
	return Optional.ofNullable(flowPosition);
    }

    public void includeUseCase(UseCase includedUseCase) {
	for (Flow includedFlow : includedUseCase.getFlows()) {
	    includeFlow(includedFlow);
	}
    }

    private void includeFlow(Flow includedFlow) {
	Optional<FlowStep> optionalFirstStepOfIncludedFlow = includedFlow.getFirstStep();
	optionalFirstStepOfIncludedFlow.ifPresent(this::includeStep);
    }

    private void includeStep(FlowStep firstStepOfIncludedFlow) {
	Predicate<UseCaseModelRunner> originalFlowPosition = firstStepOfIncludedFlow.getFlowPosition()
		.orElse(r -> false);
	Predicate<UseCaseModelRunner> newFlowPosition = new After(this).or(originalFlowPosition);
	firstStepOfIncludedFlow.setFlowPosition(newFlowPosition);
    }
}
