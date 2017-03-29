package org.requirementsascode.builder;

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

	public UseCaseStepSystemPart<T> reactWhile(Predicate<UseCaseRunner> condition) {
		useCaseStepSystem.reactWhile(condition);
		return this;
	}
}
