package org.requirementsascode.systemreaction;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;

import org.requirementsascode.Flow;
import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public class IncludesUseCase implements Consumer<ModelRunner>, Serializable {
    private static final long serialVersionUID = -9078568632090369442L;

    private UseCase includedUseCase;
    private FlowStep includeStep;

    public IncludesUseCase(UseCase includedUseCase, FlowStep includeStep) {
	this.includedUseCase = includedUseCase;
	this.includeStep = includeStep;
	enableStartOfIncludedUseCase();
    }

    @Override
    public void accept(ModelRunner runner) {
	runner.startIncludedUseCase(includedUseCase, includeStep);
    }

    public UseCase getIncludedUseCase() {
	return includedUseCase;
    }

    public void enableStartOfIncludedUseCase() {
	for (Flow includedFlow : includedUseCase.getFlows()) {
	    enableStartOfIncludedFlow(includedFlow);
	}
    }

    private void enableStartOfIncludedFlow(Flow includedFlow) {
	Optional<FlowStep> optionalFirstStepOfIncludedFlow = includedFlow.getFirstStep();
	optionalFirstStepOfIncludedFlow.ifPresent(this::enableStartOfFirstStepOfIncludedFlowAfterIncludeStep);
    }

    private void enableStartOfFirstStepOfIncludedFlowAfterIncludeStep(FlowStep firstStepOfIncludedFlow) {
	firstStepOfIncludedFlow.orAfter(includeStep);
    }
}
