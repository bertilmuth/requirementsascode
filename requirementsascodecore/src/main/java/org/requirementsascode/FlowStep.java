package org.requirementsascode;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.requirementsascode.flowposition.FlowPosition;

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

    void setPreviousStepInFlow(FlowStep previousStepInFlow) {
	this.previousStepInFlow = previousStepInFlow;
    }
    
    public FlowPosition getFlowPosition() {
	return flowPosition;
    }

    void setFlowPosition(FlowPosition flowPosition) {
	Objects.requireNonNull(flowPosition);
	
	this.flowPosition = flowPosition;
    }
    
    public void orAfter(FlowStep step) {
	setFlowPosition(flowPosition.orAfter(step));
    }

    public void includeUseCase(UseCase includedUseCase) {
	Objects.requireNonNull(flowPosition);
	
	for (Flow includedFlow : includedUseCase.getFlows()) {
	    includeFlow(includedFlow);
	}
    }

    private void includeFlow(Flow includedFlow) {
	Optional<FlowStep> optionalFirstStepOfIncludedFlow = includedFlow.getFirstStep();
	optionalFirstStepOfIncludedFlow.ifPresent(this::afterThisStepComesIncludedFlow);
    }

    private void afterThisStepComesIncludedFlow(FlowStep firstStepOfIncludedFlow) {
	firstStepOfIncludedFlow.orAfter(this);
    }
}
