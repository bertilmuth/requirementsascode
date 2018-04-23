package org.requirementsascode;

/**
 * Class that builds a {@link Model}, in a fluent way.
 *
 * @author b_muth
 */
public class ModelBuilder {

    private Model model;

    ModelBuilder(Model model) {
	this.model = model;
    }

    /**
     * Creates a new actor in the current model. If an actor with the specified name
     * already exists, returns the existing actor.
     *
     * @param actorName
     *            the name of the existing actor / actor to be created.
     * @return the created / found actor.
     */
    public Actor actor(String actorName) {
	Actor actor = model.hasActor(actorName) ? model.findActor(actorName)
		: model.newActor(actorName);
	return actor;
    }

    /**
     * Creates a new use case in the current model, and returns a part for building
     * its details. If a use case with the specified name already exists, returns a
     * part for the existing use case.
     *
     * @param useCaseName
     *            the name of the existing use case / use case to be created.
     * @return the created / found use case's part.
     */
    public UseCasePart useCase(String useCaseName) {
	UseCase useCase = model.hasUseCase(useCaseName) ? model.findUseCase(useCaseName)
		: model.newUseCase(useCaseName);

	return new UseCasePart(useCase, this);
    }

    /**
     * Returns the use case model built so far.
     *
     * @return the use case model
     */
    public Model build() {
	return model;
    }
}
