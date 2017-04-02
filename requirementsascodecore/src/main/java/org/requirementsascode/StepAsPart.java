package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.systemreaction.ContinueAfter;
import org.requirementsascode.systemreaction.ContinueAt;
import org.requirementsascode.systemreaction.ContinueWithoutAlternativeAt;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see Step#setActors(Actor[])
 * @author b_muth
 *
 */
public class StepAsPart{
	private Step step;
	private StepPart stepPart;

	public StepAsPart(StepPart useCaseStepPart, Actor[] actors) {
		this.stepPart = useCaseStepPart;
		this.step = useCaseStepPart.useCaseStep();
		
		step.setActors(actors);
		connectActorsToThisStep(step, actors);
	}
	
	private void connectActorsToThisStep(Step useCaseStep, Actor[] actors) {
		for (Actor actor : actors) {
			actor.newStep(useCaseStep);
		}
	}

	/**
	 * Defines the type of user event objects that this step accepts. 
	 * Events of this type can cause a system reaction.
	 * 
	 * Given that the step's predicate is true, and the actor is right, the system reacts 
	 * to objects that are instances of the specified class or instances of any direct or
	 * indirect subclass of the specified class.
	 * 
	 * @param eventClass the class of events the system reacts to in this step
	 * @param <T> the type of the class
	 * @return the created user part of this step
	 */
	public <T> StepUserPart<T> user(Class<T> eventClass) {
		Objects.requireNonNull(eventClass);
		return new StepUserPart<>(stepPart, eventClass);
	}
	
	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing an event provided via {@link UseCaseModelRunner#reactTo(Object)}. 
	 * Instead, the use case model runner provides
	 * itself as an event to the system reaction.
	 * 
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public StepSystemPart<UseCaseModelRunner> system(Consumer<UseCaseModelRunner> systemReaction) {
		Objects.requireNonNull(systemReaction);
		StepSystemPart<UseCaseModelRunner> systemPart = 
			user(UseCaseModelRunner.class).system(systemReaction);
		return systemPart;
	} 

	public UseCasePart continueAt(String stepName) {
		system(new ContinueAt(step.useCase(), stepName)); 
		return stepPart.useCasePart();
	}

	public UseCasePart continueAfter(String stepName) {		
		system(new ContinueAfter(step.useCase(), stepName));
		return stepPart.useCasePart();
	}

	public UseCasePart continueWithoutAlternativeAt(String stepName) {
		system(new ContinueWithoutAlternativeAt(step.useCase(), stepName));
		return stepPart.useCasePart();
	}
}
