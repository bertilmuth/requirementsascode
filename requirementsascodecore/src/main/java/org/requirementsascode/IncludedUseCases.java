package org.requirementsascode;

import java.util.LinkedList;
import java.util.List;

class IncludedUseCases {
    private LinkedList<UseCase> includedUseCases;
    private LinkedList<FlowStep> includeSteps;
    private UseCase includedUseCase;
    private FlowStep includeStep;

    public IncludedUseCases() {
	this.includedUseCases = new LinkedList<>();
	this.includeSteps = new LinkedList<>();
	this.includedUseCase = null;
	this.includeStep = null;
    }

    void startIncludedUseCase(UseCase includedUseCase, FlowStep includeStep) {
	this.includedUseCase = includedUseCase;
	this.includeStep = includeStep;

	includedUseCases.push(includedUseCase);
	includeSteps.push(includeStep);
    }

    boolean isStepInIncludedUseCaseIfPresent(Step step) {
	boolean result = true;
	if (includedUseCase != null) {
	    result = includedUseCase.equals(step.getUseCase());
	}
	return result;
    }

    void continueAfterIncludeStepWhenEndOfIncludedFlowIsReached(ModelRunner modelRunner) {
	if (includeStep != null) {
	    Step latestStep = modelRunner.getLatestStep().get();
	    if (isAtEndOfIncludedFlow(latestStep)) {
		modelRunner.setLatestStep(includeStep);
		returnToIncludeStep();
	    }
	}
    }

    UseCase getUseCaseIncludedBefore() {
	includedUseCases.pop();
	UseCase includedUseCase = includedUseCases.peek();
	return includedUseCase;
    }

    FlowStep getIncludeStep() {
	return includeStep;
    }

    FlowStep getIncludeStepBefore() {
	includeSteps.pop();
	FlowStep includeStep = includeSteps.peek();
	return includeStep;
    }

    boolean isAtEndOfIncludedFlow(Step latestStep) {
	if (includedUseCase == null || includeStep == null) {
	    return false;
	}

	FlowStep lastStepOfRunningFlow = getLastStepOf(((FlowStep) latestStep).getFlow());
	boolean result = latestStep.getUseCase().equals(includedUseCase) && latestStep.equals(lastStepOfRunningFlow);
	return result;
    }

    void returnToIncludeStep() {
	includedUseCase = getUseCaseIncludedBefore();
	includeStep = getIncludeStepBefore();
    }

    private FlowStep getLastStepOf(Flow flow) {
	List<FlowStep> stepsOfFlow = flow.getSteps();
	int lastStepIndex = stepsOfFlow.size() - 1;
	FlowStep lastStepOfFlow = stepsOfFlow.get(lastStepIndex);
	return lastStepOfFlow;
    }
}
