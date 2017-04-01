package org.requirementsascode.builder;

import static org.requirementsascode.UseCaseStepPredicates.afterStep;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseFlow;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.exception.ElementAlreadyInModel;

public class UseCaseStepSystemPart<T>{
	private UseCaseStepPart useCaseStepPart;
	private UseCaseStep useCaseStep;

	public UseCaseStepSystemPart(UseCaseStepPart useCaseStepPart, Consumer<T> systemReaction) {
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
	 * @return the newly created step, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public UseCaseStepPart step(String stepName) {
		UseCaseFlowPart useCaseFlowPart = useCaseStepPart.useCaseFlowPart();
		UseCaseFlow useCaseFlow = useCaseFlowPart.useCaseFlow();
		
		UseCaseStep nextUseCaseStepInFlow = 
			useCaseFlow.useCase().newStep(stepName, useCaseFlow, 
				Optional.of(useCaseStep), 
					Optional.empty());
		
		return new UseCaseStepPart(nextUseCaseStepInFlow, useCaseFlowPart);
	}

	/**
	 * Creates a new flow in the current use case.
	 * 
	 * @param flowName the name of the flow to be created.
	 * @return the newly created flow part
	 * @throws ElementAlreadyInModel if a flow with the specified name already exists in the use case
	 */
	public UseCaseFlowPart flow(String flowName) {
		Objects.requireNonNull(flowName);
		UseCaseFlowPart useCaseFlowPart = 
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
	public UseCaseStepSystemPart<T> reactWhile(Predicate<UseCaseRunner> condition) {
		Objects.requireNonNull(condition);
		
		UseCaseStep useCaseStep = useCaseStepPart.useCaseStep();
		Predicate<UseCaseRunner> performIfConditionIsTrue = useCaseStep.predicate().and(condition);
		Predicate<UseCaseRunner> repeatIfConditionIsTrue = afterStep(Optional.of(useCaseStep)).and(condition);
		useCaseStep.setPredicate(performIfConditionIsTrue.or(repeatIfConditionIsTrue));
		
		return this;
	}
}
