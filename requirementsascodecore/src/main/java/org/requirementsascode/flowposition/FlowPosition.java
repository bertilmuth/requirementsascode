package org.requirementsascode.flowposition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.FlowStep;
import org.requirementsascode.Step;
import org.requirementsascode.ModelRunner;

public abstract class FlowPosition implements Predicate<ModelRunner> {
    private List<FlowStep> orAfterSteps;
    private Step step;

    protected abstract boolean isRunnerAtRightPositionFor(Step step, ModelRunner useCaseModelRunner);

    public FlowPosition(Step step) {
	this.step = step;
	this.orAfterSteps = new ArrayList<>();
    }

    @Override
    public final boolean test(ModelRunner useCaseModelRunner) {
	boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step,
		useCaseModelRunner) || orAfterSteps.stream().anyMatch(step -> new After(step).test(useCaseModelRunner));
	return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
    }

    public final String getStepName() {
	String name = step != null ? step.getName() : "";
	return name;
    }

    public FlowPosition orAfter(FlowStep mergeStep) {
	orAfterSteps.add(mergeStep);
	return this;
    }
}
