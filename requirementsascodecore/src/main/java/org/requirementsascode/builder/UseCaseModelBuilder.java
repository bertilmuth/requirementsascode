package org.requirementsascode.builder;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCase;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.exception.ElementAlreadyInModel;

/**
 * Class that builds a {@link UseCaseModel}, in a fluent way.
 * 
 * @author b_muth
 *
 */
public class UseCaseModelBuilder {
	
	private UseCaseModel useCaseModel;

	private UseCaseModelBuilder(UseCaseModel useCaseModel) {
		this.useCaseModel = useCaseModel;
	}
	
	/**
	 * Create a builder for a new use case model.
	 * 
	 * @return the new builder
	 */
	public static UseCaseModelBuilder newBuilder() {
		return builderOf(new UseCaseModel());
	}
	
	/**
	 * Create a builder for an existing use case model,
	 * to continue building it.
	 * 
	 * @return a builder for the existing model
	 */
	public static UseCaseModelBuilder builderOf(UseCaseModel useCaseModel) {
		return new UseCaseModelBuilder(useCaseModel);
	}
	
	/**
	 * Creates a new actor in the current model.
	 * 
	 * @param actorName the name of the actor to be created.
	 * @return the newly created actor
	 * @throws ElementAlreadyInModel if an actor with the specified name already exists in the model
	 */
	public Actor actor(String actorName) {
		return useCaseModel.newActor(actorName);
	}

	/**
	 * Creates a new use case in the current model.
	 * 
	 * @param useCaseName the name of the use case to be created.
	 * @return the newly created use case
	 * @throws ElementAlreadyInModel if a use case with the specified name already exists in the model
	 */
	public UseCasePart useCase(String useCaseName) {
		UseCase useCase = useCaseModel.newUseCase(useCaseName);
		return new UseCasePart(useCase, this);
	}
	
	/**
	 * Returns the use case model built so far.
	 * 
	 * @return the use case model
	 */
	public UseCaseModel build() {
		return useCaseModel;
	}
}
