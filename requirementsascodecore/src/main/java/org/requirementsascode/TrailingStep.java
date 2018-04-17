package org.requirementsascode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.requirementsascode.predicate.After;

/**
 * A use case step, as part of a use case. The use case steps define the
 * behavior of the use case.
 *
 * <p>
 * A use case step is the core class of requirementsascode, providing all the
 * necessary configuration information to the {@link UseCaseModelRunner} to
 * cause the system to react to events.
 *
 * @author b_muth
 */
public class TrailingStep extends Step implements Serializable {
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
    TrailingStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
	appendToLastStepOfFlow();
	setFlowPosition(new After(previousStepInFlow));
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
	    Stream<Step> stepsWithDefinedPredicatesStream = stepsStream
		    .filter(isConditionalStep().and(isOtherStepThan(this)));

	    Set<Step> stepsWithDefinedConditionsThatCanReact = useCaseModelRunner
		    .stepsInStreamThatCanReactTo(theStepsEventClass, stepsWithDefinedPredicatesStream);
	    return stepsWithDefinedConditionsThatCanReact.size() == 0;
	};
    }

    private Predicate<Step> isConditionalStep() {
	return step -> ConditionalStep.class.equals(step.getClass());
    }

    private Predicate<Step> isOtherStepThan(Step theStep) {
	return step -> !step.equals(theStep);
    }
}
