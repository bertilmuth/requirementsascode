package org.requirementsascode.builder;

import org.requirementsascode.UseCaseModel;
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
}
