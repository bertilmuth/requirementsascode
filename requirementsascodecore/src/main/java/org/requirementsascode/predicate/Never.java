package org.requirementsascode.predicate;

import java.io.Serializable;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class Never extends FlowPosition implements Serializable {
    private static final long serialVersionUID = -4100750563505946022L;

    
    public Never() {
	super(null);
    }

    @Override
    protected boolean isRunnerAtRightPositionFor(Step step, UseCaseModelRunner useCaseModelRunner) {
	return false;
    }

    @Override
    protected String getStepName(Step step) {
	return "";
    }
}
