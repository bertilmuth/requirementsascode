package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;
import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.systemreaction.IgnoresIt;

import static org.requirementsascode.builder.StepSystemPart.*;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @author b_muth
 */
public class StepUserPart<T> {
	private StepPart stepPart;
	private Step step;

	private StepUserPart(Class<T> messageClass, StepPart stepPart) {
		this.stepPart = Objects.requireNonNull(stepPart);
		this.step = stepPart.getStep();
		step.setMessageClass(messageClass);
	}
	
	static <T> StepUserPart<T> stepUserPart(Class<T> messageClass, StepPart stepPart) {
		return new StepUserPart<>(messageClass, stepPart);
	}

	/**
	 * Defines the system reaction. The system will react as specified to the
	 * message passed in, when {@link ModelRunner#reactTo(Object)} is called.
	 *
	 * @param systemReaction the specified system reaction
	 * @return the created system part of this step
	 */
	public StepSystemPart<T> system(Consumer<? super T> systemReaction) {
		return stepSystemPartWithConsumer(systemReaction, stepPart);
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
		return stepSystemPartWithRunnable(systemReaction, stepPart);
	}

	/**
	 * Defines the system reaction. The system will react as specified to the
	 * message passed in, when you call {@link ModelRunner#reactTo(Object)}. After
	 * executing the system reaction, the runner will publish the returned event.
	 *
	 * @param systemReaction the specified system reaction, that returns an event to
	 *                       be published.
	 * @return the created system part of this step
	 */
	public StepSystemPart<T> systemPublish(Function<? super T, ?> systemReaction) {
		return stepSystemPartWithFunction(systemReaction, stepPart);
	}
	
	StepSystemPart<T> systemPublish(Supplier<?> systemReaction) {
		return stepSystemPartWithSupplier(systemReaction, stepPart);
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
		return system(new IgnoresIt<>()).step(stepName);
	}
}
