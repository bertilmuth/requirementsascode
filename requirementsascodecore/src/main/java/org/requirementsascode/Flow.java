package org.requirementsascode;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A use case flow defines a sequence of steps that lead the user through the
 * use case.
 *
 * <p>
 * A flow either ends with the user reaching her/his goal, or terminates before,
 * usually because of an exception that occurred.
 *
 * @author b_muth
 */
public class Flow extends UseCaseModelElement implements Serializable {
    private static final long serialVersionUID = -2448742413260609615L;

    private UseCase useCase;

    /**
     * Creates a use case flow with the specified name that belongs to the specified
     * use case.
     *
     * @param name
     *            the name of the flow to be created
     * @param useCase
     *            the use case that will contain the new flow
     */
    Flow(String name, UseCase useCase) {
	super(name, useCase.getUseCaseModel());
	this.useCase = useCase;
    }

    /**
     * Returns the use case this flow is part of.
     *
     * @return the containing use case
     */
    public UseCase getUseCase() {
	return useCase;
    }

    /**
     * Returns the steps contained in this flow. Do not modify the returned
     * collection directly.
     *
     * @return a collection of the steps
     */
    public List<FlowStep> getSteps() {
	List<FlowStep> steps = getUseCase().getModifiableSteps().stream()
		.filter(step -> step instanceof FlowStep)
		.map(step -> (FlowStep)step)
		.filter(step -> this.equals(step.getFlow()))
		.collect(Collectors.toList());
	return Collections.unmodifiableList(steps);
    }

    /**
     * Returns the first step of the flow
     *
     * @return the first step of the flow, or an empty optional if the flow has no
     *         steps.
     */
    public Optional<FlowStep> getFirstStep() {
	List<FlowStep> steps = getSteps();
	return steps.size() > 0 ? Optional.of(steps.get(0)) : Optional.empty();
    }

    /**
     * Convenience method that returns the position of the flow (as defined e.g. by
     * "InsteadOf").
     *
     * <p>
     * Internally this calls the method of the same name of the first step in the
     * flow.
     *
     * @return the flow position
     */
    public Optional<Predicate<UseCaseModelRunner>> getFlowPosition() {
	Optional<Predicate<UseCaseModelRunner>> flowPosition = getFirstStep().flatMap(step -> step.getFlowPosition());
	return flowPosition;
    }

    /**
     * Convenience method that returns the when predicate of the flow.
     *
     * <p>
     * Internally this calls the method of the same name of the first step in the
     * flow.
     *
     * @return the when predicate
     */
    public Optional<Predicate<UseCaseModelRunner>> getWhen() {
	Optional<Predicate<UseCaseModelRunner>> when = getFirstStep().flatMap(step -> step.getWhen());
	return when;
    }
}
