package org.requirementsascode.builder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.Flow;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.Step;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.predicate.After;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see Step#setSystemReaction(Consumer)
 * @author b_muth
 *
 */
public class UseCaseStepSystemPart<T>{
	private StepPart useCaseStepPart;
	private Step useCaseStep;

	public UseCaseStepSystemPart(StepPart useCaseStepPart, Consumer<T> systemReaction) {
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseStep = useCaseStepPart.useCaseStep();
		useCaseStep.setSystemReaction(systemReaction);
	}

	public UseCaseModel build() {
		return useCaseStepPart.useCaseModelBuilder().build();
	}

	/**
	 * Creates a new step in this flow, with the specified name, that follows the current step in sequence.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public StepPart step(String stepName) {
		FlowPart useCaseFlowPart = useCaseStepPart.useCaseFlowPart();
		Flow useCaseFlow = useCaseFlowPart.useCaseFlow();
		
		Step nextUseCaseStepInFlow = 
			useCaseFlow.useCase().newStep(stepName, useCaseFlow, 
				Optional.of(useCaseStep), 
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
			useCaseStepPart.useCasePart().flow(flowName);
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
			useCaseStepPart.useCaseModelBuilder().useCase(useCaseName);
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
	public UseCaseStepSystemPart<T> reactWhile(Predicate<UseCaseModelRunner> condition) {
		Objects.requireNonNull(condition);
		
		Step useCaseStep = useCaseStepPart.useCaseStep();
		Predicate<UseCaseModelRunner> performIfConditionIsTrue = useCaseStep.predicate().and(condition);
		Predicate<UseCaseModelRunner> repeatIfConditionIsTrue = new After(Optional.of(useCaseStep)).and(condition);
		useCaseStep.setPredicate(performIfConditionIsTrue.or(repeatIfConditionIsTrue));
		
		return this;
	}
}
