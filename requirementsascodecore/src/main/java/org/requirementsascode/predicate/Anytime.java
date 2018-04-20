package org.requirementsascode.predicate;

import java.io.Serializable;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class Anytime extends FlowPosition implements Serializable {
    private static final long serialVersionUID = 7724607380865304333L;

    public Anytime() {
	super(null);
    }


    @Override
    protected boolean isRunnerAtRightPositionFor(Step step, UseCaseModelRunner useCaseModelRunner) {
	return true;
    }
}
