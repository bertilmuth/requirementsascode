package org.requirementsascode.builder;

import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseFlow;
import org.requirementsascode.UseCaseModel;

public class UseCasePart {
	private UseCase useCase;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCasePart(UseCase useCase, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCase = useCase;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public UseCaseFlowPart basicFlow() {
		UseCaseFlow useCaseFlow = useCase().basicFlow();
		return new UseCaseFlowPart(useCaseFlow, this);
	}
	
	public UseCaseFlowPart flow(String flowName) {
		UseCaseFlow useCaseFlow = useCase().flow(flowName);
		return new UseCaseFlowPart(useCaseFlow, this);
	}

	public UseCase useCase() {
		return useCase;
	}

	public UseCaseModelBuilder useCaseModelBuilder() {
		return useCaseModelBuilder;
	}

	public UseCaseModel build() {
		return useCaseModelBuilder.build();
	}
}
