package org.requirementsascode;

import org.requirementsascode.UseCasePart.FlowlessUserPart;
import org.requirementsascode.UseCasePart.WhenPart;
import org.requirementsascode.condition.Condition;

/**
 * Class that builds a {@link Model}, in a fluent way.
 *
 * @author b_muth
 */
public class ModelBuilder {
    private static final String HANDLES_EVENTS = "Handles events";

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
	Actor actor = model.hasActor(actorName) ? model.findActor(actorName) : model.newActor(actorName);
	return actor;
    }

    /**
     * Creates a handler for events or exceptions of the specified type.
     * <p>
     * Internally, a default use case ("Handles events") is created in the model.
     * </p>
     * 
     * @param eventOrExceptionClass the specified event / exception class
     * @param <T> the type of events/exceptions
     * @return a part of the builder used to create the event handler (the "system reaction")
     */
    public <T> FlowlessUserPart<T> on(Class<T> eventOrExceptionClass) {
	FlowlessUserPart<T> on = useCase(HANDLES_EVENTS).on(eventOrExceptionClass);
	return on;
    }
    
    /**
     * Only if the specified condition is true, the event is handled.
     *
     * @param whenCondition
     *            the condition that constrains when the event is handled
     * @return a part of the builder used to define the event's class
     */
    public WhenPart when(Condition whenCondition) {
	return useCase(HANDLES_EVENTS).when(whenCondition);
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
     * Returns the model built so far.
     *
     * @return the model
     */
    public Model build() {
	return model;
    }
}
