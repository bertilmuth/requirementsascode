package org.requirementsascode.builder;

import static org.requirementsascode.builder.StepUserPart.stepUserPart;

import java.util.Objects;
import java.util.function.Supplier;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.FlowStep;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;
import org.requirementsascode.exception.NoSuchElementInModel;
import org.requirementsascode.systemreaction.ContinuesAfter;
import org.requirementsascode.systemreaction.ContinuesAt;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Step#setActors(AbstractActor[])
 * @author b_muth
 */
public class StepAsPart {
	private Step step;
	private StepPart stepPart;

	private StepAsPart(AbstractActor[] actors, StepPart stepPart) {
		Objects.requireNonNull(actors);
		this.stepPart = Objects.requireNonNull(stepPart);
		this.step = stepPart.getStep();

		step.setActors(actors);
	}
	
	static StepAsPart stepAsPart(AbstractActor[] actors, StepPart stepPart) {
		return new StepAsPart(actors, stepPart);
	}

	/**
	 * Defines the type of user commands that this step accepts. Commands of this
	 * type can cause a system reaction.
	 *
	 * <p>
	 * Given that the step's condition is true, and the actor is right, the system
	 * reacts to objects that are instances of the specified class or instances of
	 * any direct or indirect subclass of the specified class.
	 *
	 * @param commandClass the class of commands the system reacts to in this step
	 * @param <T>          the type of the class
	 * @return the created user part of this step
	 */
	public <T> StepUserPart<T> user(Class<T> commandClass) {
		Objects.requireNonNull(commandClass);
		return stepUserPart(commandClass, stepPart);
	}

	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing a message provided via {@link ModelRunner#reactTo(Object)}.
	 *
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public StepSystemPart<ModelRunner> system(Runnable systemReaction) {
		StepSystemPart<ModelRunner> systemPart = user(ModelRunner.class).system(systemReaction);
		return systemPart;
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
	public StepSystemPart<ModelRunner> systemPublish(Supplier<?> systemReaction) {
		@SuppressWarnings("unchecked")
		StepSystemPart<ModelRunner> systemPart = user(ModelRunner.class).systemPublish((Supplier<Object>)systemReaction);
		return systemPart;
	}

	/**
	 * Positions the modelRunner after the specified step, and reevaluates which flows can start from there.
	 *
	 * @param stepName name of the step to continue after, in this use case.
	 * @return the use case part this step belongs to, to ease creation of further
	 *         flows
	 * @throws NoSuchElementInModel if no step with the specified stepName is found
	 *                              in the current use case
	 */
	public UseCasePart continuesAfter(String stepName) {
		user(ModelRunner.class).system(new ContinuesAfter(stepName, step.getUseCase()));
		return stepPart.getUseCasePart();
	}

  /**
   * Makes the model runner continue at the specified step. 
   * 
   * IMPORTANT NOTE: given you have specified continuesAt(x), 
   * if there is an alternative flow with an insteadOf(x) condition,
   * that alternative flow will be given preference to x if its condition is fulfilled.
   *
   * @param stepName name of the step to continue at, in this use case.
   * @return the use case part this step belongs to, to ease creation of further
   *         flows
   * @throws NoSuchElementInModel if no step with the specified stepName is found
   *                              in the current use case
   */
	public UseCasePart continuesAt(String stepName) {
		user(ModelRunner.class).system(new ContinuesAt(stepName, (FlowStep) step));
		return stepPart.getUseCasePart();
	}
}
