package org.requirementsascode.flowposition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.requirementsascode.FlowStep;
import org.requirementsascode.Step;
import org.requirementsascode.ModelRunner;

public abstract class FlowPosition implements Predicate<ModelRunner>, Serializable {
	private static final long serialVersionUID = -8952890128132543927L;

	private List<FlowStep> orAfterSteps;
	private FlowStep step;

	protected abstract boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner);

	public FlowPosition(FlowStep step) {
		this.step = step;
		this.orAfterSteps = new ArrayList<>();
	}

	@Override
	public final boolean test(ModelRunner modelRunner) {
		boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step, modelRunner)
			|| afterAnyMergedStep(modelRunner);
		return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
	}

	private boolean afterAnyMergedStep(ModelRunner modelRunner) {
		return orAfterSteps.stream().anyMatch(step -> new After(step).test(modelRunner));
	}

	public final Step getStep() {
		return step;
	}

	public FlowPosition orAfter(FlowStep mergeStep) {
		orAfterSteps.add(mergeStep);
		return this;
	}
}
