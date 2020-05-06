package org.requirementsascode.builder;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}. Wraps
 * {@link StepPart}.
 * 
 * @author b_muth
 */
public class FlowlessConditionPart {
	private final UseCasePart useCasePart;
	private final Condition optionalCondition;
	private final long flowlessStepCounter;
	private final String autoIncrementedStepName;

	FlowlessConditionPart(Condition optionalCondition, UseCasePart useCasePart, long flowlessStepCounter) {
		this.optionalCondition = optionalCondition;
		this.useCasePart = useCasePart;
		this.flowlessStepCounter = flowlessStepCounter;
		this.autoIncrementedStepName = "S" + flowlessStepCounter;
	}

	/**
	 * Creates a named step.
	 * 
	 * @param stepName the name of the created step
	 * @return the created step part
	 */
	public FlowlessStepPart step(final String stepName) {
		FlowlessStepPart flowlessStepPart = new FlowlessStepPart(stepName, useCasePart, optionalCondition,
			flowlessStepCounter);
		return flowlessStepPart;
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
		FlowlessUserPart<T> flowlessUserPart = step(autoIncrementedStepName).user(commandClass);
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
		FlowlessUserPart<T> flowlessUserPart = step(autoIncrementedStepName).on(messageClass);
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
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = step(autoIncrementedStepName).system(systemReaction);
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
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = step(autoIncrementedStepName).system(systemReaction);
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
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = step(autoIncrementedStepName).systemPublish(systemReaction);
		return flowlessSystemPart;
	}

	Condition getOptionalCondition() {
		return optionalCondition;
	}
}
