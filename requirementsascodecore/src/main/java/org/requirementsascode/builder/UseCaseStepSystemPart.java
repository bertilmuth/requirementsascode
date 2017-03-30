package org.requirementsascode.builder;

import static org.requirementsascode.UseCaseStepPredicates.afterStep;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseFlow;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepSystem;
import org.requirementsascode.exception.ElementAlreadyInModel;

public class UseCaseStepSystemPart<T>{
	private UseCaseStepSystem<T> useCaseStepSystem;
	private UseCaseStepPart useCaseStepPart;
	private UseCaseModelBuilder useCaseModelBuilder;
	private UseCasePart useCasePart;

	public UseCaseStepSystemPart(UseCaseStepSystem<T> useCaseStepSystem, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepSystem = useCaseStepSystem;
		this.useCaseStepPart = useCaseStepPart;
		this.useCasePart = useCaseStepPart.useCasePart();
		this.useCaseModelBuilder = useCaseStepPart.useCaseModelBuilder();
	}

	public UseCaseModel build() {
		return useCaseModelBuilder.build();
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
		UseCaseStep currentUseCaseStep = useCaseStepPart.useCaseStep();
		
		UseCaseStep nextUseCaseStepInFlow = 
			useCasePart.useCase().newStep(stepName, useCaseFlow, 
				Optional.of(currentUseCaseStep), 
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
		UseCaseFlowPart useCaseFlowPart = useCasePart.flow(flowName);
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
		UseCasePart useCasePart = useCaseModelBuilder.useCase(useCaseName);
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
		Predicate<UseCaseRunner> performIfConditionIsTruePredicate = useCaseStep.predicate().and(condition);
		Predicate<UseCaseRunner> repeatIfConditionIsTruePredicate = afterStep(useCaseStep).and(condition);
		useCaseStep.setPredicate(performIfConditionIsTruePredicate.or(repeatIfConditionIsTruePredicate));
		
		return this;
	}
}
