package org.requirementsascode.builder;

import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseFlow;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepSystem;

public class UseCaseStepSystemPart<T>{
	private UseCaseStepSystem<T> useCaseStepSystem;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCaseStepSystemPart(UseCaseStepSystem<T> useCaseStepSystem, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCaseStepSystem = useCaseStepSystem;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public UseCaseModel build() {
		return useCaseModelBuilder.build();
	}

	public UseCaseStepPart step(String stepName) {
		UseCaseStep useCaseStep = useCaseStepSystem.step(stepName);
		return new UseCaseStepPart(useCaseStep, useCaseModelBuilder);
	}

	public UseCaseFlowPart flow(String flowName) {
		UseCaseFlow useCaseFlow = useCaseStepSystem.flow(flowName);
		return new UseCaseFlowPart(useCaseFlow, useCaseModelBuilder);
	}

	public UseCasePart useCase(String useCaseName) {
		UseCase useCase = useCaseStepSystem.useCase(useCaseName);
		return new UseCasePart(useCase, useCaseModelBuilder);
	}
}
