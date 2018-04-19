package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;

import org.requirementsascode.predicate.FlowPosition;

/**
 * @author b_muth
 */
public abstract class FlowStep extends Step implements Serializable {
    private static final long serialVersionUID = -2926490717985964131L;

    private Flow flow;
    private FlowPosition flowPosition;

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
    }

    void setFlowPosition(FlowPosition flowPosition) {
	this.flowPosition = flowPosition;
    }

    public FlowPosition getFlowPosition() {
	return flowPosition;
    }

    public void includeUseCase(UseCase includedUseCase) {
	for (Flow includedFlow : includedUseCase.getFlows()) {
	    includeFlow(includedFlow);
	}
    }

    private void includeFlow(Flow includedFlow) {
	Optional<FlowStep> optionalFirstStepOfIncludedFlow = includedFlow.getFirstStep();
	optionalFirstStepOfIncludedFlow.ifPresent(this::afterThisStepComesIncludedFlow);
    }

    private void afterThisStepComesIncludedFlow(FlowStep firstStepOfIncludedFlow) {
	FlowPosition includedFlowPosition = firstStepOfIncludedFlow.getFlowPosition();
	firstStepOfIncludedFlow.setFlowPosition(includedFlowPosition.orAfter(this));
    }
}
