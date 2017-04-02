package org.requirementsascode;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see UseCase
 * @author b_muth
 *
 */
public class UseCasePart {
	private UseCase useCase;
	private UseCaseModelBuilder useCaseModelBuilder;

	UseCasePart(UseCase useCase, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCase = useCase;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public FlowPart basicFlow() {
		Flow useCaseFlow = useCase().getBasicFlow();
		return new FlowPart(useCaseFlow, this);
	}
	
	public FlowPart flow(String flowName) {
		Flow useCaseFlow = useCase().newFlow(flowName);
		return new FlowPart(useCaseFlow, this);
	}

	UseCase useCase() {
		return useCase;
	}

	UseCaseModelBuilder useCaseModelBuilder() {
		return useCaseModelBuilder;
	}

	public UseCaseModel build() {
		return useCaseModelBuilder.build();
	}
}
