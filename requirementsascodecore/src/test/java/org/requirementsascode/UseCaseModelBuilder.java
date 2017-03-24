package org.requirementsascode;

public class UseCaseModelBuilder {
	
	private UseCaseModel useCaseModel;

	public UseCaseModelBuilder(UseCaseRunner useCaseRunner) {
		this.useCaseModel = new UseCaseModel(useCaseRunner);
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
