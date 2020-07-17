package org.requirementsascode.builder;

import static org.requirementsascode.builder.StepPart.interruptableFlowStepPart;

import java.util.Objects;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.FlowStep;
import org.requirementsascode.Model;
import org.requirementsascode.exception.ElementAlreadyInModel;

public class StepToPart<T> {
	private StepPart stepPart;

	private StepToPart(StepPart stepPart, AbstractActor recipient) {
		this.stepPart = Objects.requireNonNull(stepPart);
	}
	
	public static <T> StepToPart<T> stepToPart(StepSystemPart<T> stepSystemPart, AbstractActor recipient) {
		StepPart stepPart = stepSystemPart.getStepPart();
		stepPart.getStep().setPublishTo(recipient);
		return new StepToPart<>(stepPart, recipient);
	}
	
	/**
	 * Creates a new step in this flow, with the specified name, that follows the
	 * current step in sequence.
	 *
	 * @param stepName the name of the step to be created
	 * @return the newly created step
	 * @throws ElementAlreadyInModel if a step with the specified name already
	 *                               exists in the use case
	 */
	public StepPart step(String stepName) {
		Objects.requireNonNull(stepName);
		StepPart trailingStepInFlowPart = interruptableFlowStepPart(stepName, stepPart.getFlowPart());
		return trailingStepInFlowPart;
	}

	/**
	 * Creates a new flow in the current use case.
	 *
	 * @param flowName the name of the flow to be created.
	 * @return the newly created flow part
	 * @throws ElementAlreadyInModel if a flow with the specified name already
	 *                               exists in the use case
	 */
	public FlowPart flow(String flowName) {
		Objects.requireNonNull(flowName);
		FlowPart useCaseFlowPart = stepPart.getUseCasePart().flow(flowName);
		return useCaseFlowPart;
	}

	/**
	 * Creates a new use case in the current model.
	 *
	 * @param useCaseName the name of the use case to be created.
	 * @return the newly created use case part
	 * @throws ElementAlreadyInModel if a use case with the specified name already
	 *                               exists in the model
	 */
	public UseCasePart useCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		UseCasePart useCasePart = stepPart.getModelBuilder().useCase(useCaseName);
		return useCasePart;
	}

	/**
	 * React to this step's message as long as the condition is fulfilled.
	 *
	 * <p>
	 * Even when the condition is fulfilled, the flow can advance given that the
	 * message of the next step is received.
	 *
	 * <p>
	 * Note that if the condition is not fulfilled after the previous step has been
	 * performed, the step will not react at all.
	 *
	 * @param reactWhileCondition the condition to check
	 * @return the system part
	 */
	public StepToPart<T> reactWhile(Condition reactWhileCondition) {
		Objects.requireNonNull(reactWhileCondition);
		((FlowStep) stepPart.getStep()).setReactWhile(reactWhileCondition);
		return this;
	}
	
	/**
	 * Returns the model built so far.
	 *
	 * @return the model
	 */
	public Model build() {
		return stepPart.getModelBuilder().build();
	}
}
