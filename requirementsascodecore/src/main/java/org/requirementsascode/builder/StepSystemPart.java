package org.requirementsascode.builder;

import static org.requirementsascode.builder.StepPart.interruptableFlowStepPart;
import static org.requirementsascode.builder.StepToPart.stepToPart;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.requirementsascode.Behavior;
import org.requirementsascode.Condition;
import org.requirementsascode.FlowStep;
import org.requirementsascode.Model;
import org.requirementsascode.Step;
import org.requirementsascode.exception.ElementAlreadyInModel;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @author b_muth
 */
public class StepSystemPart<T> {
	private StepPart stepPart;
	private Step step;
	
	private StepSystemPart(StepPart stepPart) {
		this.stepPart = Objects.requireNonNull(stepPart);
		this.step = stepPart.getStep();
	}
	
	static <T> StepSystemPart<T> stepSystemPartWithRunnable(Runnable systemReaction, StepPart stepPart) {
		stepPart.getStep().setSystemReaction(systemReaction);
		return new StepSystemPart<>(stepPart);
	}
	
	static <T> StepSystemPart<T> stepSystemPartWithConsumer(Consumer<? super T> systemReaction, StepPart stepPart) {
		stepPart.getStep().setSystemReaction(systemReaction);
		return new StepSystemPart<>(stepPart);
	}
	
	static <T> StepSystemPart<T> stepSystemPartWithFunction(Function<? super T, ?> systemReaction, StepPart stepPart) {
		stepPart.getStep().setSystemReaction(systemReaction);
		return new StepSystemPart<>(stepPart);
	}
	
	static <T> StepSystemPart<T> stepSystemPartWithSupplier(Supplier<?> systemReaction, StepPart stepPart) {
		stepPart.getStep().setSystemReaction(systemReaction);
		return new StepSystemPart<>(stepPart);
	}

	/**
	 * Creates a new step in this flow, with the specified name, that follows the
	 * the step before in sequence.
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
	public StepUseCasePart useCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		UseCasePart newUseCasePart = stepPart.getModelBuilder().useCase(useCaseName);
		return new StepUseCasePart(newUseCasePart);
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
	

	/** Specifies the recipient of the message.
	 * 
	 * @param recipient the recipient of this message
	 * @return the created part
	 */
	public StepToPart<T> to(Behavior recipient) {
		Objects.requireNonNull(recipient);
		StepToPart<T> stepToPart = stepToPart(this, recipient);
		return stepToPart;
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
