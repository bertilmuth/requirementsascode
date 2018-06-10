package org.requirementsascode.condition;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.flowposition.FlowPosition;

public class ReactWhile implements Predicate<ModelRunner>, Serializable {
    private static final long serialVersionUID = -3190093346311188647L;

    private Predicate<ModelRunner> reactWhileCondition;

    public ReactWhile(FlowStep step, Predicate<ModelRunner> reactWhileCondition) {
	Objects.requireNonNull(step);
	Objects.requireNonNull(reactWhileCondition);
	createReactWhileCondition(step, reactWhileCondition);
    }
    
    @Override
    public boolean test(ModelRunner modelRunner) {
	return reactWhileCondition.test(modelRunner);
    }

    private void createReactWhileCondition(FlowStep step, Predicate<ModelRunner> reactWhileCondition) {
	createLoop(step);
	this.reactWhileCondition = reactWhileCondition;
    }

    private void createLoop(FlowStep step) {
	FlowPosition flowPosition = step.getFlowPosition();
	flowPosition.orAfter(step);
    }
}
