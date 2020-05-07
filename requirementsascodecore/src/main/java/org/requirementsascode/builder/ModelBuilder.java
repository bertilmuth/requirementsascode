package org.requirementsascode.builder;

import static org.requirementsascode.builder.UseCasePart.useCasePart;

import org.requirementsascode.Actor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

/**
 * Class that builds a {@link Model}, in a fluent way.
 *
 * @author b_muth
 */
public class ModelBuilder {
	private static final String HANDLES_MESSAGES = "Handles messages";

	private Model model;

	public ModelBuilder(Model model) {
		this.model = model;
	}

	/**
	 * Creates a new actor in the current model. If an actor with the specified name
	 * already exists, returns the existing actor.
	 *
	 * @param actorName the name of the existing actor / actor to be created.
	 * @return the created / found actor.
	 */
	public Actor actor(String actorName) {
		Actor actor = model.hasActor(actorName) ? model.findActor(actorName) : model.newActor(actorName);
		return actor;
	}
	
	/**
	 * Only if the specified condition is true, the message is handled.
	 *
	 * @param condition the condition that constrains when the message is handled
	 * @return a part of the builder used to define the message class
	 */
	public FlowlessConditionPart condition(Condition condition) {
		return useCase(HANDLES_MESSAGES).condition(condition);
	}
	
	/**
	 * Creates a named step.
	 * 
	 * @param stepName the name of the created step
	 * @return the created step part
	 */
	public FlowlessStepPart step(String stepName) {
		FlowlessStepPart stepPart = useCase(HANDLES_MESSAGES).step(stepName);
		return stepPart;
	}

	/**
	 * Creates a handler for commands of the specified type.
	 * <p>
	 * Internally, a default use case ("Handles messages") is created in the model.
	 * </p>
	 * 
	 * @param commandClass the specified command class
	 * @param <T>          the type of command
	 * @return a part of the builder used to create the message handler (the "system
	 *         reaction")
	 */
	public <T> FlowlessUserPart<T> user(Class<T> commandClass) {
		FlowlessUserPart<T> userPart = useCase(HANDLES_MESSAGES).user(commandClass);
		return userPart;
	}

	/**
	 * Creates a handler for messages or exceptions of the specified type.
	 * <p>
	 * Internally, a default use case ("Handles messages") is created in the model.
	 * </p>
	 * 
	 * @param messageClass the specified type of messages
	 * @param <T>                   the type of messages
	 * @return a part of the builder used to create the message handler (the "system
	 *         reaction")
	 */
	public <T> FlowlessUserPart<T> on(Class<T> messageClass) {
		FlowlessUserPart<T> userPart = useCase(HANDLES_MESSAGES).on(messageClass);
		return userPart;
	}

	/**
	 * Creates a new use case in the current model, and returns a part for building
	 * its details. If a use case with the specified name already exists, returns a
	 * part for the existing use case.
	 *
	 * @param useCaseName the name of the existing use case / use case to be
	 *                    created.
	 * @return the created / found use case's part.
	 */
	public UseCasePart useCase(String useCaseName) {
		return useCasePart(useCaseName, this);
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
