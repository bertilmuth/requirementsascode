package org.requirementsascode;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}. Wraps
 * {@link StepPart}.
 * 
 * @author b_muth
 */
public class FlowlessConditionPart {
	private UseCasePart useCasePart;
	private Condition optionalCondition;
	private long flowlessStepCounter;
	private FlowlessStepPart flowlessStepPart;
	private final String autoIncrementedStepName;

	FlowlessConditionPart(Condition optionalCondition, UseCasePart useCasePart, long flowlessStepCounter) {
		this.optionalCondition = optionalCondition;
		this.useCasePart = useCasePart;
		this.flowlessStepCounter = flowlessStepCounter;
		autoIncrementedStepName = "S" + flowlessStepCounter;
		this.flowlessStepPart = step(autoIncrementedStepName);
	}

	private FlowlessStepPart step(final String stepName) {
		UseCase useCase = useCasePart.getUseCase();
		FlowlessStep newStep = useCase.newFlowlessStep(optionalCondition, stepName);
		StepPart stepPart = new StepPart(newStep, useCasePart, null);
		FlowlessStepPart flowlessStepPart = new FlowlessStepPart(stepPart, flowlessStepCounter);
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
		FlowlessUserPart<T> flowlessUserPart = flowlessStepPart.user(commandClass);
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
		FlowlessUserPart<T> flowlessUserPart = flowlessStepPart.on(messageClass);
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
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = flowlessStepPart.system(systemReaction);
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
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = flowlessStepPart.system(systemReaction);
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
		FlowlessSystemPart<ModelRunner> flowlessSystemPart = flowlessStepPart.systemPublish(systemReaction);
		return flowlessSystemPart;
	}

	Condition getOptionalCondition() {
		return optionalCondition;
	}
}
