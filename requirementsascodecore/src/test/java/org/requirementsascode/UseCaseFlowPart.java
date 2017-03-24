package org.requirementsascode;

public class UseCaseFlowPart {
	private UseCaseFlow useCaseFlow;
	private UseCaseModelBuilder useCaseModelBuilder;

	public UseCaseFlowPart(UseCaseFlow useCaseFlow, UseCaseModelBuilder useCaseModelBuilder) {
		this.useCaseFlow = useCaseFlow;
		this.useCaseModelBuilder = useCaseModelBuilder;
	}

	public UseCaseStepPart step(String stepName) {
		UseCaseStep useCaseStep = useCaseFlow.step(stepName);
		return new UseCaseStepPart(useCaseStep, useCaseModelBuilder);
	}

}
