package org.requirementsascode.builder;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseModel;

public class UseCaseModelBuilder {
	
	private UseCaseModel useCaseModel;

	private UseCaseModelBuilder(UseCaseModel useCaseModel) {
		this.useCaseModel = useCaseModel;
	}
	
	public static UseCaseModelBuilder newModelBuilder() {
		return modelBuilderOf(new UseCaseModel());
	}
	
	public static UseCaseModelBuilder modelBuilderOf(UseCaseModel useCaseModel) {
		return new UseCaseModelBuilder(useCaseModel);
	}

	public UseCaseModel build() {
		return useCaseModel;
	}

	public UseCasePart useCase(String useCaseName) {
		UseCase useCase = useCaseModel.newUseCase(useCaseName);
		return new UseCasePart(useCase, this);
	}

	public Actor actor(String actorName) {
		return useCaseModel.newActor(actorName);
	}


}
