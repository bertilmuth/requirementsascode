package org.requirementsascode.predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.FlowStep;
import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public abstract class FlowPosition implements Predicate<UseCaseModelRunner> {
    private List<FlowStep> mergeSteps;
    private Step step;
    
    protected abstract boolean isRunnerAtRightPositionFor(Step step, UseCaseModelRunner useCaseModelRunner);
    protected abstract String getStepName(Step step);

    public FlowPosition(Step step) {
	this.step = step;
	this.mergeSteps = new ArrayList<>();
    }

    @Override
    public final boolean test(UseCaseModelRunner useCaseModelRunner) {
	boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step, useCaseModelRunner)
		|| mergeSteps.stream().anyMatch(step -> new After(step).test(useCaseModelRunner));
	return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
    }

    public final String getStepName() {
	return getStepName(step);
    }

    public void mergeAfter(FlowStep mergeStep) {
	mergeSteps.add(mergeStep);
    }
}
