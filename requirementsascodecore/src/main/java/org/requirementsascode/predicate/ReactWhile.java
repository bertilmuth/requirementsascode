package org.requirementsascode.predicate;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

import org.requirementsascode.FlowStep;
import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

public class ReactWhile extends FlowPosition implements Serializable {
    private static final long serialVersionUID = -3190093346311188647L;

    private Predicate<UseCaseModelRunner> completeCondition;
    private Predicate<UseCaseModelRunner> reactWhileCondition;

    public ReactWhile(FlowStep step, Predicate<UseCaseModelRunner> reactWhileCondition) {
	super(step);
	Objects.requireNonNull(reactWhileCondition);
	createReactWhileCondition(step, reactWhileCondition);
    }
    
    @Override
    protected boolean isRunnerAtRightPositionFor(Step step, UseCaseModelRunner useCaseModelRunner) {
	return completeCondition.test(useCaseModelRunner);
    }

    private void createReactWhileCondition(FlowStep step, Predicate<UseCaseModelRunner> reactWhileCondition) {
	completeCondition = step.getPredicate().and(reactWhileCondition);
	createLoop(step);
	this.reactWhileCondition = reactWhileCondition;
    }

    private void createLoop(FlowStep step) {
	FlowPosition flowPosition = step.getFlowPosition();
	flowPosition.orAfter(step);
    }

    public Predicate<UseCaseModelRunner> getReactWhileCondition() {
	return reactWhileCondition;
    }


}
