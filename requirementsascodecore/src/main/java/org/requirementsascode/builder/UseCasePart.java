package org.requirementsascode.builder;

import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseFlow;
import org.requirementsascode.UseCaseModel;

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

	public UseCaseFlowPart basicFlow() {
		UseCaseFlow useCaseFlow = useCase().basicFlow();
		return new UseCaseFlowPart(useCaseFlow, this);
	}
	
	public UseCaseFlowPart flow(String flowName) {
		UseCaseFlow useCaseFlow = useCase().newFlow(flowName);
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
