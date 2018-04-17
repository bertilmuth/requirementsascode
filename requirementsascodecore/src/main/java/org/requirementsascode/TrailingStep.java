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
     * Creates a use case step with the specified name that belongs to the specified
     * use case flow.
     *
     * @param stepName
     *            the name of the step to be created
     * @param useCaseFlow
     *            the use case flow that will contain the new use case
     */
    TrailingStep(String stepName, UseCase useCase, Flow useCaseFlow) {
	super(stepName, useCase, useCaseFlow);
	setLastFlowStepAsPreviousStep();
    }

    void setLastFlowStepAsPreviousStep() {
	List<Step> existingSteps = getFlow().getSteps();
	Step lastExistingStep = existingSteps.size() > 0 ? existingSteps.get(existingSteps.size() - 1) : null;
	setPreviousStepInFlow(lastExistingStep);
    }

    public Predicate<UseCaseModelRunner> getPredicate() {
	Step previousStepInFlow = getPreviousStepInFlow().orElse(null);
	Predicate<UseCaseModelRunner> afterPreviousStepIfNoConditionalStepInterrupts = new After(previousStepInFlow)
		.and(noConditionalStepInterrupts());
	return afterPreviousStepIfNoConditionalStepInterrupts;
    }

    Predicate<UseCaseModelRunner> getDefaultPredicate() {
	Step previousStepInFlow = getPreviousStepInFlow().orElse(null);
	return new After(previousStepInFlow).and(noConditionalStepInterrupts());
    }

    private Predicate<UseCaseModelRunner> noConditionalStepInterrupts() {
	return useCaseModelRunner -> {
	    Class<?> theStepsEventClass = getUserEventClass();
	    UseCaseModel useCaseModel = getUseCaseModel();

	    Stream<Step> stepsStream = useCaseModel.getModifiableSteps().stream();
	    Stream<Step> stepsWithDefinedPredicatesStream = stepsStream
		    .filter(hasDefinedPredicate().and(isOtherStepThan(this)));

	    Set<Step> stepsWithDefinedConditionsThatCanReact = useCaseModelRunner
		    .stepsInStreamThatCanReactTo(theStepsEventClass, stepsWithDefinedPredicatesStream);
	    return stepsWithDefinedConditionsThatCanReact.size() == 0;
	};
    }

    private Predicate<Step> hasDefinedPredicate() {
	return step -> step.getFlowPredicate().isPresent();
    }

    private Predicate<Step> isOtherStepThan(TrailingStep theStep) {
	return step -> !step.equals(theStep);
    }
}
