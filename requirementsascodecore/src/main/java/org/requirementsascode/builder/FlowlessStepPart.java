package org.requirementsascode.builder;

import java.util.function.Supplier;
import static org.requirementsascode.builder.FlowlessUserPart.*;


import org.requirementsascode.Condition;
import org.requirementsascode.ModelRunner;

public class FlowlessStepPart {
	private final StepPart stepPart;
	private final long flowlessStepCounter;

	private FlowlessStepPart(String stepName, UseCasePart useCasePart, Condition optionalCondition, long flowlessStepCounter) {
		this.stepPart = org.requirementsascode.builder.StepPart.flowlessStepPart(stepName, useCasePart, optionalCondition);
		this.flowlessStepCounter = flowlessStepCounter;
	}
	
	static FlowlessStepPart flowlessStepPart(String stepName, UseCasePart useCasePart, Condition optionalCondition, long flowlessStepCounter) {
		return new FlowlessStepPart(stepName, useCasePart, optionalCondition, flowlessStepCounter);
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
		FlowlessUserPart<T> flowlessUserPart = flowlessUserPart(commandClass, stepPart, flowlessStepCounter);
		return flowlessUserPart;
	}

	/**
	 * Defines the type of system event objects or exceptions that this step
	 * handles. Events of the specified type can cause a system reaction.
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
		FlowlessUserPart<T> flowlessUserPart = flowlessOnPart(messageClass, stepPart, flowlessStepCounter);
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
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = user(ModelRunner.class).system(systemReaction);
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
		@SuppressWarnings("unchecked")
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = user(ModelRunner.class).systemPublish(
			(Supplier<Object>) systemReaction);
		return flowlessSystemPart;
	}
}
