package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.predicate.After;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see Step#setSystemReaction(Consumer)
 * @author b_muth
 *
 */
public class StepSystemPart<T>{
	private StepPart stepPart;
	private Step step;

	StepSystemPart(StepPart useCaseStepPart, Consumer<T> systemReaction) {
		this.stepPart = useCaseStepPart;
		this.step = useCaseStepPart.useCaseStep();
		step.setSystemReaction(systemReaction);
	}

	public UseCaseModel build() {
		return stepPart.useCaseModelBuilder().build();
	}

	/**
	 * Creates a new step in this flow, with the specified name, that follows the current step in sequence.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public StepPart step(String stepName) {
		FlowPart useCaseFlowPart = stepPart.useCaseFlowPart();
		Flow useCaseFlow = useCaseFlowPart.useCaseFlow();
		
		Step nextUseCaseStepInFlow = 
			useCaseFlow.useCase().newStep(stepName, useCaseFlow, 
				Optional.of(step), 
					Optional.empty());
		
		return new StepPart(nextUseCaseStepInFlow, useCaseFlowPart);
	}

	/**
	 * Creates a new flow in the current use case.
	 * 
	 * @param flowName the name of the flow to be created.
	 * @return the newly created flow part
	 * @throws ElementAlreadyInModel if a flow with the specified name already exists in the use case
	 */
	public FlowPart flow(String flowName) {
		Objects.requireNonNull(flowName);
		FlowPart useCaseFlowPart = 
			stepPart.useCasePart().flow(flowName);
		return useCaseFlowPart;
	}
	
	/**
	 * Creates a new use case in the current model.
	 * 
	 * @param useCaseName the name of the use case to be created.
	 * @return the newly created use case part
	 * @throws ElementAlreadyInModel if a use case with the specified name already exists in the model
	 */
	public UseCasePart useCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		UseCasePart useCasePart = 
			stepPart.useCaseModelBuilder().useCase(useCaseName);
		return useCasePart;
	}
	
	/**
	 * React to this step's event as long as the condition is fulfilled.
	 * 
	 * Even when the condition is fulfilled, the flow can advance given
	 * that the event of the next step is received.
	 * 
	 * Note that if the condition is not fulfilled after the previous step has been performed,
	 * the step will not react at all.
	 * 
	 * @param condition the condition to check
	 * @return the system part
	 */
	public StepSystemPart<T> reactWhile(Predicate<UseCaseModelRunner> condition) {
		Objects.requireNonNull(condition);
		
		Step useCaseStep = stepPart.useCaseStep();
		Predicate<UseCaseModelRunner> performIfConditionIsTrue = useCaseStep.predicate().and(condition);
		Predicate<UseCaseModelRunner> repeatIfConditionIsTrue = new After(Optional.of(useCaseStep)).and(condition);
		useCaseStep.setPredicate(performIfConditionIsTrue.or(repeatIfConditionIsTrue));
		
		return this;
	}
}
