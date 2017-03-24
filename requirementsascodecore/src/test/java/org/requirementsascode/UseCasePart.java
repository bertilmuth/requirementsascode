package org.requirementsascode;

public class UseCasePart {
	private UseCase useCase;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCasePart(UseCase useCase, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCase = useCase;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public UseCaseFlowPart basicFlow() {
		UseCaseFlow useCaseFlow = new UseCaseFlow("Basic Flow", useCase());
		return new UseCaseFlowPart(useCaseFlow, useCaseModelBuilder);
	}

	public UseCase useCase() {
		return useCase;
	}

}
