package org.requirementsascode.builder;

import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCase;
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

	public UseCaseStepSystemPart(UseCaseStepSystem<T> useCaseStepSystem, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepSystem = useCaseStepSystem;
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseModelBuilder = useCaseStepPart.useCaseModelBuilder();
	}

	public UseCaseModel build() {
		return useCaseModelBuilder.build();
	}

	/**
	 * Creates a new step in this flow, with the specified name, that follows this step in sequence.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public UseCaseStepPart step(String stepName) {
		UseCaseFlowPart useCaseFlowPart = useCaseStepPart.useCaseFlowPart();
		UseCaseFlow useCaseFlow = useCaseFlowPart.useCaseFlow();
		
		UseCaseStep useCaseStep = 
			useCaseFlow.useCase().newStep(stepName, useCaseFlow, 
				Optional.of(useCaseStepPart.useCaseStep()), Optional.empty());
		
		return new UseCaseStepPart(useCaseStep, useCaseFlowPart);
	}

	public UseCaseFlowPart flow(String flowName) {
		UseCaseFlow useCaseFlow = useCaseStepSystem.flow(flowName);
		return new UseCaseFlowPart(useCaseFlow, useCaseStepPart.useCasePart());
	}

	public UseCasePart useCase(String useCaseName) {
		UseCase useCase = useCaseStepSystem.useCase(useCaseName);
		return new UseCasePart(useCase, useCaseModelBuilder);
	}

	public UseCaseStepSystemPart<T> reactWhile(Predicate<UseCaseRunner> condition) {
		useCaseStepSystem.reactWhile(condition);
		return this;
	}
}
