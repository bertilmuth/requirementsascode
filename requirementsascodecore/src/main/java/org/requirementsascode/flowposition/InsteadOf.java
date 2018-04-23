package org.requirementsascode.flowposition;

import java.io.Serializable;

import org.requirementsascode.FlowStep;
import org.requirementsascode.Step;
import org.requirementsascode.ModelRunner;

public class InsteadOf extends FlowPosition implements Serializable {
    private static final long serialVersionUID = -3958653686352185075L;

    public InsteadOf(FlowStep step) {
	super(step);
    }

    @Override
    public boolean isRunnerAtRightPositionFor(Step step, ModelRunner useCaseModelRunner) {
	FlowStep previousStep = ((FlowStep)step).getPreviousStepInFlow().orElse(null);
	After after = new After(previousStep);
	return after.test(useCaseModelRunner);
    }
}