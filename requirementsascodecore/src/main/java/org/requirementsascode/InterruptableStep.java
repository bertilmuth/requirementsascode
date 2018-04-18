package org.requirementsascode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author b_muth
 */
public class InterruptableStep extends Step implements Serializable {
    private static final long serialVersionUID = -2926490717985964131L;

    /**
     * Creates a use case step with the specified name as the last step of the
     * specified use case flow.
     *
     * @param stepName
     *            the name of the step to be created
     * @param useCaseFlow
     *            the use case flow that will contain the new use case
     */
    InterruptableStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
	appendToLastStepOfFlow();
    }

    private void appendToLastStepOfFlow() {
	List<Step> flowSteps = getFlow().getSteps();
	Step lastFlowStep = flowSteps.size() > 0 ? flowSteps.get(flowSteps.size() - 1) : null;
	setPreviousStepInFlow(lastFlowStep);
    }

    @Override
    public Predicate<UseCaseModelRunner> getPredicate() {
	Predicate<UseCaseModelRunner> predicate;
	Predicate<UseCaseModelRunner> reactWhile = getReactWhile();

	if (reactWhile != null) {
	    predicate = reactWhile;
	} else {
	    predicate = getFlowPosition().get().and(noConditionalStepInterrupts());
	}

	return predicate;
    }

    private Predicate<UseCaseModelRunner> noConditionalStepInterrupts() {
	return useCaseModelRunner -> {
	    Class<?> theStepsEventClass = getUserEventClass();
	    UseCaseModel useCaseModel = getUseCaseModel();

	    Stream<Step> stepsStream = useCaseModel.getModifiableSteps().stream();
	    Stream<Step> conditionalStepsStream = stepsStream
		    .filter(isConditionalStep().and(isOtherStepThan(this)));

	    Set<Step> conditionalStepsThatCanReact = useCaseModelRunner
		    .stepsInStreamThatCanReactTo(theStepsEventClass, conditionalStepsStream);
	    return conditionalStepsThatCanReact.size() == 0;
	};
    }

    private Predicate<Step> isConditionalStep() {
	return step -> InterruptingStep.class.equals(step.getClass());
    }

    private Predicate<Step> isOtherStepThan(Step theStep) {
	return step -> !step.equals(theStep);
    }
}
