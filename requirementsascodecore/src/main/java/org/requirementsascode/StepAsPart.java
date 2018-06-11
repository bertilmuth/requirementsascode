package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.systemreaction.ContinuesAfter;
import org.requirementsascode.systemreaction.ContinuesAt;
import org.requirementsascode.systemreaction.ContinuesWithoutAlternativeAt;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Step#setActors(Actor[])
 * @author b_muth
 */
public class StepAsPart {
    private Step step;
    private StepPart stepPart;

    StepAsPart(StepPart stepPart, Actor[] actors) {
	this.stepPart = stepPart;
	this.step = stepPart.getStep();

	step.setActors(actors);
	connectActorsToThisStep(step, actors);
    }

    private void connectActorsToThisStep(Step useCaseStep, Actor[] actors) {
	for (Actor actor : actors) {
	    actor.newStep(useCaseStep);
	}
    }

    /**
     * Defines the type of user command objects that this step accepts. Commands of this
     * type can cause a system reaction.
     *
     * <p>
     * Given that the step's condition is true, and the actor is right, the system
     * reacts to objects that are instances of the specified class or instances of
     * any direct or indirect subclass of the specified class.
     *
     * @param eventClass
     *            the class of commands the system reacts to in this step
     * @param <T>
     *            the type of the class
     * @return the created user part of this step
     */
    public <T> StepUserPart<T> user(Class<T> eventClass) {
	Objects.requireNonNull(eventClass);

	return new StepUserPart<>(stepPart, eventClass);
    }

    /**
     * Defines an "autonomous system reaction", meaning the system will react
     * without needing an event provided via {@link ModelRunner#reactTo(Object)}.
     * Instead, the model runner provides itself as an event to the system reaction.
     *
     * @param autonomousSystemReaction
     *            the autonomous system reaction
     * @return the created system part of this step
     */
    public StepSystemPart<ModelRunner> system(Runnable autonomousSystemReaction) {
	Objects.requireNonNull(autonomousSystemReaction);

	AutonomousSystemReaction wrappedSystemReaction = new AutonomousSystemReaction(autonomousSystemReaction);
	StepSystemPart<ModelRunner> systemPart = system(wrappedSystemReaction);
	return systemPart;
    }
    StepSystemPart<ModelRunner> system(Consumer<ModelRunner> autonomousSystemReaction) {
	StepSystemPart<ModelRunner> systemPart = user(ModelRunner.class).system(autonomousSystemReaction);
	return systemPart;
    }

    public UseCasePart continuesAt(String stepName) {
	system(new ContinuesAt(stepName, step.getUseCase()));
	return stepPart.getUseCasePart();
    }

    public UseCasePart continuesAfter(String stepName) {
	system(new ContinuesAfter(stepName, step.getUseCase()));
	return stepPart.getUseCasePart();
    }

    public UseCasePart continuesWithoutAlternativeAt(String stepName) {
	system(new ContinuesWithoutAlternativeAt(stepName, (FlowStep)step));
	return stepPart.getUseCasePart();
    }
}
