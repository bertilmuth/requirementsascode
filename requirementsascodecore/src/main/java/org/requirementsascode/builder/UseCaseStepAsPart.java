package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.systemreaction.ContinueAfter;
import org.requirementsascode.systemreaction.ContinueAt;
import org.requirementsascode.systemreaction.ContinueWithoutAlternativeAt;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 * 
 * @see UseCaseStep#setActors(Actor[])
 * @author b_muth
 *
 */
public class UseCaseStepAsPart{
	private UseCaseStep useCaseStep;
	private UseCaseStepPart useCaseStepPart;

	public UseCaseStepAsPart(UseCaseStepPart useCaseStepPart, Actor[] actors) {
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseStep = useCaseStepPart.useCaseStep();
		
		useCaseStep.setActors(actors);
		connectActorsToThisStep(useCaseStep, actors);
	}
	
	private void connectActorsToThisStep(UseCaseStep useCaseStep, Actor[] actors) {
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
	public <T> UseCaseStepUserPart<T> user(Class<T> eventClass) {
		Objects.requireNonNull(eventClass);
		return new UseCaseStepUserPart<>(useCaseStepPart, eventClass);
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
	public UseCaseStepSystemPart<UseCaseModelRunner> system(Consumer<UseCaseModelRunner> systemReaction) {
		Objects.requireNonNull(systemReaction);
		UseCaseStepSystemPart<UseCaseModelRunner> systemPart = 
			user(UseCaseModelRunner.class).system(systemReaction);
		return systemPart;
	} 

	public UseCasePart continueAt(String stepName) {
		system(new ContinueAt(useCaseStep.useCase(), stepName)); 
		return useCaseStepPart.useCasePart();
	}

	public UseCasePart continueAfter(String stepName) {		
		system(new ContinueAfter(useCaseStep.useCase(), stepName));
		return useCaseStepPart.useCasePart();
	}

	public UseCasePart continueWithoutAlternativeAt(String stepName) {
		system(new ContinueWithoutAlternativeAt(useCaseStep.useCase(), stepName));
		return useCaseStepPart.useCasePart();
	}
}
