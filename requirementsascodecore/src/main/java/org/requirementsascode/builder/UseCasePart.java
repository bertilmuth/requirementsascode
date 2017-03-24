package org.requirementsascode.builder;

import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseFlow;

public class UseCasePart {
	private UseCase useCase;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCasePart(UseCase useCase, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCase = useCase;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public UseCaseFlowPart basicFlow() {
		UseCaseFlow useCaseFlow = useCase().basicFlow();
		return new UseCaseFlowPart(useCaseFlow, useCaseModelBuilder);
	}

	public UseCase useCase() {
		return useCase;
	}

}
