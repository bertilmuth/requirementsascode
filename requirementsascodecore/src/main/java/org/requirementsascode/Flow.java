package org.requirementsascode;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.requirementsascode.flowposition.FlowPosition;

/**
 * A flow defines a sequence of steps that lead the user through a use case.
 *
 * <p>
 * A flow either ends with the user reaching her/his goal, or terminates before,
 * usually because of an exception that occurred.
 *
 * @author b_muth
 */
public class Flow extends ModelElement implements Serializable {
	private static final long serialVersionUID = -2448742413260609615L;

	private UseCase useCase;

	/**
	 * Creates a flow with the specified name that belongs to the specified use
	 * case.
	 *
	 * @param name    the name of the flow to be created
	 * @param useCase the use case that will contain the new flow
	 */
	Flow(String name, UseCase useCase) {
		super(name, useCase.getModel());
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
		List<FlowStep> steps = getUseCase().getModifiableSteps().stream().filter(step -> step instanceof FlowStep)
				.map(step -> (FlowStep) step).filter(step -> this.equals(step.getFlow())).collect(Collectors.toList());
		return Collections.unmodifiableList(steps);
	}

	/**
	 * Returns the first step of the flow
	 *
	 * @return the first step of the flow, or an empty optional if the flow has no
	 *         steps.
	 */
	public Optional<FlowStep> getFirstStep() {
		Collection<Step> steps = getUseCase().getModifiableSteps();
		FlowStep firstStep = null;
		for (Step step : steps) {
			if(step instanceof FlowStep && this.equals(((FlowStep)step).getFlow())) {
				firstStep = (FlowStep)step;
				break;
			}
		}
		return Optional.ofNullable(firstStep);
	}

	/**
	 * Convenience method that returns the position of the flow (as defined e.g. by
	 * "InsteadOf").
	 *
	 * <p>
	 * Internally this calls the method of the same name of the first step in the
	 * flow.
	 *
	 * @return the flow position, or null if the flow is empty.
	 */
	public FlowPosition getFlowPosition() {
		FlowPosition flowPosition = getFirstStep().map(step -> step.getFlowPosition()).orElse(null);
		return flowPosition;
	}

	/**
	 * Convenience method that returns the condition of the flow.
	 *
	 * <p>
	 * Internally this calls the method of the same name of the first step in the
	 * flow.
	 *
	 * @return the condition
	 */
	public Optional<Condition> getCondition() {
		Optional<Condition> condition = getFirstStep().flatMap(step -> step.getCondition());
		return condition;
	}
}
