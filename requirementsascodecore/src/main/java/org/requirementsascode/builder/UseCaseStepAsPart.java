package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;

import org.requirementsascode.ContinueAfter;
import org.requirementsascode.ContinueAt;
import org.requirementsascode.ContinueWithoutAlternativeAt;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.UseCaseStepAs;
import org.requirementsascode.UseCaseStepUser;

public class UseCaseStepAsPart{
	private UseCaseStep useCaseStep;
	private UseCaseStepPart useCaseStepPart;

	public UseCaseStepAsPart(UseCaseStepAs useCaseStepAs, UseCaseStepPart useCaseStepPart) {
		this.useCaseStepPart = useCaseStepPart;
		this.useCaseStep = useCaseStepPart.useCaseStep();
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
		UseCaseStepUser<T> user = new UseCaseStepUser<>(useCaseStep, eventClass);
		return new UseCaseStepUserPart<>(user, useCaseStepPart);
	}
	
	/**
	 * Defines an "autonomous system reaction", meaning the system will react
	 * without needing an event provided via {@link UseCaseRunner#reactTo(Object)}. 
	 * Instead, the use case runner provides
	 * itself as an event to the system reaction.
	 * 
	 * @param systemReaction the autonomous system reaction
	 * @return the created system part of this step
	 */
	public UseCaseStepSystemPart<UseCaseRunner> system(Consumer<UseCaseRunner> systemReaction) {
		Objects.requireNonNull(systemReaction);
		UseCaseStepSystemPart<UseCaseRunner> systemPart = 
			user(UseCaseRunner.class).system(systemReaction);
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
