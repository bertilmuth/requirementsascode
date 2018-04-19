package org.requirementsascode.predicate;

import java.io.Serializable;
import java.util.Objects;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class After extends FlowPosition implements Serializable {
    private static final long serialVersionUID = -4951912635216926005L;

    public After(Step step) {
	super(step);
    }
    
    @Override
    public boolean isRunnerAtRightPositionFor(Step step, UseCaseModelRunner useCaseModelRunner) {
        Step latestStepRun = useCaseModelRunner.getLatestStep().orElse(null);
	boolean stepWasRunLast = Objects.equals(step, latestStepRun);
	return stepWasRunLast;
    }

    @Override
    public String getStepName(Step step) {
	String name = step != null ? step.getName() : "";
	return name;
    }
}
