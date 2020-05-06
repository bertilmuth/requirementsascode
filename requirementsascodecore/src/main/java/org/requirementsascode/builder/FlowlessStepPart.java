package org.requirementsascode.builder;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.requirementsascode.Condition;
import org.requirementsascode.ModelRunner;

public class FlowlessStepPart {
	private final StepPart stepPart;
	private final long flowlessStepCounter;

	FlowlessStepPart(String stepName, UseCasePart useCasePart, Condition optionalCondition, long flowlessStepCounter) {
		this.stepPart = new StepPart(stepName, useCasePart, optionalCondition);
		this.flowlessStepCounter = flowlessStepCounter;
	}

	/**
	 * Defines the type of user commands that this step accepts. Commands of this
	 * type can cause a system reaction.
	 *
	 * <p>
	 * Given that the step's condition is true, the system reacts to objects that
	 * are instances of the specified class or instances of any direct or indirect
	 * subclass of the specified class.
	 *
	 * @param commandClass the class of commands the system reacts to in this step
	 * @param <T>          the type of the class
	 * @return the created user part of this step
	 */
	public <T> FlowlessUserPart<T> user(Class<T> commandClass) {
		StepUserPart<T> stepUserPart = stepPart.user(commandClass);
		FlowlessUserPart<T> flowlessUserPart = new FlowlessUserPart<>(stepUserPart, flowlessStepCounter);
		return flowlessUserPart;
	}

	/**
	 * Defines the type of messages or exceptions that will cause a system reaction,
	 * given that the condition is fulfilled.
	 *
	 * <p>
	 * Given that the step's condition is true, the system reacts to objects that
	 * are instances of the specified class or instances of any direct or indirect
	 * subclass of the specified class.
	 *
	 * @param messageClass the class of messages the system reacts to
	 * @param <T>          the type of the class
	 * @return the created user part of this step
	 */
	public <T> FlowlessUserPart<T> on(Class<T> messageClass) {
		StepUserPart<T> stepUserPart = stepPart.on(messageClass);
		FlowlessUserPart<T> flowlessUserPart = new FlowlessUserPart<>(stepUserPart, flowlessStepCounter);
		return flowlessUserPart;
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
	 *
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public FlowlessSystemPart<ModelRunner> system(Runnable systemReaction) {
		StepSystemPart<ModelRunner> stepSystemPart = stepPart.system(systemReaction);
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
		return flowlessSystemPart;
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
	 * Instead, the model runner provides itself as an event to the system reaction.
	 *
	 * @param systemReaction the autonomous system reaction (that needs information
	 *                       from a model runner to work)
	 * @return the created system part of this step
	 */
	public FlowlessSystemPart<ModelRunner> system(Consumer<ModelRunner> systemReaction) {
		StepSystemPart<ModelRunner> stepSystemPart = stepPart.system(systemReaction);
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
		return flowlessSystemPart;
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
	 * After executing the system reaction, the runner will publish the returned
	 * event.
	 *
	 * @param systemReaction the autonomous system reaction, that returns a single
	 *                       event to be published.
	 * @return the created system part of this step
	 */
	public FlowlessSystemPart<ModelRunner> systemPublish(Supplier<?> systemReaction) {
		StepSystemPart<ModelRunner> stepSystemPart = stepPart.systemPublish(systemReaction);
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = new FlowlessSystemPart<>(stepSystemPart, flowlessStepCounter);
		return flowlessSystemPart;
	}
}
