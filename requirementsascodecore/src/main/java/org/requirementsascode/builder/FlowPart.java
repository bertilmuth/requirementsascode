package org.requirementsascode.builder;

import java.util.Objects;

import org.requirementsascode.Condition;
import org.requirementsascode.Flow;
import org.requirementsascode.FlowStep;
import org.requirementsascode.Model;
import org.requirementsascode.UseCase;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.flowposition.After;
import org.requirementsascode.flowposition.Anytime;
import org.requirementsascode.flowposition.InsteadOf;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Flow
 * @author b_muth
 */
public class FlowPart {
	private Flow flow;
	private UseCase useCase;
	private UseCasePart useCasePart;
	private FlowPositionPart optionalFlowPositionPart;

	FlowPart(Flow flow, UseCasePart useCasePart) {
		this.flow = flow;
		this.useCasePart = useCasePart;
		this.useCase = useCasePart.getUseCase();
	}

	/**
	 * Starts the flow after the specified step has been run, in this flow's use
	 * case.
	 * 
	 * Note: You should use after to handle exceptions that occurred in the
	 * specified step.
	 *
	 * @param stepName the name of the step to start the flow after
	 * @return the flow position part, to ease creation of the condition and the
	 *         first step of the flow
	 * @throws NoSuchElementInModel if the specified step is not found in a flow of
	 *                              this use case
	 * 
	 */
	public FlowPositionPart after(String stepName) {
		FlowStep flowStep = (FlowStep)useCase.findStep(stepName);
		After after = new After(flowStep);
		optionalFlowPositionPart = new FlowPositionPart(after, this);
		return optionalFlowPositionPart;
	}

	/**
	 * Creates the first step of this flow, without specifying position or
	 * condition. It can be interrupted by any other flow that has an explicit
	 * position and/or condition. It can be run when no other step has been run
	 * before.
	 *
	 * @param stepName the name of the step to be created
	 * @return the newly created step part, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already
	 *                               exists in the use case
	 */
	public StepPart step(String stepName) {
		StepPart stepPart = new StepPart(stepName, FlowPart.this);
		return stepPart;
	}

	/**
	 * Starts the flow as an alternative to the specified step, in this flow's use
	 * case.
	 *
	 * @param stepName the name of the specified step
	 * @return the flow position part, to ease creation of the condition and the
	 *         first step of the flow
	 * @throws NoSuchElementInModel if the specified step is not found in this
	 *                              flow's use case
	 */
	public FlowPositionPart insteadOf(String stepName) {
		FlowStep flowStep = (FlowStep) useCase.findStep(stepName);
		InsteadOf insteadOf = new InsteadOf(flowStep);
		optionalFlowPositionPart = new FlowPositionPart(insteadOf, this);
		return optionalFlowPositionPart;
	}

	/**
	 * Starts the flow after any step that has been run, or at the beginning.
	 * 
	 * @return the flow position part, to ease creation of the condition and the
	 *         first step of the flow
	 */
	public FlowPositionPart anytime() {
		Anytime anytime = new Anytime();
		optionalFlowPositionPart = new FlowPositionPart(anytime, this);
		return optionalFlowPositionPart;
	}

	/**
	 * Constrains the flow's condition: only if the specified condition is true, the
	 * flow is started.
	 *
	 * @param condition the condition that constrains when the flow is started
	 * @return the condition part, to ease creation of the first step of the flow
	 */
	public FlowConditionPart condition(Condition condition) {
		Objects.requireNonNull(condition);
		optionalFlowPositionPart = new FlowPositionPart(new Anytime(), this);
		FlowConditionPart conditionPart = optionalFlowPositionPart.condition(condition);
		return conditionPart;
	}

	Flow getFlow() {
		return flow;
	}

	UseCasePart getUseCasePart() {
		return useCasePart;
	}

	ModelBuilder getModelBuilder() {
		return useCasePart.getModelBuilder();
	}
}
