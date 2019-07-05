package org.requirementsascode;

import java.util.Objects;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 * 
 * @author b_muth
 */
public class FlowlessSystemPart<T> {
	private long flowlessStepCounter;
	private UseCasePart useCasePart;

	FlowlessSystemPart(StepSystemPart<T> stepSystemPart, long flowlessStepCounter) {
		this.flowlessStepCounter = flowlessStepCounter;
		this.useCasePart = stepSystemPart.getStepPart().getUseCasePart();
	}

	/**
	 * Constrains the condition for triggering a system reaction: only if the
	 * specified condition is true, a system reaction can be triggered.
	 *
	 * @param condition the condition that constrains when the system reaction is
	 *                  triggered
	 * @return the created condition part
	 */
	public FlowlessConditionPart condition(Condition condition) {
		FlowlessConditionPart conditionPart = new FlowlessConditionPart(condition, useCasePart, ++flowlessStepCounter);
		return conditionPart;
	}

	/**
	 * Defines the type of commands that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param commandClass the class of commands the system reacts to
	 * @param <U>          the type of the class
	 * @return the created user part
	 */
	public <U> FlowlessUserPart<U> user(Class<U> commandClass) {
		Objects.requireNonNull(commandClass);
		FlowlessUserPart<U> flowlessUserPart = condition(null).user(commandClass);
		return flowlessUserPart;
	}

	/**
	 * Defines the type of messages or exceptions that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param messageClass the class of messages the system reacts to
	 * @param <U>          the type of the class
	 * @return the created user part
	 */
	public <U> FlowlessUserPart<U> on(Class<U> messageClass) {
		Objects.requireNonNull(messageClass);
		FlowlessUserPart<U> flowlessUserPart = condition(null).on(messageClass);
		return flowlessUserPart;
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
		Objects.requireNonNull(useCaseName);
		UseCasePart newUseCasePart = useCasePart.getModelBuilder().useCase(useCaseName);
		return newUseCasePart;
	}

	/**
	 * Returns the model that has been built.
	 * 
	 * @return the model
	 */
	public Model build() {
		return useCasePart.build();
	}
}