package org.requirementsascode;

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
}
