package org.requirementsascode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.requirementsascode.flowposition.After;
import org.requirementsascode.flowposition.FlowPosition;

/**
 * @author b_muth
 */
public class InterruptableFlowStep extends FlowStep implements Serializable {
    private static final long serialVersionUID = -2926490717985964131L;

    /**
     * Creates step with the specified name as the last step of the
     * specified use case flow.
     *
     * @param stepName
     *            the name of the step to be created
     * @param useCaseFlow
     *            the use case flow that will contain the new step
     */
    InterruptableFlowStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
	appendToLastStepOfFlow();
    }

    private void appendToLastStepOfFlow() {
	List<FlowStep> flowSteps = getFlow().getSteps();
	FlowStep lastFlowStep = flowSteps.size() > 0 ? flowSteps.get(flowSteps.size() - 1) : null;
	setPreviousStepInFlow(lastFlowStep);
	setFlowPosition(new After(lastFlowStep));
    }

    @Override
    public Predicate<ModelRunner> getCondition() {
	Predicate<ModelRunner> condition;
	Predicate<ModelRunner> reactWhile = getReactWhile();

	if (reactWhile != null) {
	    condition = reactWhile;
	} else {
	    FlowPosition flowPosition = getFlowPosition();
	    condition = flowPosition.and(noStepInterrupts());
	}

	return condition;
    }

    private Predicate<ModelRunner> noStepInterrupts() {
	return modelRunner -> {
	    Class<?> theStepsEventClass = getEventClass();
	    Model useCaseModel = getModel();

	    Stream<Step> stepsStream = useCaseModel.getModifiableSteps().stream();
	    Stream<Step> conditionalStepsStream = stepsStream
		    .filter(isInterruptingStep().and(isOtherStepThan(this)));

	    Set<Step> conditionalStepsThatCanReact = modelRunner
		    .stepsInStreamThatCanReactTo(theStepsEventClass, conditionalStepsStream);
	    return conditionalStepsThatCanReact.size() == 0;
	};
    }

    private Predicate<Step> isInterruptingStep() {
	return step -> InterruptingFlowStep.class.equals(step.getClass());
    }

    private Predicate<Step> isOtherStepThan(FlowStep theStep) {
	return step -> !step.equals(theStep);
    }
}
