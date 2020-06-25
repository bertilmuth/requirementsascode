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

	private List<FlowStep> mergeSteps;
	private FlowStep step;

	protected abstract boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner);

	public FlowPosition(FlowStep step) {
		this.step = step;
		this.mergeSteps = new ArrayList<>();
	}

	@Override
	public final boolean test(ModelRunner modelRunner) {
		boolean isRunnerAtRightPositionForStepOrAfterAnyMergedStep = isRunnerAtRightPositionFor(step, modelRunner)
			|| afterAnyMergedStep(modelRunner);
		return isRunnerAtRightPositionForStepOrAfterAnyMergedStep;
	}

	private boolean afterAnyMergedStep(ModelRunner modelRunner) {
		boolean isAfterStep = false;
		for (FlowStep mergeStep : mergeSteps) {
			if(new After(mergeStep).test(modelRunner)) {
				isAfterStep = true;
				break;
			}
		}
		return isAfterStep;
	}

	public final Step getStep() {
		return step;
	}

	public FlowPosition orAfter(FlowStep mergeStep) {
		mergeSteps.add(mergeStep);
		return this;
	}
}
