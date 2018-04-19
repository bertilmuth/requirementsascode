package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;

import org.requirementsascode.predicate.After;
import org.requirementsascode.predicate.FlowPosition;
import org.requirementsascode.predicate.Never;

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
	setFlowPosition(new After(previousStepInFlow));
    }

    void setFlowPosition(FlowPosition flowPosition) {
	this.flowPosition = flowPosition;
    }

    public Optional<FlowPosition> getFlowPosition() {
	return Optional.ofNullable(flowPosition);
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
	// The included flow position if there is one, or else "never" (because included flows don't start themselves by default)
	FlowPosition includedFlowPosition = firstStepOfIncludedFlow.getFlowPosition().orElse(new Never());
	includedFlowPosition.mergeAfter(this);
    }
}
