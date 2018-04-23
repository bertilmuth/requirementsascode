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

    protected abstract boolean isRunnerAtRightPositionFor(Step step, ModelRunner modelRunner);

    public FlowPosition(Step step) {
	this.step = step;
	this.orAfterSteps = new ArrayList<>();
    }

    @Override
    public final boolean test(ModelRunner modelRunner) {
	boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step,
		modelRunner) || orAfterSteps.stream().anyMatch(step -> new After(step).test(modelRunner));
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
