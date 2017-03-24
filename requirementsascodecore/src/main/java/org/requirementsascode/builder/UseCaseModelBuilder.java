package org.requirementsascode.builder;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class UseCaseModelBuilder {
	
	private UseCaseModel useCaseModel;

	public UseCaseModelBuilder(UseCaseRunner useCaseRunner) {
		this.useCaseModel = useCaseRunner.useCaseModel();
	}

	public UseCaseModel build() {
		return useCaseModel;
	}

	public UseCasePart useCase(String useCaseName) {
		UseCase useCase = useCaseModel.useCase(useCaseName);
		return new UseCasePart(useCase, this);
	}

	public Actor actor(String actorName) {
		return useCaseModel.actor(actorName);
	}

}
