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

	public UseCasePart(UseCase useCase, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCase = useCase;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public FlowPart basicFlow() {
		Flow useCaseFlow = useCase().basicFlow();
		return new FlowPart(useCaseFlow, this);
	}
	
	public FlowPart flow(String flowName) {
		Flow useCaseFlow = useCase().newFlow(flowName);
		return new FlowPart(useCaseFlow, this);
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
