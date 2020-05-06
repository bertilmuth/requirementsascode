package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.requirementsascode.Condition;
import org.requirementsascode.Flow;
import org.requirementsascode.FlowStep;
import org.requirementsascode.Model;
import org.requirementsascode.Step;
import org.requirementsascode.SystemReaction;
import org.requirementsascode.exception.ElementAlreadyInModel;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @author b_muth
 */
public class StepSystemPart<T> {
	private StepPart stepPart;
	private Step step;
	
	StepSystemPart(Runnable systemReaction, StepPart stepPart) {
		this.stepPart = stepPart;
		this.step = stepPart.getStep();
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		step.setSystemReaction(systemReactionObject);
	}
	
	StepSystemPart(Consumer<? super T> systemReaction, StepPart stepPart) {
		this.stepPart = stepPart;
		this.step = stepPart.getStep();
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		step.setSystemReaction(systemReactionObject);
	}
	
	StepSystemPart(Function<? super T, ?> systemReaction, StepPart stepPart) {
		this.stepPart = stepPart;
		this.step = stepPart.getStep();
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		step.setSystemReaction(systemReactionObject);
	}
	
	StepSystemPart(Supplier<? super T> systemReaction, StepPart stepPart) {
		this.stepPart = stepPart;
		this.step = stepPart.getStep();
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		step.setSystemReaction(systemReactionObject);
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
		FlowPart flowPart = stepPart.getFlowPart();
		FlowStep trailingStepInFlow = createTrailingStepInFlow(stepName, flowPart);
		StepPart trailingStepInFlowPart = new StepPart(trailingStepInFlow, stepPart.getUseCasePart(), flowPart);
		return trailingStepInFlowPart;
	}

	FlowStep createTrailingStepInFlow(String stepName, FlowPart flowPart) {
		Flow flow = flowPart.getFlow();
		FlowStep newTrailingStep = step.getUseCase().newInterruptableFlowStep(stepName, flow);
		return newTrailingStep;
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
	public StepSystemPart<T> reactWhile(Condition reactWhileCondition) {
		Objects.requireNonNull(reactWhileCondition);
		((FlowStep) step).setReactWhile(reactWhileCondition);
		return this;
	}

	StepPart getStepPart() {
		return stepPart;
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
