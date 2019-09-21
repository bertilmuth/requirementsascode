package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.systemreaction.IgnoresIt;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @author b_muth
 */
public class StepUserPart<T> {
	private StepPart stepPart;
	private Step step;

	StepUserPart(Class<T> messageClass, StepPart stepPart) {
		this.stepPart = stepPart;
		this.step = stepPart.getStep();
		step.setMessageClass(messageClass);
	}

	/**
	 * Defines the system reaction. The system will react as specified to the
	 * message passed in, when {@link ModelRunner#reactTo(Object)} is called.
	 *
	 * @param systemReaction the specified system reaction
	 * @return the created system part of this step
	 */
	public StepSystemPart<T> system(Consumer<T> systemReaction) {
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		return createStepSystemPart(systemReactionObject);
	}

	private StepSystemPart<T> createStepSystemPart(SystemReaction<T> systemReactionObject) {
		Objects.requireNonNull(systemReactionObject);
		return new StepSystemPart<>(systemReactionObject, stepPart);
	}

	/**
	 * Defines the system reaction. The system will react as specified to the
	 * message passed in, when you call {@link ModelRunner#reactTo(Object)}. After
	 * executing the system reaction, the runner will publish the returned events.
	 *
	 * @param systemReaction the specified system reaction, that returns an event to
	 *                       be published.
	 * @return the created system part of this step
	 */
	public StepSystemPart<T> systemPublish(Function<T, Object> systemReaction) {
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		return createStepSystemPart(systemReactionObject);
	}
	
	StepSystemPart<T> systemPublish(Supplier<Object> systemReaction) {
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		return createStepSystemPart(systemReactionObject);
	}
	
	/**
	 * Defines the system reaction. The system will react as specified, but it will
	 * ignore the message passed in, when {@link ModelRunner#reactTo(Object)} is
	 * called.
	 *
	 * @param systemReaction the specified system reaction
	 * @return the created system part of this step
	 */
	public StepSystemPart<T> system(Runnable systemReaction) {
		SystemReaction<T> systemReactionObject = new SystemReaction<>(systemReaction);
		return createStepSystemPart(systemReactionObject);
	}

	/**
	 * Creates a new step in this flow, with the specified name, that follows the
	 * current step in sequence.
	 *
	 * @param stepName the name of the step to be created
	 * @return the newly created step
	 * @throws ElementAlreadyInModel if a step with the specified name already
	 *                               exists in the use case
	 */
	public StepPart step(String stepName) {
		return system(new IgnoresIt<T>()).step(stepName);
	}
}
